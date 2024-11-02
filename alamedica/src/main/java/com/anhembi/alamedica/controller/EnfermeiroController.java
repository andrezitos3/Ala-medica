package com.anhembi.alamedica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.repository.EnfermeiroRepository;

@RestController
@RequestMapping("/enfermeiros")
public class EnfermeiroController {

    @Autowired
    private EnfermeiroRepository repo;

    @SuppressWarnings("rawtypes")
    @GetMapping
    public ResponseEntity getAllEnfermeiros(){
        
        List<Enfermeiro> allEnfermeiros = repo.findAll();

        return ResponseEntity.ok(allEnfermeiros);
    }

}
