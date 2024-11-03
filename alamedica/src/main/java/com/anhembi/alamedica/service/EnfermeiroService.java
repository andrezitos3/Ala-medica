package com.anhembi.alamedica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.repository.EnfermeiroRepository;

@Service
public class EnfermeiroService {

    @Autowired
    private EnfermeiroRepository repo;

    public List<Enfermeiro> getAllEnfermeiros(){
        return (List<Enfermeiro>) repo.findAll();
    }

}
