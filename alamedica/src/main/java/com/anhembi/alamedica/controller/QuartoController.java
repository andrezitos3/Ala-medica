package com.anhembi.alamedica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.service.QuartoService;

@RestController
@RequestMapping("/quartos")
public class QuartoController {

    @Autowired
    private QuartoService service;

    @GetMapping
    public ResponseEntity <List<Quarto>> getAll(){

        List<Quarto> quartos = service.getAllQuartos();

        return ResponseEntity.ok(quartos);
    }
}
