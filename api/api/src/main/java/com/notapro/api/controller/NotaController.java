package com.notapro.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notapro.api.dto.nota.NotaInputDTO;
import com.notapro.api.dto.nota.NotaOutputDTO;
import com.notapro.api.model.enums.StatusNota;
import com.notapro.api.service.NotaService;
import com.notapro.api.service.RealTimeNotaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notas")
@RequiredArgsConstructor
public class NotaController {
    
    private final NotaService notaService;
    private final RealTimeNotaService realTimeNotaService;
    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotaOutputDTO>> getAllNotas() {
        return ResponseEntity.ok(notaService.findAll());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotaOutputDTO> getNotaById(@PathVariable Integer id) {
        return notaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/empresa/{empresaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotaOutputDTO>> getNotasByEmpresa(@PathVariable Integer empresaId) {
        return ResponseEntity.ok(notaService.findByEmpresaId(empresaId));
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotaOutputDTO>> getNotasByStatus(@PathVariable StatusNota status) {
        return ResponseEntity.ok(notaService.findByStatus(status));
    }
    
    @GetMapping("/vencimento")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotaOutputDTO>> getNotasByVencimentoRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(notaService.findByVencimentoRange(inicio, fim));
    }
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotaOutputDTO> createNota(@Valid @RequestBody NotaInputDTO notaInputDTO) {
        NotaOutputDTO savedNota = notaService.save(notaInputDTO);
        realTimeNotaService.atualizarEstatisticasAgora();
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNota);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotaOutputDTO> updateNota(
            @PathVariable Integer id, 
            @Valid @RequestBody NotaInputDTO notaInputDTO) {
        return notaService.update(id, notaInputDTO)
                .map(responseNota -> {
                    realTimeNotaService.atualizarEstatisticasAgora();
                    return ResponseEntity.ok(responseNota);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNota(@PathVariable Integer id) {
        if (!notaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        notaService.delete(id);
        realTimeNotaService.atualizarEstatisticasAgora();
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/numero/{numeroNota}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotaOutputDTO> getNotaByNumero(@PathVariable String numeroNota) {
        return notaService.findByNumeroNota(numeroNota)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}