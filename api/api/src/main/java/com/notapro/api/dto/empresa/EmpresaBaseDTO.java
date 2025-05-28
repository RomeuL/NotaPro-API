package com.notapro.api.dto.empresa;

import com.notapro.api.dto.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmpresaBaseDTO extends BaseDTO {
    private Integer id;
    private String nome;
    private String cnpj;
}