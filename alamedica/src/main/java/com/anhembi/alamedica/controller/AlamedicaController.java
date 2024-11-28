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

    @GetMapping("/{id}")
    public ResponseEntity<Alamedica> getById(@PathVariable Integer id) {
        Optional<Alamedica> alaMedica = service.getAlaMedica(id);
        return alaMedica.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Alamedica> create(@RequestBody Alamedica ala_medica){
        Optional<Alamedica> optional_ala_medica = service.criarAlaMedica(ala_medica);

        if (optional_ala_medica.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optional_ala_medica.get(), HttpStatus.CREATED);
    }

    // PUT: Atualiza uma ala médica pelo ID
    @PutMapping("/{id}")
    public ResponseEntity<Alamedica> update(@PathVariable Integer id, @RequestBody Alamedica alaAtualizada) {
        Optional<Alamedica> optionalAlaMedica = service.atualizarAlaMedica(id, alaAtualizada);
        return optionalAlaMedica.map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = service.deletarAlaMedica(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
