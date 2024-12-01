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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.anhembi.alamedica.dto.EnfermeiroDTO;
import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.repository.AlamedicaRepository;
import com.anhembi.alamedica.service.EnfermeiroService;

@ExtendWith(MockitoExtension.class)
public class EnfermeiroControllerTest {

    @InjectMocks
    private EnfermeiroController controller;

    @Mock
    private EnfermeiroService service;

    @Mock
    private AlamedicaRepository alamedica_repo;

    private EnfermeiroDTO enfermeiroDto;
    private Alamedica alaMedica;
    private Enfermeiro enfermeiro1;
    private Enfermeiro enfermeiro2;

    @BeforeEach
    public void setUp() {
        enfermeiroDto = new EnfermeiroDTO();
        enfermeiroDto.setNome("André");
        enfermeiroDto.setId_alamedica(1);

        alaMedica = new Alamedica();

        enfermeiro1 = new Enfermeiro(1, "Douglas", null);
        enfermeiro2 = new Enfermeiro(2, "Leonardo", null);
    }

    // Testes getAll
    @Test
    @DisplayName("Deve retornar todos os enfermeiros")
    public void RetornarTodosEnfermeiros_retornaTodosEnfermeiros() {

        when(service.getAllEnfermeiros()).thenReturn(List.of(enfermeiro1, enfermeiro2));

        ResponseEntity<List<Enfermeiro>> response = controller.getAll();

        assertEquals(2, response.getBody().size());

        // Verifica se o conteúdo está correto
        assertEquals(enfermeiro1, response.getBody().get(0));
        assertEquals(enfermeiro2, response.getBody().get(1));
    }

    // Testes getById
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

    // Testes delete
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

    // Testes Post
    // teste sucesso alaMedica valida
    @Test
    @DisplayName("Deve criar um enfermeiro com uma Ala Médica válida")
    public void criarEnfermeiro_AlaMedicaValida_CriaEnfermeiro() {

        when(alamedica_repo.findById(enfermeiroDto.getId_alamedica())).thenReturn(Optional.of(alaMedica));

        Enfermeiro enfermeiro = enfermeiroDto.toEnfermeiro(alaMedica);

        when(service.cadastrarEnfermeiro(Mockito.any(Enfermeiro.class))).thenReturn(Optional.of(enfermeiro));

        ResponseEntity<Enfermeiro> response = controller.register(enfermeiroDto);

        assertEquals(enfermeiro, response.getBody());
    }

    // teste sucesso alaMedica null
    @Test
    @DisplayName("Deve criar um enfermeiro sem uma Ala Médica associada")
    public void criarEnfermeiro_AlaMedicaNull_CriaEnfermeiro() {

        // Configura o DTO sem Ala Médica
        enfermeiroDto.setId_alamedica(null);

        Enfermeiro enfermeiro = enfermeiroDto.toEnfermeiro(null);

        // Simula a criação do enfermeiro com sucesso
        when(service.cadastrarEnfermeiro(Mockito.any(Enfermeiro.class))).thenReturn(Optional.of(enfermeiro));

        // Chama o método do controlador
        ResponseEntity<Enfermeiro> response = controller.register(enfermeiroDto);

        // Verifica o retorno
        assertEquals(enfermeiro, response.getBody());
    }

    // teste falha alaMedica empty
    @Test
    @DisplayName("Deve retornar Not Found se a Ala Médica não for encontrada")
    public void criarEnfermeiro_AlaMedicaNaoEncontrada_RetornaNotFound() {

        // Simula que a Ala Médica não foi encontrada
        when(alamedica_repo.findById(enfermeiroDto.getId_alamedica())).thenReturn(Optional.empty());

        // Chama o método do controlador
        ResponseEntity<Enfermeiro> response = controller.register(enfermeiroDto);

        // Verifica o retorno
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // teste falha service recusou
    @Test
    @DisplayName("Deve retornar Bad Request se o service recusar a criação")
    public void criarEnfermeiro_ServiceRecusou_RetornaBadRequest() {

        // Simula a Ala Médica válida
        when(alamedica_repo.findById(enfermeiroDto.getId_alamedica())).thenReturn(Optional.of(alaMedica));

        // Simula que o serviço recusou a criação
        when(service.cadastrarEnfermeiro(Mockito.any(Enfermeiro.class))).thenReturn(Optional.empty());

        // Chama o método do controlador
        ResponseEntity<Enfermeiro> response = controller.register(enfermeiroDto);

        // Verifica o retorno
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Testes Put
    // teste sucesso alaMedica valida
    @Test
    @DisplayName("Deve atualizar um enfermeiro com uma Ala Médica válida")
    public void atualizarEnfermeiro_AlaMedicaValida_AtualizaEnfermeiro() {

        // Configura Ala Médica válida
        when(alamedica_repo.findById(enfermeiroDto.getId_alamedica())).thenReturn(Optional.of(alaMedica));

        Enfermeiro enfermeiroAtualizado = enfermeiroDto.toEnfermeiro(alaMedica);

        when(service.atualizarEnfermeiro(Mockito.anyInt(), Mockito.any(Enfermeiro.class)))
                .thenReturn(Optional.of(enfermeiroAtualizado));

        // Chama o método do controlador
        ResponseEntity<Enfermeiro> response = controller.update(1, enfermeiroDto);

        // Verifica o retorno
        assertEquals(enfermeiroAtualizado, response.getBody());
    }

    // teste sucesso alaMedica null
    @Test
    @DisplayName("Deve atualizar um enfermeiro sem uma Ala Médica associada")
    public void atualizarEnfermeiro_AlaMedicaNull_AtualizaEnfermeiro() {

        // Configura o DTO sem Ala Médica
        enfermeiroDto.setId_alamedica(null);

        Enfermeiro enfermeiroAtualizado = enfermeiroDto.toEnfermeiro(null);

        when(service.atualizarEnfermeiro(Mockito.anyInt(), Mockito.any(Enfermeiro.class)))
                .thenReturn(Optional.of(enfermeiroAtualizado));

        // Chama o método do controlador
        ResponseEntity<Enfermeiro> response = controller.update(1, enfermeiroDto);

        // Verifica o retorno
        assertEquals(enfermeiroAtualizado, response.getBody());
    }

    // teste falha alaMedica empty
    @Test
    @DisplayName("Deve retornar Not Found se a Ala Médica não for encontrada")
    public void atualizarEnfermeiro_AlaMedicaNaoEncontrada_RetornaBadRequest() {

        // Simula que a Ala Médica não foi encontrada
        when(alamedica_repo.findById(enfermeiroDto.getId_alamedica())).thenReturn(Optional.empty());

        // Chama o método do controlador
        ResponseEntity<Enfermeiro> response = controller.update(1, enfermeiroDto);

        // Verifica o retorno
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // testes falha service recusou
    @Test
    @DisplayName("Deve retornar Bad Request se o service recusar a atualização")
    public void atualizarEnfermeiro_ServiceRecusou_RetornaBadRequest() {

        // Configura Ala Médica válida
        when(alamedica_repo.findById(enfermeiroDto.getId_alamedica())).thenReturn(Optional.of(alaMedica));

        // Simula que o serviço recusou a atualização
        when(service.atualizarEnfermeiro(Mockito.anyInt(), Mockito.any(Enfermeiro.class)))
                .thenReturn(Optional.empty());

        // Chama o método do controlador
        ResponseEntity<Enfermeiro> response = controller.update(1, enfermeiroDto);
        
        // Verifica o retorno
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
