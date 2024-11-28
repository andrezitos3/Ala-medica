package com.anhembi.alamedica.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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

import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.service.QuartoService;

@ExtendWith(MockitoExtension.class)
public class QuartoControllerTest {

    @InjectMocks
    private QuartoController controller;

    @Mock
    private QuartoService service;

    private Quarto quarto1;
    private Quarto quarto2;
    
    @BeforeEach
    public void setUp() {
        quarto1 = new Quarto(1, "101", null, null);
        quarto2 = new Quarto(2, "102", null, null);
    }

    @Test
    @DisplayName("Deve retornar todos os quartos")
    public void RetornarTodosQuartos_retornaTodosQuartos() {

        when(service.getAllQuartos()).thenReturn(List.of(quarto1, quarto2));

        ResponseEntity<List<Quarto>> response = controller.getAll();

        assertEquals(2, response.getBody().size());
    }

    @Test
    @DisplayName("Deve retornar o quarto especificado pelo ID")
    public void RetornarQuartoPeloId_IdValido_RetornaQuartoPeloId() {

        when(service.getQuartoById(1)).thenReturn(Optional.of(quarto1));

        ResponseEntity<Quarto> response = controller.getById(1);

        assertEquals(quarto1, response.getBody());
    }

    @Test
    @DisplayName("Deve retornar NotFound se o quarto não for encontrado")
    public void RetornarEnfermeiroPeloId_IdInvalido_RetornaNotFound() {

        when(service.getQuartoById(999)).thenReturn(Optional.empty());

        ResponseEntity<Quarto> response = controller.getById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar NotFound se o quarto não for encontrado")
    public void RetornarQuartoPeloId_IdInvalido_RetornaNotFound() {

        when(service.getQuartoById(999)).thenReturn(Optional.empty());

        ResponseEntity<Quarto> response = controller.getById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificaro caso de sucesso da função delete")
    public void DeletarQuarto_QuartoValido_RetornaNoContent() {

        when(service.deletarQuarto(1)).thenReturn(true);

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificaro caso de falha da função delete")
    public void DeletarQuarto_QuartoInvalido_RetornaNoContent() {

        when(service.deletarQuarto(999)).thenReturn(false);

        ResponseEntity<Void> response = controller.delete(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
