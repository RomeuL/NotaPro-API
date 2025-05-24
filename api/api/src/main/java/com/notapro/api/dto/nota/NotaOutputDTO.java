package com.notapro.api.dto.nota;

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
public class NotaOutputDTO extends NotaBaseDTO {
    
    private Integer empresaId;
    private String empresaNome;
}