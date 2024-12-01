package com.anhembi.alamedica.controller;

import java.util.ArrayList;
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

import com.anhembi.alamedica.dto.AlamedicaDTO;
import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.EnfermeiroRepository;
import com.anhembi.alamedica.repository.QuartoRepository;
import com.anhembi.alamedica.service.AlamedicaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/alasmedicas")
public class AlamedicaController {

    @Autowired
    private AlamedicaService service;

    @Autowired
    private QuartoRepository quarto_repo;

    @Autowired
    private EnfermeiroRepository enfermeiro_repo;

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
    public ResponseEntity<Alamedica> create(@RequestBody @Valid AlamedicaDTO ala_medica_Dto){

        // inicializar lista p quarto e enfermeiro
        List<Quarto> quartos = new ArrayList<>();
        List<Enfermeiro> enfermeiros = new ArrayList<>();

        // valida e obtém os objetos dos quartos se tiverem ids
        if (ala_medica_Dto.getQuartos_ids() != null && !ala_medica_Dto.getQuartos_ids().isEmpty()){

            for (Integer quarto_id : ala_medica_Dto.getQuartos_ids()) {
                
                Optional<Quarto> quarto_optional = quarto_repo.findById(quarto_id);

                if (quarto_optional.isEmpty()){
                    return ResponseEntity.notFound().build(); // quarto não encontrado
                }
                quartos.add(quarto_optional.get());
            }
        }

        // valida e obtém os objetos dos enfermeiros se tiverem ids
        if (ala_medica_Dto.getEnfermeiros_ids() != null && !ala_medica_Dto.getEnfermeiros_ids().isEmpty()){

            for (Integer enfermeiro_id : ala_medica_Dto.getEnfermeiros_ids()){
                Optional<Enfermeiro> enfermeiro_optional = enfermeiro_repo.findById(enfermeiro_id);

                if (enfermeiro_optional.isEmpty()){

                    return ResponseEntity.notFound().build(); // enfermeiro não encontrado
                }

                enfermeiros.add(enfermeiro_optional.get());
            }
        }

        // convertendo o DTO em uma ala médica
        Alamedica ala_medica = ala_medica_Dto.toaAlamedica(quartos, enfermeiros);


        Optional<Alamedica> optional_ala_medica = service.criarAlaMedica(ala_medica);

        if (optional_ala_medica.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optional_ala_medica.get(), HttpStatus.CREATED);
    }

    // PUT: Atualiza uma ala médica pelo ID
    @PutMapping("/{id}")
    public ResponseEntity<Alamedica> update(@PathVariable Integer id, @RequestBody @Valid AlamedicaDTO ala_medica_Dto) {

        List<Quarto> novos_quartos = new ArrayList<>();
        List<Enfermeiro> novos_enfermeiros = new ArrayList<>();

        // Valida e obtém os objetos dos quartos, se IDs forem fornecidos
        if (ala_medica_Dto.getQuartos_ids() != null && !ala_medica_Dto.getQuartos_ids().isEmpty()) {
            for (Integer quartoId : ala_medica_Dto.getQuartos_ids()) {
                Optional<Quarto> quarto_optional = quarto_repo.findById(quartoId);
                if (quarto_optional.isEmpty()) {
                    return ResponseEntity.notFound().build(); // Quarto não encontrado
                }
                novos_quartos.add(quarto_optional.get());
            }
        }

        // Valida e obtém os objetos dos enfermeiros, se IDs forem fornecidos
        if (ala_medica_Dto.getEnfermeiros_ids() != null && !ala_medica_Dto.getEnfermeiros_ids().isEmpty()) {
            for (Integer enfermeiroId : ala_medica_Dto.getEnfermeiros_ids()) {
                Optional<Enfermeiro> enfermeiro_optional = enfermeiro_repo.findById(enfermeiroId);
                if (enfermeiro_optional.isEmpty()) {
                    return ResponseEntity.notFound().build(); // Enfermeiro não encontrado
                }
                novos_enfermeiros.add(enfermeiro_optional.get());
            }
        }

        // convertendo o dto
        Alamedica ala_medica_atualizada = ala_medica_Dto.toaAlamedica(novos_quartos, novos_enfermeiros);

        Optional<Alamedica> optionalAlaMedica = service.atualizarAlaMedica(id, ala_medica_atualizada);

        if (optionalAlaMedica.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(optionalAlaMedica.get(), HttpStatus.OK);
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
