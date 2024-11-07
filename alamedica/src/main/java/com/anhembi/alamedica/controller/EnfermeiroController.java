package com.anhembi.alamedica.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<Enfermeiro>> getAll(){
        
        List<Enfermeiro> enfermeiros = service.getAllEnfermeiros();

        return ResponseEntity.ok(enfermeiros);
    }

    @PostMapping
    public ResponseEntity<Enfermeiro> register(@RequestBody Enfermeiro enfermeiro){

        Optional<Enfermeiro> optional_enfermeiro = service.cadastrarEnfermeiro(enfermeiro);

        if (optional_enfermeiro.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optional_enfermeiro.get(), HttpStatus.CREATED);

    }
}
