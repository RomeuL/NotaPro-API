package com.notapro.api.dto.nota;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.notapro.api.dto.BaseDTO;
import com.notapro.api.model.enums.StatusNota;
import com.notapro.api.model.enums.TipoPagamento;

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
public class NotaBaseDTO extends BaseDTO {
    private Integer id;
    private String numeroNota;
    private String descricao;
    private LocalDate dataEmissao;
    private LocalDate dataVencimento;
    private BigDecimal valor;
    private TipoPagamento tipoPagamento;
    private String numeroBoleto;
    private StatusNota status;
}