package com.notapro.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.notapro.api.dto.empresa.EmpresaInputDTO;
import com.notapro.api.dto.empresa.EmpresaOutputDTO;
import com.notapro.api.model.Empresa;
import com.notapro.api.repository.EmpresaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpresaService {
    
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;
    
    public List<EmpresaOutputDTO> findAll() {
        return empresaRepository.findAll().stream()
                .map(this::convertToOutputDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<EmpresaOutputDTO> findById(Integer id) {
        return empresaRepository.findById(id)
                .map(this::convertToOutputDTO);
    }
    
    public Optional<EmpresaOutputDTO> findByCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .map(this::convertToOutputDTO);
    }
    
    public EmpresaOutputDTO save(EmpresaInputDTO empresaInputDTO) {
        Empresa empresa = convertToEntity(empresaInputDTO);
        Empresa savedEmpresa = empresaRepository.save(empresa);
        return convertToOutputDTO(savedEmpresa);
    }
    
    public Optional<EmpresaOutputDTO> update(Integer id, EmpresaInputDTO empresaInputDTO) {
        return empresaRepository.findById(id)
                .map(existingEmpresa -> {
                    existingEmpresa.setNome(empresaInputDTO.getNome());
                    existingEmpresa.setCnpj(empresaInputDTO.getCnpj());
                    return convertToOutputDTO(empresaRepository.save(existingEmpresa));
                });
    }
    
    public void delete(Integer id) {
        empresaRepository.deleteById(id);
    }
    
    private EmpresaOutputDTO convertToOutputDTO(Empresa empresa) {
        return modelMapper.map(empresa, EmpresaOutputDTO.class);
    }
    
    private Empresa convertToEntity(EmpresaInputDTO empresaInputDTO) {
        return modelMapper.map(empresaInputDTO, Empresa.class);
    }
}