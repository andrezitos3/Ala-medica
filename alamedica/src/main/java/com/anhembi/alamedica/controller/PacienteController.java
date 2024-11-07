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

import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.service.PacienteService;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @GetMapping
    public ResponseEntity<List<Paciente>> getAll(){
        
        List<Paciente> pacientes = service.getAllPacientes();

        return ResponseEntity.ok(pacientes);

    }

    @PostMapping
    public ResponseEntity<Paciente> register(@RequestBody Paciente paciente){

        Optional<Paciente> optional_paciente = service.cadastrarPaciente(paciente);

        if (optional_paciente.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optional_paciente.get(), HttpStatus.CREATED);
    }
}
