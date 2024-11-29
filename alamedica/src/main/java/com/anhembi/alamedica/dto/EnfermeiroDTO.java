package com.anhembi.alamedica.dto;

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Enfermeiro;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnfermeiroDTO {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    private Integer id_alamedica;


    public Enfermeiro toEnfermeiro(Alamedica ala_medica){

        Enfermeiro enfermeiro = new Enfermeiro();

        enfermeiro.setNome(nome);
        enfermeiro.setAlaMedica(ala_medica);

        return enfermeiro;
    }
}
