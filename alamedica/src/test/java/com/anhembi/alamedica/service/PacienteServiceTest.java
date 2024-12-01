package com.anhembi.alamedica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private Paciente pacienteAtualizado;
    private Quarto quartoDisponivel;
    private Quarto quartoOcupado;

    @BeforeEach
    public void setUp() {
        paciente1 = new Paciente();
        paciente1.setNome("Paciente 1");

        paciente2 = new Paciente();
        paciente2.setNome("Paciente 2");

        pacienteAtualizado = new Paciente();
        pacienteAtualizado.setNome("Paciente Atualizado");
        pacienteAtualizado.setAlergia("Alergia");
        pacienteAtualizado.setInternado(true);
        pacienteAtualizado.setData_de_nascimento(LocalDate.of(2006, 4, 11));
        pacienteAtualizado.setEnfermidade("Enfermidade");

        quartoDisponivel = new Quarto("101", null, null);
        
        quartoOcupado = new Quarto("102", new Paciente() , null);
    }

    //Teste getAllPacientes()
    @Test
    @DisplayName("Deve retornar todos os pacientes cadastrados")
    public void retornarTodosPacientes_RetornaTodosPacientes() {

        when(repo.findAll()).thenReturn(List.of(paciente1, paciente2));

        List<Paciente> result = service.getAllPacientes();

        assertEquals(2, result.size());

        // Verifica se o conteúdo está correto
        assertEquals(paciente1, result.get(0));
        assertEquals(paciente2, result.get(1));
    }

    //Teste getById()
    @Test
    @DisplayName("Deve retornar o paciente especificado pelo Id")
    public void retornarEnfermeiroPorId_RetornaEnfermeiroPorId() {

        when(repo.findById(1)).thenReturn(Optional.of(paciente1));

        Optional<Paciente> response = service.getPacienteById(1);

        assertEquals(paciente1, response.get());
    }

    //Teste Post
    //teste sucesso cadastrar paciente com quarto
    @Test
    @DisplayName("Deve cadastrar um paciente com um quarto com sucesso")
    public void cadastrarPaciente_QuartoEPacienteValidos_RetornaPacienteComQuarto() {
        
        // Configuração inicial
        paciente1.setId(null); // Deve ser null para simular um novo cadastro
        paciente1.setQuarto(quartoDisponivel); // Associar o quarto disponível ao paciente
    
        // Simulações dos repositórios
        when(repo.save(paciente1)).thenReturn(paciente1);
    
        // Execução do método
        Optional<Paciente> result = service.cadastrarPaciente(paciente1);
    
        // Verificações
        assertTrue(result.isPresent()); // Verifica se o Optional não está vazio
        assertEquals(paciente1, result.get()); // Verifica se o paciente cadastrado é o esperado
    }

    //teste sucesso cadastrar paciente sem quarto (Não possivel no momento) da pra usar pra verificar se ele é empty caso não seja possivel fazer isso mesmo
    // @Test
    // @DisplayName("Deve cadastrar um paciente sem um quarto com sucesso")
    // public void cadastrarPaciente_QuartoNull_RetornaPacienteSemQuarto() {
        
    //     // Configuração inicial
    //     paciente1.setId(null); // Deve ser null para simular um novo cadastro
    
    //     // Simulações dos repositórios
    //     when(repo.save(paciente1)).thenReturn(paciente1);
    
    //     // Execução do método
    //     Optional<Paciente> result = service.cadastrarPaciente(paciente1);
    
    //     // Verificações
    //     assertTrue(result.isPresent()); // Verifica se o Optional não está vazio
    //     assertEquals(paciente1, result.get()); // Verifica se o paciente cadastrado é o esperado
    // }


    //teste falha paciente_Id ja está cadastrado
    @Test
    @DisplayName("Deve verificar se o paciente ja tem um Id cadastrado")
    public void cadastrarPaciente_PacienteIdInvalido_RetornaOptionalEmpty() {

        // Configuração inicial
        paciente1.setId(1); // como é a primeira coisa que o método checa, não é necessario colocar mais nada

        // Execução do método
        Optional<Paciente> result = service.cadastrarPaciente(paciente1);

        // Verificações
        assertTrue(result.isEmpty());
    }

    //teste falha Quarto ja está ocupado
    @Test
    @DisplayName("Deve veriicar se o quarto desejado ja esta ocupado")
    public void cadastrarPaciente_QuartoOcupado_RetornaOptionalEmpty() {

        // Configuração inicial
        paciente1.setId(null);
        paciente1.setQuarto(quartoOcupado);

        // Execução do método
        Optional<Paciente> result = service.cadastrarPaciente(paciente1);

        // Verificações
        assertTrue(result.isEmpty());
    }

    //Testes Put
    //teste sucesso paciente atualizado com um quarto novo
    @Test
    @DisplayName("Deve atualizar um paciente sem quarto com um quarto novo")
    public void atualizarPaciente_PacienteSemQuarto_AtualizaPacienteComUmQuarto() {

        // Configuração inicial
        paciente1.setId(1);
        paciente1.setQuarto(null);

        pacienteAtualizado.setQuarto(quartoDisponivel);

        // Simulações dos repositórios
        when(repo.findById(paciente1.getId())).thenReturn(Optional.of(paciente1));
        when(repo.save(paciente1)).thenReturn(pacienteAtualizado);

        // Execução do método
        Optional<Paciente> result = service.atualizarPaciente(1, pacienteAtualizado, quartoDisponivel);

        // Verificações
        assertTrue(result.isPresent()); // Verifica se o Optional não está vazio
        assertEquals(pacienteAtualizado, result.get()); // Verifica se o paciente atualizado é o esperado
        assertEquals(quartoDisponivel, result.get().getQuarto());
    }

    //teste sucesso paciente com um quarto muda para outro quarto
    @Test
    @DisplayName("")
    public void atualizarPaciente_PacienteComQuarto_AtualizaPacienteComQuartoNovo() {

        // Configuração inicial
        paciente1.setId(1);
        paciente1.setQuarto(quartoOcupado);

        quartoOcupado.setPaciente(paciente1);

        pacienteAtualizado.setQuarto(quartoDisponivel);

        // Simulações dos repositórios
        when(repo.findById(paciente1.getId())).thenReturn(Optional.of(paciente1));
        when(repo.save(paciente1)).thenReturn(pacienteAtualizado);

        // Execução do método
        Optional<Paciente> result = service.atualizarPaciente(1, pacienteAtualizado, quartoDisponivel);

        // Verificações
        assertTrue(result.isPresent()); // Verifica se o Optional não está vazio
        assertEquals(pacienteAtualizado, result.get()); // Verifica se o paciente atualizado é o esperado
        assertEquals(quartoDisponivel, result.get().getQuarto());
    }

    //teste sucesso paciente atualizado sem um quarto (ver com o Dré se esse é o comportamento que deveria acontecer)
    // @Test
    // @DisplayName("")
    // public void atualizarPaciente_PacienteComQuarto_AtualizaPacienteComQuartoNovo() {

    //     // Configuração inicial
    //     paciente1.setId(1);
    //     paciente1.setQuarto(quartoOcupado);

    //     quartoOcupado.setPaciente(paciente1);

    //     pacienteAtualizado.setQuarto(quartoDisponivel);

    //     // Simulações dos repositórios
    //     when(repo.findById(paciente1.getId())).thenReturn(Optional.of(paciente1));
    //     when(repo.save(paciente1)).thenReturn(pacienteAtualizado);

    //     // Execução do método
    //     Optional<Paciente> result = service.atualizarPaciente(1, pacienteAtualizado, quartoDisponivel);

    //     // Verificações
    //     assertTrue(result.isPresent()); // Verifica se o Optional não está vazio
    //     assertEquals(pacienteAtualizado, result.get()); // Verifica se o paciente atualizado é o esperado
    //     assertEquals(quartoDisponivel, result.get().getQuarto());
    // }

    //teste falha paciente_Id não cadastrado no BD
    @Test
    @DisplayName("Deve verificar se o paciente ja tem um Id cadastrado")
    public void atualizarPaciente_PacienteIdInvalido_RetornaOptionalEmpty() {

        // Configuração inicial
        paciente1.setId(null);

        // Simulações dos repositórios
        when(repo.findById(paciente1.getId())).thenReturn(Optional.empty());

        // Execução do método
        Optional<Paciente> result = service.atualizarPaciente(null, pacienteAtualizado, quartoDisponivel);

        // Verificações
        assertTrue(result.isEmpty());
    }

    //teste falha quarto desejado ja está ocupado
    @Test
    @DisplayName("Deve verificar se o quarto desejado ja esta ocupado")
    public void atualizarPaciente_QuartoOcupado_RetornaOptionalEmpty() {
        
        // Configuração inicial
        paciente1.setId(1);

        // Simulações dos repositórios
        when(repo.findById(paciente1.getId())).thenReturn(Optional.of(paciente1));

        // Execução do método
        Optional<Paciente> result = service.atualizarPaciente(1, pacienteAtualizado, quartoOcupado);

        // Verificações
        assertTrue(result.isEmpty());
    }

    //Testes delete
    //teste sucesso deleta paciente com quarto
    @Test
    @DisplayName("Deve verificar se o paciente foi deletado com sucesso")
    public void deletarPaciente_PacienteComQuartoExiste_PacienteDeletado() {

        // Configuração inicial
        paciente1.setId(1);
        paciente1.setQuarto(quartoOcupado);

        quartoOcupado.setPaciente(paciente1);

        // Simulações dos repositórios
        when(repo.findById(paciente1.getId())).thenReturn(Optional.of(paciente1));

        // Execução do método
        Boolean result = service.deletarPaciente(paciente1.getId());

        assertTrue(result);
        assertEquals(null, quartoOcupado.getPaciente());
    }

    //teste sucesso deleta paciente sem quarto
    @Test
    @DisplayName("Deve verificar se o paciente foi deletado com sucesso")
    public void deletarPaciente_PacienteSemQuartoExiste_PacienteDeletado() {

        // Configuração inicial
        paciente1.setId(1);
        paciente1.setQuarto(null);

        // Simulações dos repositórios
        when(repo.findById(paciente1.getId())).thenReturn(Optional.of(paciente1));

        // Execução do método
        Boolean result = service.deletarPaciente(paciente1.getId());

        assertTrue(result);
    }

    //teste falha paciente não existe
    @Test
    @DisplayName("Deve verificar se o paciente existe no BD")
    public void deletarPaciente_PacienteInexistente_RetornaFalse() {

        // Configuração inicial
        paciente1.setId(null);

        // Simulações dos repositórios
        when(repo.findById(paciente1.getId())).thenReturn(Optional.empty());

        // Execução do método
        Boolean result = service.deletarPaciente(paciente1.getId());

        assertFalse(result);
    }
}
