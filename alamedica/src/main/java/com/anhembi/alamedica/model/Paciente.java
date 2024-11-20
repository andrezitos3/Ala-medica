package com.anhembi.alamedica.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pacientes")
@Getter
@Setter

public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    private boolean internado;

    @NotNull(message = "A data de nascimento é obrigatória.")
    private LocalDate data_de_nascimento;

    @NotBlank(message = "A enfermidade é obrigatória.")
    private String enfermidade;

    private String alergia;

    @OneToOne
    @JoinColumn(name = "id_quarto", referencedColumnName = "id")
    @JsonIgnoreProperties("paciente")
    private Quarto quarto;

    public boolean getInternado(){
        return this.internado;
    }
}
