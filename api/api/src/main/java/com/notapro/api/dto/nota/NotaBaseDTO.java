package com.notapro.api.dto.nota;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.notapro.api.model.enums.StatusNota;
import com.notapro.api.model.enums.TipoPagamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class NotaBaseDTO {
    private Integer id;
    private String descricao;
    private LocalDate dataEmissao;
    private LocalDate dataVencimento;
    private BigDecimal valor;
    private TipoPagamento tipoPagamento;
    private String numeroBoleto;
    private StatusNota status;
}