package com.anhembi.alamedica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    private String nome;

    private boolean alta = false;

    private String data_de_nascimento;

    private String enfermidade;

    private String alergia;

    @OneToOne
    @JoinColumn(name = "id_quarto", referencedColumnName = "id")
    @JsonIgnoreProperties("paciente")
    private Quarto quarto;

}
