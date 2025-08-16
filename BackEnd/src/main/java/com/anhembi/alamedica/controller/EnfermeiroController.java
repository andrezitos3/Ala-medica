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

import com.anhembi.alamedica.dto.EnfermeiroDTO;
import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.repository.AlamedicaRepository;
import com.anhembi.alamedica.service.EnfermeiroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/enfermeiros")
public class EnfermeiroController {

    @Autowired
    private EnfermeiroService service;

    @Autowired
    private AlamedicaRepository alamedica_repo;

    @GetMapping
    public ResponseEntity<List<Enfermeiro>> getAll(){
        
        List<Enfermeiro> enfermeiros = service.getAllEnfermeiros();

        return ResponseEntity.ok(enfermeiros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enfermeiro> getById(@PathVariable Integer id) {
        Optional<Enfermeiro> enfermeiro = service.getEnfermeiroPorId(id);
        return enfermeiro.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Enfermeiro> register(@RequestBody @Valid EnfermeiroDTO enfermeiroDto){

        Alamedica ala_medica = null;

        if (enfermeiroDto.getId_alamedica() != null){

            Optional<Alamedica> ala_medica_optional = alamedica_repo.findById(enfermeiroDto.getId_alamedica());

            if (ala_medica_optional.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            ala_medica = ala_medica_optional.get();

        }

        Enfermeiro enfermeiro = enfermeiroDto.toEnfermeiro(ala_medica);

        Optional<Enfermeiro> optional_enfermeiro = service.cadastrarEnfermeiro(enfermeiro);

        if (optional_enfermeiro.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optional_enfermeiro.get(), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Enfermeiro> update(@PathVariable Integer id, @RequestBody @Valid EnfermeiroDTO enfermeiroDto) {

        Alamedica nova_ala_medica = null;

        if (enfermeiroDto.getId_alamedica() != null){

            Optional<Alamedica> ala_medica_optional = alamedica_repo.findById(enfermeiroDto.getId_alamedica());

            if (ala_medica_optional.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            nova_ala_medica = ala_medica_optional.get();
        }

        Enfermeiro enfermeiroAtualizado = enfermeiroDto.toEnfermeiro(nova_ala_medica);

        Optional<Enfermeiro> optionalEnfermeiro = service.atualizarEnfermeiro(id, enfermeiroAtualizado);

        if (optionalEnfermeiro.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optionalEnfermeiro.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = service.deletarEnfermeiro(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
