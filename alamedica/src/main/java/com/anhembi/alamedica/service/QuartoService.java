package com.anhembi.alamedica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.QuartoRepository;

@Service
public class QuartoService {

    @Autowired
    private QuartoRepository repo;

    public List<Quarto> getAllQuartos(){
        return (List<Quarto>) repo.findAll();
    }
    
}
