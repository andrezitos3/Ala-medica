package com.anhembi.alamedica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.repository.AlamedicaRepository;

@Service
public class AlamedicaService {

    @Autowired
    private AlamedicaRepository repo;

    // get 
    public List<Alamedica> getAlasMedicas(){
        return (List<Alamedica>) repo.findAll();
    }

    // post
    public Optional<Alamedica> criarAlaMedica(Alamedica ala_medica){
        if (ala_medica.getId() != null){
            return Optional.empty();
        }
        return Optional.of(repo.save(ala_medica));
    }
}
