package com.anhembi.alamedica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.service.EnfermeiroService;

@RestController
@RequestMapping("/enfermeiros")
public class EnfermeiroController {

    @Autowired
    private EnfermeiroService service;

    @GetMapping
    public ResponseEntity<List<Enfermeiro>> getAllEnfermeiros(){
        
        List<Enfermeiro> enfermeiros = service.getAllEnfermeiros();

        return ResponseEntity.ok(enfermeiros);
    }
}
