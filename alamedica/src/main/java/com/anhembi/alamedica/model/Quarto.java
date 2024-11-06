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

    private String numero;

    @OneToOne
    @JoinColumn(name = "id_paciente", referencedColumnName = "id")
    @JsonIgnoreProperties("quarto")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_ala_medica")
    @JsonIgnoreProperties("quartos")
    private Alamedica alaMedica;
    
}
