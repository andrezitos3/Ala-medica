package com.anhembi.alamedica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anhembi.alamedica.model.Quarto;

public interface QuartoRepository extends JpaRepository<Quarto, Integer>{

    boolean existsByNumero(String numero); 
    boolean existsByAlaMedicaId(Integer alaMedicaId);
}
