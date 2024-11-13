package com.anhembi.alamedica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quartos")
@Getter
@Setter
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O número do quarto é obrigatório")
    private String numero;

    @OneToOne
    @JoinColumn(name = "id_paciente", referencedColumnName = "id")
    @JsonIgnoreProperties("quarto")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_ala_medica")
    @JsonIgnoreProperties("quartos")
    @NotNull(message = "A ala médica é obrigatória.")
    private Alamedica alaMedica;
    
}
