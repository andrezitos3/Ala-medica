package com.anhembi.alamedica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.repository.AlamedicaRepository;
import com.anhembi.alamedica.repository.EnfermeiroRepository;

@Service
public class EnfermeiroService {

    @Autowired
    private EnfermeiroRepository repo;

    @Autowired
    private AlamedicaRepository alamedica_repo;

    // get todos os enfermeiros
    public List<Enfermeiro> getAllEnfermeiros(){
        return (List<Enfermeiro>) repo.findAll();
    }

    // get por id

    public Optional<Enfermeiro> getEnfermeiroPorId(Integer id){
        return repo.findById(id);
    }

    // post
    public Optional<Enfermeiro> cadastrarEnfermeiro(Enfermeiro enfermeiro){
        if (enfermeiro.getId() != null){
            return Optional.empty();
        }

        // nome obrigatório
        if (enfermeiro.getNome() == null || enfermeiro.getNome().isBlank()){
            return Optional.empty();
        }

        // verifica se a ala medica existe
        Optional<Alamedica> alamedica_optional = alamedica_repo.findById(enfermeiro.getAlaMedica().getId());

        if (alamedica_optional.isEmpty()){
            return Optional.empty();
        }

        Alamedica ala_medica = alamedica_optional.get();

        // verificar se a ala médica já está cheia
        if (ala_medica.getEnfermeiros().size() >= 9){
            return Optional.empty(); // capacidade máxima atingida
        }

        // associar o enfermeiro à ala médica
        enfermeiro.setAlaMedica(ala_medica);

        return Optional.of(repo.save(enfermeiro));
    }

    // put - atualizar os dados do enfermeiro

    public Optional<Enfermeiro> atualizarEnfermeiro(Integer id, Enfermeiro enfermeiro_atualizado){

        Optional<Enfermeiro> enfermeiro_optional = repo.findById(id);

        if (enfermeiro_optional.isEmpty()){
            return Optional.empty();
        }

        Enfermeiro enfermeiro_existente = enfermeiro_optional.get();

        // atualiza o nome, se for fornecido
        if (enfermeiro_atualizado.getNome() != null && !enfermeiro_atualizado.getNome().isBlank()){

            enfermeiro_existente.setNome(enfermeiro_atualizado.getNome());

        }

        // atualiza a ala medica, se for fornecida

        if (enfermeiro_atualizado.getAlaMedica() != null){

            enfermeiro_existente.setAlaMedica(enfermeiro_atualizado.getAlaMedica());

        }

        return Optional.of(repo.save(enfermeiro_existente));


    }

    // delete - remove um enfermeiro por id

    public boolean deletarEnfermeiro(Integer id){

        Optional<Enfermeiro> enfermeiro_optional = repo.findById(id);

        if (enfermeiro_optional.isEmpty()){
            return false; // não permite excluir um enferemeiro inexistente
        }

        repo.deleteById(id);

        return true;

    }
}
