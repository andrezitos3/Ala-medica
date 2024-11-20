package com.anhembi.alamedica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.repository.AlamedicaRepository;
import com.anhembi.alamedica.repository.EnfermeiroRepository;
import com.anhembi.alamedica.repository.QuartoRepository;

@Service
public class AlamedicaService {

    @Autowired
    private AlamedicaRepository repo;

    @Autowired 
    private QuartoRepository quarto_repo;

    @Autowired 
    private EnfermeiroRepository enfermeiro_repo;

    // get 
    public List<Alamedica> getAlasMedicas(){
        return (List<Alamedica>) repo.findAll();
    }

    // get por id
    public Optional<Alamedica> getAlaMedica(Integer id){

        return repo.findById(id);

    }


    // post
    public Optional<Alamedica> criarAlaMedica(Alamedica ala_medica){
        if (ala_medica.getId() != null){
            return Optional.empty();
        }

        // verifica se o número do andar é unico

        boolean andar_existente = repo.existsByAndar(ala_medica.getAndar());

        if (andar_existente){
            return Optional.empty();
        }

        return Optional.of(repo.save(ala_medica));
    }

    // put
    public Optional<Alamedica> atualizarAlaMedica(Integer id, Alamedica alamedica_atualizada){

        Optional<Alamedica> alamedica_optional = repo.findById(id);

        if (alamedica_optional.isEmpty()){
            return Optional.empty();
        }

        Alamedica alamedica_existente = alamedica_optional.get();

        // atualiza o andar se não causar duplicação
        if (alamedica_atualizada.getAndar() != alamedica_existente.getAndar()){
            boolean andar_existente = repo.existsByAndar(alamedica_atualizada.getAndar());

            if (andar_existente){
                return Optional.empty(); // sem duplicar os andares
            }

            alamedica_existente.setAndar(alamedica_atualizada.getAndar());
        }

        return Optional.of(repo.save(alamedica_existente));
    }

    // delete - remove uma ala medica

    public boolean deletarAlaMedica(Integer id){

        Optional<Alamedica> alamedica_optional = repo.findById(id);

        if (alamedica_optional.isEmpty()){
            return false; 
        }

        Alamedica ala_medica = alamedica_optional.get();

        // não deleta se tiver quartos ou enfermeiros associados a ala
        boolean temQuarto = quarto_repo.existsByAlaMedicaId(ala_medica.getId());
        boolean temEnfermeiro = enfermeiro_repo.existsByAlaMedicaId(ala_medica.getId());

        if (temQuarto || temEnfermeiro){
            return false;
        }

        repo.deleteById(id);
        return true;
    }
}
