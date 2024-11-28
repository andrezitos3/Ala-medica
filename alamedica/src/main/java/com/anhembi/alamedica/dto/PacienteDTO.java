package com.anhembi.alamedica.dto;

import java.time.LocalDate;

import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.model.Quarto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PacienteDTO {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    private boolean internado;

    @NotNull(message = "A data de nascimento é obrigatória.")
    private LocalDate data_de_nascimento;

    @NotBlank(message = "A enfermidade é obrigatória.")
    private String enfermidade;

    private String alergia;

    private Integer quarto_id;

    public Paciente toPaciente(Quarto quarto) {
    return new Paciente(
        nome,
        data_de_nascimento,
        enfermidade,
        alergia,
        quarto
    );
    }
}
