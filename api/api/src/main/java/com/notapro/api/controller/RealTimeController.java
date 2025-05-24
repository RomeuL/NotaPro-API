package com.notapro.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notapro.api.dto.EstatisticasNotaDTO;
import com.notapro.api.service.RealTimeNotaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/estatisticas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class RealTimeController {

    private final RealTimeNotaService realTimeNotaService;

    @GetMapping("/notas")
    public ResponseEntity<EstatisticasNotaDTO> getEstatisticasNotas() {
        EstatisticasNotaDTO estatisticas = new EstatisticasNotaDTO(
                realTimeNotaService.getTotalNotas(),
                realTimeNotaService.getNotasPendentes(),
                realTimeNotaService.getNotasPagas(),
                realTimeNotaService.getValorTotalNotas(),
                realTimeNotaService.getValorTotalPendente(),
                realTimeNotaService.getValorTotalPago()
        );
        
        log.info("Estatísticas solicitadas: {}", estatisticas);
        return ResponseEntity.ok(estatisticas);
    }
}