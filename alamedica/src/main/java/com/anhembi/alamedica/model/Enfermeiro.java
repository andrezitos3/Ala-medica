package com.anhembi.alamedica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "enfermeiros")
@Getter
@Setter
@AllArgsConstructor
public class Enfermeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_ala_medica")
    @JsonIgnoreProperties("enfermeiros")
    private Alamedica alaMedica;

    public Enfermeiro(String nome, Alamedica ala_medica){
        this.nome = nome;
        this.alaMedica = ala_medica;
    }

    public Enfermeiro() {}
}
