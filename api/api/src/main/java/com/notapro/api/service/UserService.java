package com.notapro.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.notapro.api.dto.user.UserInputDTO;
import com.notapro.api.dto.user.UserOutputDTO;
import com.notapro.api.model.User;
import com.notapro.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    
    public List<UserOutputDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToOutputDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<UserOutputDTO> findById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToOutputDTO);
    }
    
    public Optional<UserOutputDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToOutputDTO);
    }
    
    public UserOutputDTO save(UserInputDTO userInputDTO) {
        User user = convertToEntity(userInputDTO);
        user.setSenha(passwordEncoder.encode(userInputDTO.getSenha()));
        User savedUser = userRepository.save(user);
        return convertToOutputDTO(savedUser);
    }
    
    public Optional<UserOutputDTO> update(Integer id, UserInputDTO userInputDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setNome(userInputDTO.getNome());
                    existingUser.setEmail(userInputDTO.getEmail());
                    if (userInputDTO.getSenha() != null && !userInputDTO.getSenha().isEmpty()) {
                        existingUser.setSenha(passwordEncoder.encode(userInputDTO.getSenha()));
                    }
                    existingUser.setRole(userInputDTO.getRole());
                    return convertToOutputDTO(userRepository.save(existingUser));
                });
    }
    
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
    
    private UserOutputDTO convertToOutputDTO(User user) {
        return modelMapper.map(user, UserOutputDTO.class);
    }
    
    private User convertToEntity(UserInputDTO userInputDTO) {
        return modelMapper.map(userInputDTO, User.class);
    }
}