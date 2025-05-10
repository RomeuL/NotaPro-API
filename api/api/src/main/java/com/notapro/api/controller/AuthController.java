package com.notapro.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notapro.api.dto.AuthRequestDTO;
import com.notapro.api.dto.AuthResponseDTO;
import com.notapro.api.dto.UserDTO;
import com.notapro.api.model.User;
import com.notapro.api.repository.UserRepository;
import com.notapro.api.security.jwt.JwtUtils;
import com.notapro.api.service.EmailService;
import com.notapro.api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        AuthResponseDTO response = new AuthResponseDTO(
                jwt,
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getRole().name()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Erro: Email já está em uso!");
        }
        
        UserDTO savedUser = userService.save(userDTO);
        
        try {
            emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getNome());
            log.info("Email de boas-vindas agendado para: {}", savedUser.getEmail());
        } catch (Exception e) {
            log.error("Erro ao enviar email de boas-vindas no controller: {}", e.getMessage());
            try {
                emailService.sendWelcomeEmailSimple(savedUser.getEmail(), savedUser.getNome());
            } catch (Exception ex) {
                log.error("Também falhou ao enviar email simples: {}", ex.getMessage());
            }
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}