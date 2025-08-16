package com.anhembi.alamedica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.AlamedicaRepository;
import com.anhembi.alamedica.repository.PacienteRepository;
import com.anhembi.alamedica.repository.QuartoRepository;

@ExtendWith(MockitoExtension.class)
public class QuartoServiceTest {

    @InjectMocks
    private QuartoService service;

    @Mock
    private QuartoRepository repo;

    @Mock
    private PacienteRepository paciente_repo;

    @Mock
    private AlamedicaRepository alamedica_repo;

    private Alamedica alaMedica;
    private Alamedica alaMedica2;
    private Paciente paciente;
    private Quarto quartoAtualizado;
    private Quarto quarto1;
    private Quarto quarto2;

    @BeforeEach
    public void setUp() {
        alaMedica = new Alamedica();
        alaMedica2 = new Alamedica();

        paciente = new Paciente();

        quartoAtualizado = new Quarto();

        quarto1 = new Quarto();
        quarto2 = new Quarto();
    }

    // Teste getAllPacientes()
    @Test
    @DisplayName("Deve retornar todos os pacientes cadastrados")
    public void retornarTodosPacientes_RetornaTodosPacientes() {

        when(repo.findAll()).thenReturn(List.of(quarto1, quarto2));

        List<Quarto> result = service.getAllQuartos();

        assertEquals(2, result.size());

        // Verifica se o conteúdo está correto
        assertEquals(quarto1, result.get(0));
        assertEquals(quarto2, result.get(1));
    }

    // Teste getById()
    @Test
    @DisplayName("Deve retornar o paciente especificado pelo Id")
    public void retornarEnfermeiroPorId_RetornaEnfermeiroPorId() {

        when(repo.findById(1)).thenReturn(Optional.of(quarto1));

        Optional<Quarto> response = service.getQuartoById(1);

        assertEquals(quarto1, response.get());
    }

    // Testes Post
    // teste sucesso cria quarto
    @Test
    @DisplayName("Deve criar um quarto com sucesso")
    public void criarQuarto_DadosValidos_CriaQuarto() {

        // Configuração inicial
        quarto1.setId(null);
        quarto1.setNumero("101");
        quarto1.setAlaMedica(alaMedica);

        alaMedica.setId(1);

        // Simulações dos repositórios
        when(repo.existsByNumero(quarto1.getNumero())).thenReturn(false);
        when(alamedica_repo.existsById(quarto1.getAlaMedica().getId())).thenReturn(true);
        when(repo.save(quarto1)).thenReturn(quarto1);

        // Execução do método
        Optional<Quarto> result = service.criarQuarto(quarto1);

        // Verificações
        assertTrue(result.isPresent()); // Verifica se o Optional não está vazio
        assertEquals(quarto1, result.get());
    }

    // teste falha quarto_id ja existe
    @Test
    @DisplayName("Deve verificar se o Id do quarto não existe")
    public void criarQuarto_QuartoIdInvalido_RetornaOptionalEmpty() {

        // Configuração inicial
        quarto1.setId(1);

        // Execução do método
        Optional<Quarto> result = service.criarQuarto(quarto1);

        // Verificações
        assertTrue(result.isEmpty());
    }

    // teste falha numero do quarto ja existe
    @Test
    @DisplayName("Deve criar um quarto com sucesso")
    public void criarQuarto_NumeroQuartoJaExiste_RetornaOptionalEmpty() {

        // Configuração inicial
        quarto1.setId(null);

        // Simulações dos repositórios
        when(repo.existsByNumero(quarto1.getNumero())).thenReturn(true);

        // Execução do método
        Optional<Quarto> result = service.criarQuarto(quarto1);

        // Verificações
        assertTrue(result.isEmpty());
    }

    // teste falha ala medica null
    @Test
    @DisplayName("Deve criar um quarto com sucesso")
    public void criarQuarto_AlaMedicaNull_RetornaOptionalEmpty() {

        // Configuração inicial
        quarto1.setId(null);
        quarto1.setNumero("101");

        alaMedica.setId(null);

        // Simulações dos repositórios
        when(repo.existsByNumero(quarto1.getNumero())).thenReturn(false);

        // Execução do método
        Optional<Quarto> result = service.criarQuarto(quarto1);

        // Verificações
        assertTrue(result.isEmpty());
    }

    // teste falha ala medica invalida
    @Test
    @DisplayName("Deve criar um quarto com sucesso")
    public void criarQuarto_AlaMedicaInexistente_RetornaOptionalEmpty() {

        // Configuração inicial
        quarto1.setId(null);
        quarto1.setNumero("101");
        quarto1.setAlaMedica(alaMedica);

        alaMedica.setId(1);

        // Simulações dos repositórios
        when(repo.existsByNumero(quarto1.getNumero())).thenReturn(false);
        when(alamedica_repo.existsById(quarto1.getAlaMedica().getId())).thenReturn(false);

        // Execução do método
        Optional<Quarto> result = service.criarQuarto(quarto1);

        // Verificações
        assertTrue(result.isEmpty());
    }

    // teste falha quarto não existe
    @Test
    @DisplayName("Deve verificar se o quarto existe no BD")
    public void editarQuarto_QuartoInexistente_RetornaOptionalEmpty() {

        // Configuração inicial
        quartoAtualizado.setId(null);

        // Simulações dos repositórios
        when(repo.findById(quartoAtualizado.getId())).thenReturn(Optional.empty());

        // Execução do método
        Optional<Quarto> result = service.atualizarQuarto(null, quartoAtualizado);

        // Verificações
        assertTrue(result.isEmpty());
    }

    // teste falha quarto não tem numero unico
    @Test
    @DisplayName("Deve verificar se o número do quarto ja não esta cadastrado no BD")
    public void editarQuarto_QuartoNumeroInvalido_RetornaOptionalEmpty() {

        // Configuração inicial
        quarto1.setId(1);
        quarto1.setNumero("101");

        quartoAtualizado.setNumero("102");

        // Simulações dos repositórios
        when(repo.findById(quarto1.getId())).thenReturn(Optional.of(quarto1));
        when(repo.existsByNumero(quartoAtualizado.getNumero())).thenReturn(true);

        // Execução do método
        Optional<Quarto> result = service.atualizarQuarto(1, quartoAtualizado);

        // Verificações
        assertTrue(result.isEmpty());
    }

    // Testes Delete
    @Test
    @DisplayName("Deve retornar falso ao tentar deletar um quarto inexistente")
    public void deletarQuarto_QuartoInexistente_RetornaFalse() {
        // Configuração inicial
        when(repo.findById(1)).thenReturn(Optional.empty());

        // Execução
        boolean resultado = service.deletarQuarto(1);

        // Verificação
        assertFalse(resultado, "Deve retornar falso se o quarto não for encontrado.");
    }

    @Test
    @DisplayName("Deve desassociar o paciente e deletar o quarto com sucesso")
    public void deletarQuarto_ComPaciente_AssociaPacienteNuloERemoveQuarto() {
        // Configuração inicial
        paciente.setId(1);

        quarto1.setId(1);
        quarto1.setPaciente(paciente);

        when(repo.findById(1)).thenReturn(Optional.of(quarto1));
        doNothing().when(repo).deleteById(1);
        when(paciente_repo.save(paciente)).thenReturn(paciente);

        // Execução
        boolean resultado = service.deletarQuarto(1);

        // Verificações
        assertTrue(resultado, "Deve retornar verdadeiro se o quarto for deletado com sucesso.");
        assertNull(paciente.getQuarto(), "O paciente deve ser desassociado do quarto.");
        verify(paciente_repo).save(paciente);
        verify(repo).deleteById(1);
    }

    @Test
    @DisplayName("Deve deletar o quarto sem paciente ou ala médica associada")
    public void deletarQuarto_SemPacienteOuAlaMedica_RemoveQuarto() {
        // Configuração inicial
        quarto1.setId(1);

        when(repo.findById(1)).thenReturn(Optional.of(quarto1));
        doNothing().when(repo).deleteById(1);

        // Execução
        boolean resultado = service.deletarQuarto(1);

        // Verificações
        assertTrue(resultado, "Deve retornar verdadeiro se o quarto for deletado com sucesso.");
        verify(repo).deleteById(1);
    }
}
