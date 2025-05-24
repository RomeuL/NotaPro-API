package com.notapro.api.dto.empresa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
public class EmpresaInputDTO extends EmpresaBaseDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Override
    public String getNome() {
        return super.getNome();
    }
    
    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "^\\d{14}$", message = "CNPJ deve conter 14 dígitos numéricos")
    @Override
    public String getCnpj() {
        return super.getCnpj();
    }
}