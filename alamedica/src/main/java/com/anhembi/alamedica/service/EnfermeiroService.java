package com.anhembi.alamedica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.repository.AlamedicaRepository;
import com.anhembi.alamedica.repository.EnfermeiroRepository;

@Service
public class EnfermeiroService {

    @Autowired
    private EnfermeiroRepository repo;

    @Autowired
    private AlamedicaRepository alamedica_repo;

    // get todos os enfermeiros
    public List<Enfermeiro> getAllEnfermeiros(){
        return (List<Enfermeiro>) repo.findAll();
    }

    // get por id

    public Optional<Enfermeiro> getEnfermeiroPorId(Integer id){
        return repo.findById(id);
    }

    // post
    public Optional<Enfermeiro> cadastrarEnfermeiro(Enfermeiro enfermeiro){
        if (enfermeiro.getId() != null){
            return Optional.empty();
        }

        return Optional.of(repo.save(enfermeiro));
    }

}
