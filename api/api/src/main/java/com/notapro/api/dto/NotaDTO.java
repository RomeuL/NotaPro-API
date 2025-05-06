package com.notapro.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.notapro.api.model.enums.StatusNota;
import com.notapro.api.model.enums.TipoPagamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaDTO {
    
    private Integer id;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    
    @NotNull(message = "ID da empresa é obrigatório")
    private Integer empresaId;
    
    @NotNull(message = "Data de emissão é obrigatória")
    private LocalDate dataEmissao;
    
    @NotNull(message = "Data de vencimento é obrigatória")
    private LocalDate dataVencimento;
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;
    
    @NotNull(message = "Tipo de pagamento é obrigatório")
    private TipoPagamento tipoPagamento;
    
    private String numeroBoleto;
    
    @NotNull(message = "Status é obrigatório")
    private StatusNota status;
}