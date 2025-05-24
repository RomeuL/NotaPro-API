package com.notapro.api.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticasNotaDTO {
    private long totalNotas;
    private long notasPendentes;
    private long notasPagas;
    private BigDecimal valorTotalNotas;
    private BigDecimal valorTotalPendente;
    private BigDecimal valorTotalPago;
}