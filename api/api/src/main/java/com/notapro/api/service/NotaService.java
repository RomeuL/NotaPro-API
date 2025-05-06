package com.notapro.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.notapro.api.dto.NotaDTO;
import com.notapro.api.model.Empresa;
import com.notapro.api.model.Nota;
import com.notapro.api.model.enums.StatusNota;
import com.notapro.api.model.enums.TipoPagamento;
import com.notapro.api.repository.EmpresaRepository;
import com.notapro.api.repository.NotaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotaService {
    
    private final NotaRepository notaRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;
    
    public List<NotaDTO> findAll() {
        return notaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<NotaDTO> findById(Integer id) {
        return notaRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public List<NotaDTO> findByEmpresaId(Integer empresaId) {
        return notaRepository.findByEmpresaId(empresaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<NotaDTO> findByStatus(StatusNota status) {
        return notaRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<NotaDTO> findByVencimentoRange(LocalDate inicio, LocalDate fim) {
        return notaRepository.findByDataVencimentoBetween(inicio, fim).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public NotaDTO save(NotaDTO notaDTO) {
        validateNota(notaDTO);
        Nota nota = convertToEntity(notaDTO);
        Nota savedNota = notaRepository.save(nota);
        return convertToDTO(savedNota);
    }
    
    public Optional<NotaDTO> update(Integer id, NotaDTO notaDTO) {
        validateNota(notaDTO);
        return notaRepository.findById(id)
                .map(existingNota -> {
                    // Update fields
                    existingNota.setDescricao(notaDTO.getDescricao());
                    
                    // Handle empresa ID
                    Empresa empresa = empresaRepository.findById(notaDTO.getEmpresaId())
                            .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
                    existingNota.setEmpresa(empresa);
                    
                    existingNota.setDataEmissao(notaDTO.getDataEmissao());
                    existingNota.setDataVencimento(notaDTO.getDataVencimento());
                    existingNota.setValor(notaDTO.getValor());
                    existingNota.setTipoPagamento(notaDTO.getTipoPagamento());
                    
                    // Only set numeroBoleto if payment type is BOLETO
                    if (notaDTO.getTipoPagamento() == TipoPagamento.BOLETO) {
                        existingNota.setNumeroBoleto(notaDTO.getNumeroBoleto());
                    } else {
                        existingNota.setNumeroBoleto(null);
                    }
                    
                    existingNota.setStatus(notaDTO.getStatus());
                    
                    return convertToDTO(notaRepository.save(existingNota));
                });
    }
    
    public void delete(Integer id) {
        notaRepository.deleteById(id);
    }
    
    // Validate nota details
    private void validateNota(NotaDTO notaDTO) {
        // Check if empresa exists
        empresaRepository.findById(notaDTO.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        
        // Check if numeroBoleto is provided when payment type is BOLETO
        if (notaDTO.getTipoPagamento() == TipoPagamento.BOLETO && 
                (notaDTO.getNumeroBoleto() == null || notaDTO.getNumeroBoleto().trim().isEmpty())) {
            throw new IllegalArgumentException("Número do boleto é obrigatório para pagamento via boleto");
        }
    }
    
    private NotaDTO convertToDTO(Nota nota) {
        NotaDTO notaDTO = modelMapper.map(nota, NotaDTO.class);
        notaDTO.setEmpresaId(nota.getEmpresa().getId());
        return notaDTO;
    }
    
    private Nota convertToEntity(NotaDTO notaDTO) {
        Nota nota = modelMapper.map(notaDTO, Nota.class);
        
        // Map empresa
        if (notaDTO.getEmpresaId() != null) {
            Empresa empresa = empresaRepository.findById(notaDTO.getEmpresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
            nota.setEmpresa(empresa);
        }
        
        return nota;
    }
}