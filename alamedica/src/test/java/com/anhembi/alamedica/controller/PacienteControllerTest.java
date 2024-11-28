package com.anhembi.alamedica.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.service.PacienteService;

@ExtendWith(MockitoExtension.class)
public class PacienteControllerTest {

    @InjectMocks
    private PacienteController controller;

    @Mock
    private PacienteService service;

    private Paciente paciente1;
    private Paciente paciente2;

    @BeforeEach
    public void setUp() {
        paciente1 = new Paciente("Douglas", LocalDate.of(1990, 6, 17), "Dor no braço", "Nenhuma", null);
        paciente2 = new Paciente("Leandro", LocalDate.of(2002, 10, 12), "Enjoo", "Amendoim", null);
    }

    @Test
    @DisplayName("Deve retornar todos os pacientes")
    public void RetornarTodosPacientes_retornaTodosPacientes() {
        // Mockando o comportamento do serviço
        when(service.getAllPacientes()).thenReturn(List.of(paciente1, paciente2));

        // Chamando o controlador
        ResponseEntity<List<Paciente>> response = controller.getAll();

        // Verificando o resultado
        assertEquals(2, response.getBody().size());
    }

    @Test
    @DisplayName("Deve retornar o paciente especificado pelo ID")
    public void RetornarPacientePeloId_IdValido_RetornaPacientePeloId() {
        // Mockando o comportamento do serviço
        when(service.getPacienteById(1)).thenReturn(Optional.of(paciente1));

        // Chamando o controlador
        ResponseEntity<Paciente> response = controller.getById(1);

        // Verificando o resultado
        assertEquals(paciente1, response.getBody());
    }

    @Test
    @DisplayName("Deve retornar NotFound se o paciente não for encontrado")
    public void RetornarPacientePeloId_IdInvalido_RetornaNotFound() {
        // Mockando o comportamento do serviço
        when(service.getPacienteById(999)).thenReturn(Optional.empty());

        // Chamando o controlador
        ResponseEntity<Paciente> response = controller.getById(999);

        // Verificando o resultado
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificaro caso de sucesso da função delete")
    public void DeletarPaciente_PacienteValido_RetornaNoContent() {
        // Mockando o comportamento do serviço
        when(service.deletarPaciente(1)).thenReturn(true);

        // Chamando o controlador
        ResponseEntity<Void> response = controller.delete(1);

        // Verificando o resultado
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificaro caso de falha da função delete")
    public void DeletarPaciente_PacienteInvalido_RetornaNotFound() {
        // Mockando o comportamento do serviço
        when(service.deletarPaciente(999)).thenReturn(false);

        // Chamando o controlador
        ResponseEntity<Void> response = controller.delete(999);

        // Verificando o resultado
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
