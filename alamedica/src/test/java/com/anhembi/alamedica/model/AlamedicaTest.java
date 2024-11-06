package com.anhembi.alamedica.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AlamedicaTest {

    @Test
    void criarAlamedica_AlamedicaValida_AlamedicaCriada() {
        Enfermeiro anderson = new Enfermeiro();
        Enfermeiro jose = new Enfermeiro();
        Enfermeiro bobEsponja = new Enfermeiro();
        List<Enfermeiro> listaEnfermeiros = new ArrayList<>();

        listaEnfermeiros.add(anderson);
        listaEnfermeiros.add(jose);
        listaEnfermeiros.add(bobEsponja);

        Quarto um = new Quarto();
        Quarto dois = new Quarto();
        Quarto tres = new Quarto();
        List<Quarto> listaQuarto = new ArrayList<>();

        listaQuarto.add(um);
        listaQuarto.add(dois);
        listaQuarto.add(tres);

        Alamedica ala = new Alamedica(1, 0, listaEnfermeiros, listaQuarto); 

        assertThat(ala).isNotNull();
    }

}
