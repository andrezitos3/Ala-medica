package com.anhembi.alamedica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.repository.PacienteRepository;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repo;

    public List<Paciente> getAllPacientes(){
        return (List<Paciente>) repo.findAll();
    }
}
