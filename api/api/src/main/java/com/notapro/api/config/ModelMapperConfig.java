package com.notapro.api.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.notapro.api.dto.empresa.EmpresaInputDTO;
import com.notapro.api.dto.empresa.EmpresaOutputDTO;
import com.notapro.api.dto.nota.NotaInputDTO;
import com.notapro.api.dto.nota.NotaOutputDTO;
import com.notapro.api.dto.user.UserInputDTO;
import com.notapro.api.dto.user.UserOutputDTO;
import com.notapro.api.model.Empresa;
import com.notapro.api.model.Nota;
import com.notapro.api.model.User;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        modelMapper.createTypeMap(User.class, UserOutputDTO.class);
        modelMapper.createTypeMap(UserInputDTO.class, User.class);
        modelMapper.createTypeMap(Empresa.class, EmpresaOutputDTO.class);
        modelMapper.createTypeMap(EmpresaInputDTO.class, Empresa.class);
        modelMapper.createTypeMap(Nota.class, NotaOutputDTO.class);
        modelMapper.createTypeMap(NotaInputDTO.class, Nota.class);
        
        return modelMapper;
    }
}