package com.notapro.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    private Integer id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "^\\d{14}$", message = "CNPJ deve conter 14 dígitos numéricos")
    private String cnpj;
}