package com.notapro.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.notapro.api.dto.EmpresaDTO;
import com.notapro.api.model.Empresa;
import com.notapro.api.repository.EmpresaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpresaService {
    
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;
    
    public List<EmpresaDTO> findAll() {
        return empresaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<EmpresaDTO> findById(Integer id) {
        return empresaRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public Optional<EmpresaDTO> findByCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .map(this::convertToDTO);
    }
    
    public EmpresaDTO save(EmpresaDTO empresaDTO) {
        Empresa empresa = convertToEntity(empresaDTO);
        Empresa savedEmpresa = empresaRepository.save(empresa);
        return convertToDTO(savedEmpresa);
    }
    
    public Optional<EmpresaDTO> update(Integer id, EmpresaDTO empresaDTO) {
        return empresaRepository.findById(id)
                .map(existingEmpresa -> {
                    existingEmpresa.setNome(empresaDTO.getNome());
                    existingEmpresa.setCnpj(empresaDTO.getCnpj());
                    return convertToDTO(empresaRepository.save(existingEmpresa));
                });
    }
    
    public void delete(Integer id) {
        empresaRepository.deleteById(id);
    }
    
    private EmpresaDTO convertToDTO(Empresa empresa) {
        return modelMapper.map(empresa, EmpresaDTO.class);
    }
    
    private Empresa convertToEntity(EmpresaDTO empresaDTO) {
        return modelMapper.map(empresaDTO, Empresa.class);
    }
}