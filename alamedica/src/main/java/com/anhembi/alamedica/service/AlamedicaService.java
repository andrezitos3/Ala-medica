package com.anhembi.alamedica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.repository.AlamedicaRepository;

@Service
public class AlamedicaService {

    @Autowired
    private AlamedicaRepository repo;

    public List<Alamedica> getAlasMedicas(){
        return (List<Alamedica>) repo.findAll();
    }
}
