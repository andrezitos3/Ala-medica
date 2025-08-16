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

import com.anhembi.alamedica.dto.QuartoDTO;
import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.AlamedicaRepository;
import com.anhembi.alamedica.repository.PacienteRepository;
import com.anhembi.alamedica.service.QuartoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/quartos")
public class QuartoController {

    @Autowired
    private QuartoService service;

    @Autowired
    private AlamedicaRepository alamedica_repo;

    @Autowired
    private PacienteRepository paciente_repo;

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
    public ResponseEntity<Quarto> create(@RequestBody @Valid QuartoDTO quartoDto){

        Alamedica ala_medica = null;
        Paciente paciente = null;

        if (quartoDto.getId_alamedica() != null){
            Optional<Alamedica> alamedica_optional = alamedica_repo.findById(quartoDto.getId_alamedica());

            if (alamedica_optional.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            ala_medica = alamedica_optional.get();
        }

        if(quartoDto.getId_paciente() != null){
            Optional<Paciente> paciente_optional = paciente_repo.findById(quartoDto.getId_paciente());

            if (paciente_optional.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            paciente = paciente_optional.get();
        }

        Quarto quarto = quartoDto.toQuarto(paciente, ala_medica);

        Optional<Quarto> optional_quarto = service.criarQuarto(quarto);

        if (optional_quarto.isEmpty()){
           return ResponseEntity.badRequest().build(); 
        }

        return new ResponseEntity<>(optional_quarto.get(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quarto> update(@PathVariable Integer id, @RequestBody @Valid QuartoDTO quartoDto){

        Paciente novo_paciente = null;
        Alamedica nova_ala_medica = null;

        if (quartoDto.getId_alamedica() != null){
            Optional<Alamedica> alamedica_optional = alamedica_repo.findById(quartoDto.getId_alamedica());

            if (alamedica_optional.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            nova_ala_medica = alamedica_optional.get();
        }

        if (quartoDto.getId_paciente() != null){
            Optional<Paciente> paciente_optional = paciente_repo.findById(quartoDto.getId_paciente());

            if (paciente_optional.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            novo_paciente = paciente_optional.get();
        }

        // convertendo o DTO para quarto

        Quarto quarto_atualizado = quartoDto.toQuarto(novo_paciente, nova_ala_medica);


        Optional<Quarto> optional_quarto = service.atualizarQuarto(id, quarto_atualizado);

        if (optional_quarto.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optional_quarto.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){

        boolean deleted = service.deletarQuarto(id);

        if (deleted){

            return ResponseEntity.noContent().build();

        } else {
            
            return ResponseEntity.notFound().build();
        }
    }
}
