package com.anhembi.alamedica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
