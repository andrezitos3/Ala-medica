package com.anhembi.alamedica.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.service.EnfermeiroService;

@ExtendWith(MockitoExtension.class)
public class EnfermeiroControllerTest {

    @InjectMocks
    private EnfermeiroController enfermeiroController;

    @Mock
    private EnfermeiroService enfermeiroService;

    @Test
    @DisplayName("Deve retornar todos os enfermeiros")
    public void RetornarTodosEnfermeiros_retornaTodosEnfermeiros() {

        Alamedica mockAlaMedica = mock(Alamedica.class);

        Enfermeiro enfermeiro1 = new Enfermeiro(1, "Douglas", mockAlaMedica);
        Enfermeiro enfermeiro2 = new Enfermeiro(2, "Leonardo", mockAlaMedica);
        
        when(enfermeiroService.getAllEnfermeiros()).thenReturn(List.of(enfermeiro1, enfermeiro2));

        ResponseEntity<List<Enfermeiro>> response = enfermeiroController.getAll();

        assertEquals(2, response.getBody().size());
    }

}
