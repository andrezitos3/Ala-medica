package com.anhembi.alamedica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.QuartoRepository;

@Service
public class QuartoService {

    @Autowired
    private QuartoRepository repo;

    // get
    public List<Quarto> getAllQuartos(){
        return (List<Quarto>) repo.findAll();
    }

    // post
    public Optional<Quarto> criarQuarto(Quarto quarto){
        if (quarto.getId() != null){
            return Optional.empty();
        }

        return Optional.of(repo.save(quarto));
    }    
}
