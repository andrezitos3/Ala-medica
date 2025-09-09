package com.anhembi.alamedica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anhembi.alamedica.model.Enfermeiro;

public interface EnfermeiroRepository extends JpaRepository <Enfermeiro, Integer> {

    boolean existsByAlaMedicaId(Integer alaMedicaId);
}
