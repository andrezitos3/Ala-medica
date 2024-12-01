package com.anhembi.alamedica.dto;

import java.util.List;

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.model.Quarto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlamedicaDTO {

    @NotNull(message = "O andar é obrigatório.")
    private int andar;

    private List<Integer> quartos_ids;

    private List<Integer> enfermeiros_ids;

    public Alamedica toaAlamedica(List<Quarto> quartos, List<Enfermeiro> enfermeiros){
        
        Alamedica ala_medica = new Alamedica();

        ala_medica.setAndar(this.andar);
        ala_medica.setQuartos(quartos);
        ala_medica.setEnfermeiros(enfermeiros);

        return ala_medica;
    }
}
