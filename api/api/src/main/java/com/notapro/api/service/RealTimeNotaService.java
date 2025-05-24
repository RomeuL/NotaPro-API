package com.notapro.api.service;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.notapro.api.model.enums.StatusNota;
import com.notapro.api.repository.NotaRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealTimeNotaService {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final NotaRepository notaRepository;

    private static final Logger logger = LoggerFactory.getLogger(RealTimeNotaService.class);

    private long totalNotas;
    private long notasPendentes;
    private long notasPagas;
    private BigDecimal valorTotalNotas = BigDecimal.ZERO;
    private BigDecimal valorTotalPendente = BigDecimal.ZERO;
    private BigDecimal valorTotalPago = BigDecimal.ZERO;

    @PostConstruct
    public void startThread() {
        executorService.submit(() -> {
            while (true) {
                try {
                    atualizarEstatisticas();
                    TimeUnit.SECONDS.sleep(43200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("Erro ao atualizar estatísticas: " + e.getMessage(), e);
                }
            }
        });
    }

    private void atualizarEstatisticas() {
        totalNotas = notaRepository.count();
        notasPendentes = notaRepository.countByStatus(StatusNota.PENDENTE);
        notasPagas = notaRepository.countByStatus(StatusNota.PAGO);
        
        valorTotalNotas = notaRepository.findAll().stream()
                .map(nota -> nota.getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        valorTotalPendente = notaRepository.findByStatus(StatusNota.PENDENTE).stream()
                .map(nota -> nota.getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        valorTotalPago = notaRepository.findByStatus(StatusNota.PAGO).stream()
                .map(nota -> nota.getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        logger.info("Estatísticas atualizadas: Total Notas={}, Pendentes={}, Pagas={}, Valor Total={}",
                totalNotas, notasPendentes, notasPagas, valorTotalNotas);
    }

    public void atualizarEstatisticasAgora() {
        try {
            atualizarEstatisticas();
        } catch (Exception e) {
            logger.error("Erro ao atualizar estatísticas sob demanda: " + e.getMessage(), e);
        }
    }

    public long getTotalNotas() { return totalNotas; }
    public long getNotasPendentes() { return notasPendentes; }
    public long getNotasPagas() { return notasPagas; }
    public BigDecimal getValorTotalNotas() { return valorTotalNotas; }
    public BigDecimal getValorTotalPendente() { return valorTotalPendente; }
    public BigDecimal getValorTotalPago() { return valorTotalPago; }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}