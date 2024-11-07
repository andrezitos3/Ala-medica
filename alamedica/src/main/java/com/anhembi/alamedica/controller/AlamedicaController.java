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

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.service.AlamedicaService;

@RestController
@RequestMapping("/alasmedicas")
public class AlamedicaController {

    @Autowired
    private AlamedicaService service;

    @GetMapping
    public ResponseEntity<List<Alamedica>> getAll(){
        
        List<Alamedica> alas_medicas = service.getAlasMedicas();

        return ResponseEntity.ok(alas_medicas);
    }

    @PostMapping
    public ResponseEntity<Alamedica> create(@RequestBody Alamedica ala_medica){
        Optional<Alamedica> optional_ala_medica = service.criarAlaMedica(ala_medica);

        if (optional_ala_medica.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optional_ala_medica.get(), HttpStatus.CREATED);
    }
}
