package com.anhembi.alamedica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "alas_medicas")
@Getter
@Setter
@AllArgsConstructor
public class Alamedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int andar;

    @OneToMany(mappedBy = "alaMedica")
    @JsonIgnoreProperties("alaMedica")
    private List<Enfermeiro> enfermeiros;

    @OneToMany(mappedBy = "alaMedica")
    @JsonIgnoreProperties("alaMedica")
    private List<Quarto> quartos;

}
