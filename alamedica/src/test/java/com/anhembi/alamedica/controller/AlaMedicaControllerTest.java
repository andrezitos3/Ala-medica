package com.anhembi.alamedica.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
    private AlamedicaController controller;

    @Mock
    private AlamedicaService service;

    private Alamedica ala1;
    private Alamedica ala2;

    @BeforeEach
    public void setUp() {
        ala1 = new Alamedica(1, 2, Collections.emptyList(), Collections.emptyList());
        ala2 = new Alamedica(2, 3, Collections.emptyList(), Collections.emptyList());
    }

    //Testes getAll
    @Test
    @DisplayName("Deve retornar todas as alas")
    public void RetornarTodasAlas_retornaTodasAsAlas() {
        
        when(service.getAlasMedicas()).thenReturn(List.of(ala1, ala2));

        ResponseEntity<List<Alamedica>> response = controller.getAll();

        assertEquals(2, response.getBody().size());
    }

    //Testes getById
    @Test
    @DisplayName("Deve retornar a ala especificada pelo ID")
    public void RetornarAlaPeloId_IdValido_RetornaAlaPeloId() {

        when(service.getAlaMedica(1)).thenReturn(Optional.of(ala1));

        ResponseEntity<Alamedica> response = controller.getById(1);

        assertEquals(ala1, response.getBody());
    }

    @Test
    @DisplayName("Deve retornar NotFound se a Ala não for encontrada")
    public void RetornarAlaPeloId_IdInvalido_RetornaNotFound() {

        when(service.getAlaMedica(999)).thenReturn(Optional.empty());

        ResponseEntity<Alamedica> response = controller.getById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //Testes delete
    @Test
    @DisplayName("Deve verificaro caso de sucesso da função delete")
    public void DeletarAla_AlaValida_RetornaNoContent() {

        when(service.deletarAlaMedica(1)).thenReturn(true);

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificaro caso de falha da função delete")
    public void DeletarAla_AlaInvalida_RetornaBadRequest() {

        when(service.deletarAlaMedica(1)).thenReturn(false);

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}