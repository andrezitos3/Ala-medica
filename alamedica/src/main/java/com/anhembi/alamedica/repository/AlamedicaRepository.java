package com.anhembi.alamedica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anhembi.alamedica.model.Alamedica;

public interface AlamedicaRepository extends JpaRepository<Alamedica, Integer> {
    
}
