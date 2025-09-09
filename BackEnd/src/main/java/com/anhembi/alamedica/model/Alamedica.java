package com.anhembi.alamedica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "O andar é obrigatório.")
    private int andar;

    @OneToMany(mappedBy = "alaMedica")
    @JsonIgnoreProperties("alaMedica")
    private List<Enfermeiro> enfermeiros;

    @OneToMany(mappedBy = "alaMedica")
    @JsonIgnoreProperties("alaMedica")
    private List<Quarto> quartos;

    public Alamedica() {}

    public Alamedica(int andar, List<Enfermeiro> enfermeiros, List<Quarto> quartos){
        this.andar = andar;
        this.enfermeiros = enfermeiros;
        this.quartos = quartos;
    }
}
