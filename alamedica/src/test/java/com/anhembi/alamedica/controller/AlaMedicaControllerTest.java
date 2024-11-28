package com.anhembi.alamedica.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.service.AlamedicaService;

@ExtendWith(MockitoExtension.class)
public class AlaMedicaControllerTest {
    
    @InjectMocks
    private AlamedicaController alamedicaController;

    @Mock
    private AlamedicaService alamedicaService;

    @Test
    @DisplayName("Deve retornar todas as alas")
    public void RetornarTodasAlas_retornaTodasAsAlas() {

        Alamedica ala1 = new Alamedica(1, 2, Collections.emptyList(), Collections.emptyList());
        Alamedica ala2 = new Alamedica(2, 3, Collections.emptyList(), Collections.emptyList());
        
        when(alamedicaService.getAlasMedicas()).thenReturn(List.of(ala1, ala2));

        ResponseEntity<List<Alamedica>> response = alamedicaController.getAll();

        assertEquals(2, response.getBody().size());
    }

    @Test
    @DisplayName("Deve retornar a ala especificada pelo ID")
    public void RetornarAlaPeloId_IdValido_RetornaAlaPeloId() {
        Alamedica ala1 = new Alamedica(1, 2, Collections.emptyList(), Collections.emptyList());

        when(alamedicaService.getAlaMedica(1)).thenReturn(Optional.of(ala1));

        ResponseEntity<Alamedica> response = alamedicaController.getById(1);

        assertEquals(ala1, response.getBody());
    }

    @Test
    @DisplayName("Deve verificaro caso de sucesso da função delete")
    public void DeletarAla_AlaValida_RetornaNoContent() {

        when(alamedicaService.deletarAlaMedica(1)).thenReturn(true);

        ResponseEntity<Void> response = alamedicaController.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificaro caso de falha da função delete")
    public void DeletarAla_AlaInvalida_RetornaBadRequest() {

        when(alamedicaService.deletarAlaMedica(1)).thenReturn(false);

        ResponseEntity<Void> response = alamedicaController.delete(1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}