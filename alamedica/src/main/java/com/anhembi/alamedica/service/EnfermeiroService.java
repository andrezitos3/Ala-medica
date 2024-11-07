package com.anhembi.alamedica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.repository.EnfermeiroRepository;

@Service
public class EnfermeiroService {

    @Autowired
    private EnfermeiroRepository repo;

    // get
    public List<Enfermeiro> getAllEnfermeiros(){
        return (List<Enfermeiro>) repo.findAll();
    }

    // post
    public Optional<Enfermeiro> cadastrarEnfermeiro(Enfermeiro enfermeiro){
        if (enfermeiro.getId() != null){
            return Optional.empty();
        }

        return Optional.of(repo.save(enfermeiro));
    }

}
