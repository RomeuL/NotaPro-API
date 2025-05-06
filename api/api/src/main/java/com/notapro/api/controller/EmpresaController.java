package com.notapro.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notapro.api.dto.EmpresaDTO;
import com.notapro.api.repository.EmpresaRepository;
import com.notapro.api.service.EmpresaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {
    
    private final EmpresaService empresaService;
    private final EmpresaRepository empresaRepository;
    
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> getAllEmpresas() {
        return ResponseEntity.ok(empresaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> getEmpresaById(@PathVariable Integer id) {
        return empresaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createEmpresa(@Valid @RequestBody EmpresaDTO empresaDTO) {
        if (empresaRepository.existsByCnpj(empresaDTO.getCnpj())) {
            return ResponseEntity
                    .badRequest()
                    .body("Erro: CNPJ já está cadastrado!");
        }
        
        EmpresaDTO savedEmpresa = empresaService.save(empresaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmpresa);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpresaDTO> updateEmpresa(@PathVariable Integer id, @Valid @RequestBody EmpresaDTO empresaDTO) {
        return empresaService.update(id, empresaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Integer id) {
        if (!empresaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        empresaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}