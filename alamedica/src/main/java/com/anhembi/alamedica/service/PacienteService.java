package com.anhembi.alamedica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.PacienteRepository;
import com.anhembi.alamedica.repository.QuartoRepository;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repo;

    @Autowired
    private QuartoRepository quarto_repo;

    // get
    public List<Paciente> getAllPacientes(){
        return (List<Paciente>) repo.findAll();
    }

    // get por id
    public Optional<Paciente> getPacienteById(Integer id) {
        return repo.findById(id);
    }

    // post
    public Optional<Paciente> cadastrarPaciente(Paciente paciente){
        if (paciente.getId() != null){
            return Optional.empty();
        }

        // Regra: O paciente deve iniciar com alta = false
        paciente.setAlta(false);

        // Regra: Verificar se o quarto está disponível
        if (paciente.getQuarto() != null) {
            Optional<Quarto> quartoOptional = quarto_repo.findById(paciente.getQuarto().getId());
            if (quartoOptional.isPresent()) {
                Quarto quarto = quartoOptional.get();
                if (quarto.getPaciente() != null) {
                    // Retornar vazio se o quarto já está ocupado por outro paciente
                    return Optional.empty();
                }
                // Associar o paciente ao quarto
                quarto.setPaciente(paciente);
                paciente.setQuarto(quarto);
            } else {
                // Retornar vazio se o quarto não existe
                return Optional.empty();
            }
        }

        return Optional.of(repo.save(paciente));
    }
}
