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

import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.service.EnfermeiroService;

@ExtendWith(MockitoExtension.class)
public class EnfermeiroControllerTest {

    @InjectMocks
    private EnfermeiroController controller;

    @Mock
    private EnfermeiroService service;

    private Enfermeiro enfermeiro1;
    private Enfermeiro enfermeiro2;

    @BeforeEach
    public void setUp() {
        enfermeiro1 = new Enfermeiro(1, "Douglas", null);
        enfermeiro2 = new Enfermeiro(2, "Leonardo", null);
    }

    //Testes getAll
    @Test
    @DisplayName("Deve retornar todos os enfermeiros")
    public void RetornarTodosEnfermeiros_retornaTodosEnfermeiros() {

        when(service.getAllEnfermeiros()).thenReturn(List.of(enfermeiro1, enfermeiro2));

        ResponseEntity<List<Enfermeiro>> response = controller.getAll();

        assertEquals(2, response.getBody().size());
    }

    //Testes getById
    @Test
    @DisplayName("Deve retornar o enfermeiro especificado pelo ID")
    public void RetornarEnfermeiroPeloId_IdValido_RetornaEnfermeiroPeloId() {

        when(service.getEnfermeiroPorId(1)).thenReturn(Optional.of(enfermeiro1));

        ResponseEntity<Enfermeiro> response = controller.getById(1);

        assertEquals(enfermeiro1, response.getBody());
    }

    @Test
    @DisplayName("Deve retornar NotFound se o enfermeiro não for encontrado")
    public void RetornarEnfermeiroPeloId_IdInvalido_RetornaNotFound() {

        when(service.getEnfermeiroPorId(999)).thenReturn(Optional.empty());

        ResponseEntity<Enfermeiro> response = controller.getById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //Testes delete
    @Test
    @DisplayName("Deve verificaro caso de sucesso da função delete")
    public void DeletarEnfermeiro_EnfermeiroValido_RetornaNoContent() {

        when(service.deletarEnfermeiro(1)).thenReturn(true);

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificaro caso de falha da função delete")
    public void DeletarEnfermeiro_EnfermeiroInvalido_RetornaBadRequest() {

        when(service.deletarEnfermeiro(999)).thenReturn(false);

        ResponseEntity<Void> response = controller.delete(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
