package com.notapro.api.dto.nota;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.notapro.api.model.enums.StatusNota;
import com.notapro.api.model.enums.TipoPagamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class NotaInputDTO extends NotaBaseDTO {
    
    @NotBlank(message = "Descrição é obrigatória")
    @Override
    public String getDescricao() {
        return super.getDescricao();
    }
    
    @NotNull(message = "ID da empresa é obrigatório")
    private Integer empresaId;
    
    @NotNull(message = "Data de emissão é obrigatória")
    @Override
    public LocalDate getDataEmissao() {
        return super.getDataEmissao();
    }
    
    @NotNull(message = "Data de vencimento é obrigatória")
    @Override
    public LocalDate getDataVencimento() {
        return super.getDataVencimento();
    }
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Override
    public BigDecimal getValor() {
        return super.getValor();
    }
    
    @NotNull(message = "Tipo de pagamento é obrigatório")
    @Override
    public TipoPagamento getTipoPagamento() {
        return super.getTipoPagamento();
    }
    
    @NotNull(message = "Status é obrigatório")
    @Override
    public StatusNota getStatus() {
        return super.getStatus();
    }
}