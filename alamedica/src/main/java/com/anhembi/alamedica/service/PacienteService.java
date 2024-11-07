package com.anhembi.alamedica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.repository.PacienteRepository;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repo;

    // get
    public List<Paciente> getAllPacientes(){
        return (List<Paciente>) repo.findAll();
    }

    // post
    public Optional<Paciente> cadastrarPaciente(Paciente paciente){
        if (paciente.getId() != null){
            return Optional.empty();
        }

        return Optional.of(repo.save(paciente));
    }
}
