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

import com.notapro.api.dto.empresa.EmpresaInputDTO;
import com.notapro.api.dto.empresa.EmpresaOutputDTO;
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
    public ResponseEntity<List<EmpresaOutputDTO>> getAllEmpresas() {
        return ResponseEntity.ok(empresaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaOutputDTO> getEmpresaById(@PathVariable Integer id) {
        return empresaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createEmpresa(@Valid @RequestBody EmpresaInputDTO empresaInputDTO) {
        if (empresaRepository.existsByCnpj(empresaInputDTO.getCnpj())) {
            return ResponseEntity
                    .badRequest()
                    .body("Erro: CNPJ já está cadastrado!");
        }
        
        EmpresaOutputDTO savedEmpresa = empresaService.save(empresaInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmpresa);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpresaOutputDTO> updateEmpresa(
            @PathVariable Integer id, 
            @Valid @RequestBody EmpresaInputDTO empresaInputDTO) {
        return empresaService.update(id, empresaInputDTO)
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