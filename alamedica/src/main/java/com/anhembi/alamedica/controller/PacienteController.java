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

import com.anhembi.alamedica.dto.PacienteDTO;
import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.service.PacienteService;

import jakarta.validation.Valid;

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

    // GET: Retorna um paciente específico pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getById(@PathVariable Integer id) {
        Optional<Paciente> optionalPaciente = service.getPacienteById(id);
        return optionalPaciente.map(ResponseEntity::ok)
                               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Paciente> register(@RequestBody @Valid PacienteDTO pacienteDto){

        Paciente paciente = pacienteDto.toPaciente();

        Optional<Paciente> optional_paciente = service.cadastrarPaciente(paciente);

        if (optional_paciente.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optional_paciente.get(), HttpStatus.CREATED);
    }
    


    // PUT: Atualiza um paciente existente
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> update(@PathVariable Integer id, @RequestBody @Valid PacienteDTO pacienteDto) {

        Paciente pacienteAtualizado = pacienteDto.toPaciente();

        pacienteAtualizado.setId(id);

        Optional<Paciente> optionalPaciente = service.atualizarPaciente(id, pacienteAtualizado);
        return optionalPaciente.map(ResponseEntity::ok)
                               .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){

        boolean deletado = service.deletarPaciente(id);

        if (deletado){
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
