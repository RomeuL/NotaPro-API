package com.notapro.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.notapro.api.dto.nota.NotaInputDTO;
import com.notapro.api.dto.nota.NotaOutputDTO;
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
    
    public List<NotaOutputDTO> findAll() {
        return notaRepository.findAll().stream()
                .map(this::convertToOutputDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<NotaOutputDTO> findById(Integer id) {
        return notaRepository.findById(id)
                .map(this::convertToOutputDTO);
    }
    
    public List<NotaOutputDTO> findByEmpresaId(Integer empresaId) {
        return notaRepository.findByEmpresaId(empresaId).stream()
                .map(this::convertToOutputDTO)
                .collect(Collectors.toList());
    }
    
    public List<NotaOutputDTO> findByStatus(StatusNota status) {
        return notaRepository.findByStatus(status).stream()
                .map(this::convertToOutputDTO)
                .collect(Collectors.toList());
    }
    
    public List<NotaOutputDTO> findByVencimentoRange(LocalDate inicio, LocalDate fim) {
        return notaRepository.findByDataVencimentoBetween(inicio, fim).stream()
                .map(this::convertToOutputDTO)
                .collect(Collectors.toList());
    }
    
    public NotaOutputDTO save(NotaInputDTO notaInputDTO) {
        validateNota(notaInputDTO);
        Nota nota = convertToEntity(notaInputDTO);
        Nota savedNota = notaRepository.save(nota);
        return convertToOutputDTO(savedNota);
    }
    
    public Optional<NotaOutputDTO> update(Integer id, NotaInputDTO notaInputDTO) {
        validateNota(notaInputDTO);
        return notaRepository.findById(id)
                .map(existingNota -> {
                    existingNota.setDescricao(notaInputDTO.getDescricao());
                    
                    Empresa empresa = empresaRepository.findById(notaInputDTO.getEmpresaId())
                            .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
                    existingNota.setEmpresa(empresa);
                    
                    existingNota.setDataEmissao(notaInputDTO.getDataEmissao());
                    existingNota.setDataVencimento(notaInputDTO.getDataVencimento());
                    existingNota.setValor(notaInputDTO.getValor());
                    existingNota.setTipoPagamento(notaInputDTO.getTipoPagamento());
                    
                    if (notaInputDTO.getTipoPagamento() == TipoPagamento.BOLETO) {
                        existingNota.setNumeroBoleto(notaInputDTO.getNumeroBoleto());
                    } else {
                        existingNota.setNumeroBoleto(null);
                    }
                    
                    existingNota.setStatus(notaInputDTO.getStatus());
                    
                    return convertToOutputDTO(notaRepository.save(existingNota));
                });
    }
    
    public void delete(Integer id) {
        notaRepository.deleteById(id);
    }
    
    private void validateNota(NotaInputDTO notaInputDTO) {
        empresaRepository.findById(notaInputDTO.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        
        if (notaInputDTO.getTipoPagamento() == TipoPagamento.BOLETO && 
                (notaInputDTO.getNumeroBoleto() == null || notaInputDTO.getNumeroBoleto().trim().isEmpty())) {
            throw new IllegalArgumentException("Número do boleto é obrigatório para pagamento via boleto");
        }
    }
    
    private NotaOutputDTO convertToOutputDTO(Nota nota) {
        NotaOutputDTO notaDTO = modelMapper.map(nota, NotaOutputDTO.class);
        notaDTO.setEmpresaId(nota.getEmpresa().getId());
        notaDTO.setEmpresaNome(nota.getEmpresa().getNome());
        return notaDTO;
    }
    
    private Nota convertToEntity(NotaInputDTO notaInputDTO) {
        Nota nota = modelMapper.map(notaInputDTO, Nota.class);
        
        if (notaInputDTO.getEmpresaId() != null) {
            Empresa empresa = empresaRepository.findById(notaInputDTO.getEmpresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
            nota.setEmpresa(empresa);
        }
        
        return nota;
    }
}