package com.notapro.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.notapro.api.dto.UserDTO;
import com.notapro.api.model.User;
import com.notapro.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<UserDTO> findById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDTO);
    }
    
    public UserDTO save(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user.setSenha(passwordEncoder.encode(userDTO.getSenha()));
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    public Optional<UserDTO> update(Integer id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setNome(userDTO.getNome());
                    existingUser.setEmail(userDTO.getEmail());
                    if (userDTO.getSenha() != null && !userDTO.getSenha().isEmpty()) {
                        existingUser.setSenha(passwordEncoder.encode(userDTO.getSenha()));
                    }
                    existingUser.setRole(userDTO.getRole());
                    return convertToDTO(userRepository.save(existingUser));
                });
    }
    
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setSenha(null); // Não retornamos a senha
        return userDTO;
    }
    
    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}