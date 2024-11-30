package com.anhembi.alamedica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.PacienteRepository;
import com.anhembi.alamedica.repository.QuartoRepository;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    @InjectMocks
    private PacienteService service;

    @Mock
    private PacienteRepository repo;

    @Mock
    private QuartoRepository quarto_repo;

    private Paciente paciente1;
    private Paciente paciente2;
    private Paciente pacienteSemId;
    private Quarto quartoDisponivel;
    private Quarto quartoOcupado;

    @BeforeEach
    public void setUp() {
        paciente1 = new Paciente();
        paciente2 = new Paciente();

        quartoDisponivel = new Quarto("101", null, null);
        
        quartoOcupado = new Quarto("102", new Paciente() , null);
        
        pacienteSemId.setId(null);
    }

    //Teste getAllPacientes()
    // @Test
    // @DisplayName("Deve retornar todos os pacientes cadastrados")
    // public void retornarTodosPacientes_RetornaTodosPacientes() {

    //     when(repo.findAll()).thenReturn(List.of(paciente1, paciente2));

    //     List<Paciente> result = service.getAllPacientes();

    //     assertEquals(2, result.size());

    //     // Verifica se o conteúdo está correto
    //     assertEquals(paciente1, result.get(0));
    //     assertEquals(paciente2, result.get(1));
    // }

    //Teste getById()
    // @Test
    // @DisplayName("Deve retornar o paciente especificado pelo Id")
    // public void retornarEnfermeiroPorId_RetornaEnfermeiroPorId() {

    //     when(repo.findById(1)).thenReturn(Optional.of(paciente1));

    //     Optional<Paciente> response = service.getPacienteById(1);

    //     assertEquals(paciente1, response.get());
    // }

    //Teste Post
    //teste sucesso cadastrar paciente
    // @Test
    // @DisplayName("Deve cadastrar um paciente com sucesso")
    // public void cadastrarPaciente_ComDadosValidos_RetornaPaciente() {

    //     when(repo.save(paciente1)).thenReturn(paciente1);

    //     Optional<Paciente> result = service.cadastrarPaciente(paciente1);

    //     assertEquals(paciente1, result.get());
    // }
}
