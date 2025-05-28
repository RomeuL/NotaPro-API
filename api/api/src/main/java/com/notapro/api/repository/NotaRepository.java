package com.notapro.api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notapro.api.model.Nota;
import com.notapro.api.model.enums.StatusNota;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {
    List<Nota> findByEmpresaId(Integer empresaId);
    List<Nota> findByStatus(StatusNota status);
    List<Nota> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim);
    long countByStatus(StatusNota status);
    Optional<Nota> findByNumeroNota(String numeroNota);
    boolean existsByNumeroNota(String numeroNota);
}