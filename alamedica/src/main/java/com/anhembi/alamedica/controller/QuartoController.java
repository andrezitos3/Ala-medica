package com.anhembi.alamedica.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/{id}")
    public ResponseEntity<Quarto> getById(@PathVariable Integer id){

        Optional<Quarto> quarto = service.getQuartoById(id);

        return quarto.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Quarto> create(@RequestBody Quarto quarto){

        Optional<Quarto> optional_quarto = service.criarQuarto(quarto);

        if (optional_quarto.isEmpty()){
           return ResponseEntity.badRequest().build(); 
        }

        return new ResponseEntity<>(optional_quarto.get(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quarto> update(@PathVariable Integer id, @RequestBody Quarto quarto_atualizado){

        Optional<Quarto> optional_quarto = service.atualizarQuarto(id, quarto_atualizado);

        if (optional_quarto.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optional_quarto.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Quarto> delete(@PathVariable Integer id){

        boolean deleted = service.deletarQuarto(id);

        if (deleted){

            return ResponseEntity.noContent().build();

        } else {
            
            return ResponseEntity.notFound().build();
        }
    }
}
