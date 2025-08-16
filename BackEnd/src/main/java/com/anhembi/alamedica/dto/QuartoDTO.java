package com.anhembi.alamedica.dto;


import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.model.Quarto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuartoDTO {

    @NotBlank(message = "O número do quarto é obrigatório.")
    private String numero;

    private Integer id_paciente; // id do paciente associado (opcional)

    @NotNull(message = "a ala médica é obrigatória.")
    private Integer id_alamedica; // o id da ala médica é obrigatório
    
    public Quarto toQuarto(Paciente paciente, Alamedica ala_medica){

        Quarto quarto = new Quarto();

        quarto.setNumero(numero);
        quarto.setPaciente(paciente);
        quarto.setAlaMedica(ala_medica);
        return quarto;
    }

}
