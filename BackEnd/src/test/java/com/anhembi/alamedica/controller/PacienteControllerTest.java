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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.anhembi.alamedica.dto.PacienteDTO;
import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.QuartoRepository;
import com.anhembi.alamedica.service.PacienteService;

@ExtendWith(MockitoExtension.class)
public class PacienteControllerTest {

    @InjectMocks
    private PacienteController controller;

    @Mock
    private PacienteService service;

    @Mock
    private QuartoRepository quarto_repo;

    private Quarto quarto;
    private PacienteDTO pacienteDto;
    private Paciente paciente1;
    private Paciente paciente2;

    @BeforeEach
    public void setUp() {
        pacienteDto = new PacienteDTO();
        pacienteDto.setNome("Amanda");
        pacienteDto.setAlergia("Nenhuma");
        pacienteDto.setData_de_nascimento(LocalDate.of(2004, 6, 5));
        pacienteDto.setInternado(false);
        pacienteDto.setQuarto_id(1);
        pacienteDto.setEnfermidade("Dor de cabeça");

        quarto = new Quarto("204", null, null);

        paciente1 = new Paciente("Douglas", LocalDate.of(1990, 6, 17), "Dor no braço", "Nenhuma", null);
        paciente2 = new Paciente("Leandro", LocalDate.of(2002, 10, 12), "Enjoo", "Amendoim", null);
    }

    // Testes getAll
    @Test
    @DisplayName("Deve retornar todos os pacientes")
    public void RetornarTodosPacientes_retornaTodosPacientes() {
        // Mockando o comportamento do serviço
        when(service.getAllPacientes()).thenReturn(List.of(paciente1, paciente2));

        // Chamando o controlador
        ResponseEntity<List<Paciente>> response = controller.getAll();

        // Verificando o resultado
        assertEquals(2, response.getBody().size());

        // Verifica se o conteúdo está correto
        assertEquals(paciente1, response.getBody().get(0));
        assertEquals(paciente2, response.getBody().get(1));
    }

    // Testes getById
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

    // Testes delete
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

    // Testes Post
    // teste sucesso paciente e ala validos
    @Test
    @DisplayName("Deve criar um paciente com dados válidos")
    public void criarPaciente_PacienteComQuarto_CriaPaciente() {

        // Simula o paciente válido
        when(quarto_repo.findById(pacienteDto.getQuarto_id())).thenReturn(Optional.of(quarto));

        Paciente paciente = pacienteDto.toPaciente(quarto);

        when(service.cadastrarPaciente(Mockito.any(Paciente.class))).thenReturn(Optional.of(paciente));

        ResponseEntity<Paciente> response = controller.register(pacienteDto);

        // Verifica se o paciente foi criado com sucesso
        assertEquals(paciente, response.getBody());
    }

    // teste sucesso com quarto null
    @Test
    @DisplayName("Deve criar um paciente sem quarto associado")
    public void criarPaciente_PacienteSemQuarto_CriaPaciente() {

        // Configura o DTO de paciente sem quarto_id
        pacienteDto.setQuarto_id(null);

        // Converte o DTO para entidade Paciente sem quarto
        Paciente paciente = pacienteDto.toPaciente(null);

        // Simula o sucesso ao cadastrar o paciente
        when(service.cadastrarPaciente(Mockito.any(Paciente.class))).thenReturn(Optional.of(paciente));

        // Chama o método de registro do controlador
        ResponseEntity<Paciente> response = controller.register(pacienteDto);

        // Verifica se o paciente retornado é o esperado
        assertEquals(paciente, response.getBody());
    }

    // teste falha com quarto empty
    @Test
    @DisplayName("Deve retornar Not Found quando o quarto não for encontrado")
    public void criarPaciente_QuartoInvalido_RetornaNotFound() {

        // Configura o DTO de paciente com quarto_id inválido
        pacienteDto.setQuarto_id(999); // ID de quarto que não existe

        // Simula a busca do quarto que não existe
        when(quarto_repo.findById(999)).thenReturn(Optional.empty());

        // Chama o método de registro do controlador
        ResponseEntity<Paciente> response = controller.register(pacienteDto);

        // Verifica se o status é NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // teste falha service recusou paciente
    @Test
    @DisplayName("Deve retornar Bad Request quando o service recusar o cadastro do paciente")
    public void criarPaciente_ServiceRecusou_RetornaBadRequest() {

        // Simula o quarto válido
        when(quarto_repo.findById(pacienteDto.getQuarto_id())).thenReturn(Optional.of(quarto));

        // Simula que o serviço recusou o cadastro (retorna Optional.empty)
        when(service.cadastrarPaciente(Mockito.any(Paciente.class))).thenReturn(Optional.empty());

        // Chama o método de registro do controlador
        ResponseEntity<Paciente> response = controller.register(pacienteDto);

        // Verifica se o status é BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Testes Put
    // teste sucesso paciente e ala validos
    @Test
    @DisplayName("Deve atualizar um paciente com dados válidos")
    public void atualizarPaciente_PacienteComQuarto_AtualizaPaciente() {

        // Simula o quarto válido
        when(quarto_repo.findById(pacienteDto.getQuarto_id())).thenReturn(Optional.of(quarto));

        // Converte o DTO para entidade Paciente
        Paciente pacienteAtualizado = pacienteDto.toPaciente(quarto);

        // Simula o sucesso ao atualizar o paciente
        when(service.atualizarPaciente(Mockito.anyInt(), Mockito.any(Paciente.class)))
                .thenReturn(Optional.of(pacienteAtualizado));

        // Chama o método de atualização do controlador
        ResponseEntity<Paciente> response = controller.update(1, pacienteDto);

        // Verifica se o paciente retornado é o esperado
        assertEquals(pacienteAtualizado, response.getBody());
    }

    // teste sucesso com quarto null
    @Test
    @DisplayName("Deve atualizar um paciente sem quarto associado")
    public void atualizarPaciente_PacienteSemQuarto_AtualizaPaciente() {

        // Configura o DTO de paciente sem quarto_id
        pacienteDto.setQuarto_id(null);

        // Converte o DTO para entidade Paciente sem quarto
        Paciente pacienteAtualizado = pacienteDto.toPaciente(null);

        // Simula o sucesso ao atualizar o paciente
        when(service.atualizarPaciente(Mockito.anyInt(), Mockito.any(Paciente.class)))
                .thenReturn(Optional.of(pacienteAtualizado));

        // Chama o método de atualização do controlador
        ResponseEntity<Paciente> response = controller.update(1, pacienteDto);

        // Verifica se o paciente retornado é o esperado
        assertEquals(pacienteAtualizado, response.getBody());
    }
    
    // teste falha com quarto empty
    @Test
    @DisplayName("Deve retornar Not Found quando o quarto não for encontrado")
    public void atualizarPaciente_QuartoInvalido_RetornaBadRequest() {

        // Configura o DTO de paciente com quarto_id inválido
        pacienteDto.setQuarto_id(999); // ID de quarto que não existe

        // Simula a busca do quarto que não existe
        when(quarto_repo.findById(999)).thenReturn(Optional.empty());

        // Chama o método de atualização do controlador
        ResponseEntity<Paciente> response = controller.update(1, pacienteDto);

        // Verifica se o status é NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // teste falha service recusou paciente
    @Test
    @DisplayName("Deve retornar Bad Request quando o serviço recusar a atualização do paciente")
    public void atualizarPaciente_ServicoRecusou_RetornaBadRequest() {

        // Simula o quarto válido
        when(quarto_repo.findById(pacienteDto.getQuarto_id())).thenReturn(Optional.of(quarto));

        // Simula que o serviço recusou a atualização (retorna Optional.empty)
        when(service.atualizarPaciente(Mockito.anyInt(), Mockito.any(Paciente.class)))
            .thenReturn(Optional.empty());

        // Chama o método de atualização do controlador
        ResponseEntity<Paciente> response = controller.update(1, pacienteDto);
        
        // Verifica se o status é BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
