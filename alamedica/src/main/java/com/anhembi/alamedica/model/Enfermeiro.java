package com.anhembi.alamedica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "enfermeiros")
@Getter
@Setter
public class Enfermeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_ala_medica")
    @JsonIgnoreProperties("enfermeiros")
    private Alamedica alaMedica;

}
