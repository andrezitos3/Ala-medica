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

import com.anhembi.alamedica.dto.QuartoDTO;
import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.AlamedicaRepository;
import com.anhembi.alamedica.repository.PacienteRepository;
import com.anhembi.alamedica.service.QuartoService;

@ExtendWith(MockitoExtension.class)
public class QuartoControllerTest {

    @InjectMocks
    private QuartoController controller;

    @Mock
    private QuartoService service;

    @Mock
    private AlamedicaRepository alamedica_repo;

    @Mock
    private PacienteRepository paciente_repo;

    private QuartoDTO quartoDto;
    private Alamedica alamedica;
    private Paciente paciente;
    private Quarto quarto1;
    private Quarto quarto2;
    
    @BeforeEach
    public void setUp() {
        quarto1 = new Quarto(1, "101", null, alamedica);
        quarto2 = new Quarto(2, "102", null, alamedica);

        quartoDto = new QuartoDTO();
        quartoDto.setId_alamedica(1);
        quartoDto.setId_paciente(1);
        quartoDto.setNumero("103");

        alamedica = new Alamedica(1, 1, null, List.of(quarto1, quarto2));
        paciente = new Paciente("Everson", LocalDate.of(1990, 12, 4), "Dor no abdomen", "Nenhuma", null);
    }

    //Testes getAll
    @Test
    @DisplayName("Deve retornar todos os quartos")
    public void retornarTodosQuartos_retornaTodosQuartos() {

        when(service.getAllQuartos()).thenReturn(List.of(quarto1, quarto2));

        ResponseEntity<List<Quarto>> response = controller.getAll();

        assertEquals(2, response.getBody().size());
    }

    //Testes getById
    @Test
    @DisplayName("Deve retornar o quarto especificado pelo ID")
    public void retornarQuartoPeloId_IdValido_RetornaQuartoPeloId() {

        when(service.getQuartoById(1)).thenReturn(Optional.of(quarto1));

        ResponseEntity<Quarto> response = controller.getById(1);

        assertEquals(quarto1, response.getBody());
    }

    @Test
    @DisplayName("Deve retornar NotFound se o quarto não for encontrado")
    public void retornarEnfermeiroPeloId_IdInvalido_RetornaNotFound() {

        when(service.getQuartoById(999)).thenReturn(Optional.empty());

        ResponseEntity<Quarto> response = controller.getById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //Testes delete
    @Test
    @DisplayName("Deve verificaro caso de sucesso da função delete")
    public void deletarQuarto_QuartoValido_RetornaNoContent() {

        when(service.deletarQuarto(1)).thenReturn(true);

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificaro caso de falha da função delete")
    public void deletarQuarto_QuartoInvalido_RetornaNoContent() {

        when(service.deletarQuarto(999)).thenReturn(false);

        ResponseEntity<Void> response = controller.delete(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //Testes Post
    //teste de sucesso
    @Test
    @DisplayName("Deve criar um quarto com base no paciente e na ala médica")
    public void criarQuarto_AlaMedicaEPacienteValidos_CriaQuarto() {

        //Transforma a ala médica em um optional
        when(alamedica_repo.findById(quartoDto.getId_alamedica())).thenReturn(Optional.of(alamedica));

        //Transforma o paciente em um optional
        when(paciente_repo.findById(quartoDto.getId_paciente())).thenReturn(Optional.of(paciente));

        //Cria um quarto com o quartoDto
        Quarto quarto_novo = quartoDto.toQuarto(paciente, alamedica);

        //Cria um optional do quarto_novo
        when(service.criarQuarto(Mockito.any(Quarto.class))).thenReturn(Optional.of(quarto_novo));

        ResponseEntity<Quarto> response = controller.create(quartoDto);

        assertEquals(quarto_novo, response.getBody());
    }

    //teste de falha pela ala médica
    @Test
    @DisplayName("Deve retornar um Not Found no caso da ala médica ser invalida")
    public void criarQuarto_AlaMedicaInvalida_RetornaNotFound() {

            //Transforma a ala médica em um optional empty por ser invalida
            when(alamedica_repo.findById(quartoDto.getId_alamedica())).thenReturn(Optional.empty());

            ResponseEntity<Quarto> response = controller.create(quartoDto);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //teste de falha pelo paciente
    @Test
    @DisplayName("Deve retornar um Not Found no caso do paciente ser invalido")
    public void criarQuarto_PacienteInvalido_RetornaNotFound() {

        //Transforma a ala médica em um optional
        when(alamedica_repo.findById(quartoDto.getId_alamedica())).thenReturn(Optional.of(alamedica));

        //Transforma o pacient em um optional empty por ser invalido
        when(paciente_repo.findById(quartoDto.getId_paciente())).thenReturn(Optional.empty());

        ResponseEntity<Quarto> response = controller.create(quartoDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //teste de falha na criação do quarto
    @Test
    @DisplayName("Deve retornar um Not Found no caso do paciente ser invalido")
    public void criarQuarto_ServiceRecusou_RetornaBadRequest() {

        //Transforma a ala médica em um optional
        when(alamedica_repo.findById(quartoDto.getId_alamedica())).thenReturn(Optional.of(alamedica));

        //Transforma o paciente em um optional
        when(paciente_repo.findById(quartoDto.getId_paciente())).thenReturn(Optional.of(paciente));

        //Cria um optional de quarto empty
        when(service.criarQuarto(Mockito.any(Quarto.class))).thenReturn(Optional.empty());

        ResponseEntity<Quarto> response = controller.create(quartoDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve criar um quarto mesmo quando Id_paciente é nulo")
    public void criarQuarto_IdPacienteNulo_CriaQuarto() {

        // Configuração do QuartoDto com Id_paciente nulo
        quartoDto.setId_paciente(null);

        // Simula o retorno válido para a ala médica
        when(alamedica_repo.findById(quartoDto.getId_alamedica())).thenReturn(Optional.of(alamedica));

        // Criação de quarto sem paciente
        Quarto quarto_novo = quartoDto.toQuarto(null, alamedica);

        // Simula a criação do quarto
        when(service.criarQuarto(Mockito.any(Quarto.class))).thenReturn(Optional.of(quarto_novo));

        ResponseEntity<Quarto> response = controller.create(quartoDto);

        // Verificação
        assertEquals(quarto_novo, response.getBody());
    }

    @Test
    @DisplayName("Deve criar um quarto mesmo quando Id_alamedica é nulo")
    public void criarQuarto_IdAlamedicaNulo_CriaQuarto() {

        // Configuração do QuartoDTO com Id_alamedica nulo
        quartoDto.setId_alamedica(null);

        // Simula o retorno válido para o paciente
        when(paciente_repo.findById(quartoDto.getId_paciente())).thenReturn(Optional.of(paciente));

        // Criação de quarto sem ala médica
        Quarto quarto_atualizado = quartoDto.toQuarto(paciente, null);

        // Simula a criação do quarto
        when(service.criarQuarto(Mockito.any(Quarto.class))).thenReturn(Optional.of(quarto_atualizado));

        ResponseEntity<Quarto> response = controller.create(quartoDto);

        // Verificação
        assertEquals(quarto_atualizado, response.getBody());
    }

    //Testes Put
    //teste sucesso paciente e ala validos
    @Test
    @DisplayName("Deve alterar um quarto com base no paciente e na ala médica")
    public void alterarQuarto_AlaMedicaEPacienteValidos_AlteraQuarto() {

        //Transforma a ala médica em um optional
        when(alamedica_repo.findById(quartoDto.getId_alamedica())).thenReturn(Optional.of(alamedica));

        //Transforma o paciente em um optional
        when(paciente_repo.findById(quartoDto.getId_paciente())).thenReturn(Optional.of(paciente));

        //Cria um quarto com o quartoDto
        Quarto quarto_novo = quartoDto.toQuarto(paciente, alamedica);

        //Cria um optional do quarto_novo
        when(service.atualizarQuarto(Mockito.anyInt(), Mockito.any(Quarto.class))).thenReturn(Optional.of(quarto_novo));

        ResponseEntity<Quarto> response = controller.update(1, quartoDto);

        assertEquals(quarto_novo, response.getBody());
    }

    //teste falha paciente empty
    @Test
    @DisplayName("Deve retornar um Not Found no caso do paciente ser inválido")
    public void atualizarQuarto_PacienteInvalido_RetornaNotFound() {

        // Simula a ala médica como válida
        when(alamedica_repo.findById(quartoDto.getId_alamedica())).thenReturn(Optional.of(alamedica));

        // Simula o paciente como inválido (Optional.empty)
        when(paciente_repo.findById(quartoDto.getId_paciente())).thenReturn(Optional.empty());

        ResponseEntity<Quarto> response = controller.update(1, quartoDto); // Chama o método PUT com ID do quarto

        // Verifica se a resposta é Not Found (404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //teste falha ala empty
    @Test
    @DisplayName("Deve retornar um Not Found no caso da ala médica ser inválida")
    public void atualizarQuarto_AlaMedicaInvalida_RetornaNotFound() {

        // Simula a ala médica como inválida (Optional.empty)
        when(alamedica_repo.findById(quartoDto.getId_alamedica())).thenReturn(Optional.empty());

        ResponseEntity<Quarto> response = controller.update(1, quartoDto); // Chama o método PUT com ID do quarto

        // Verifica se a resposta é Not Found (404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //teste falha quarto empty
    @Test
    @DisplayName("Deve retornar um Bad Request no caso de falha na atualização do quarto")
    public void atualizarQuarto_ServiceRecusou_RetornaBadRequest() {

        // Simula a ala médica como válida
        when(alamedica_repo.findById(quartoDto.getId_alamedica())).thenReturn(Optional.of(alamedica));

        // Simula o paciente como válido
        when(paciente_repo.findById(quartoDto.getId_paciente())).thenReturn(Optional.of(paciente));

        // Simula a falha na criação do quarto (retorna um Optional vazio)
        when(service.atualizarQuarto(Mockito.anyInt(), Mockito.any(Quarto.class))).thenReturn(Optional.empty());

        ResponseEntity<Quarto> response = controller.update(1, quartoDto); // Chama o método PUT com ID do quarto

        // Verifica se a resposta é Bad Request (400)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //teste sucesso ala nula
    @Test
    @DisplayName("Deve atualizar um quarto mesmo quando Id_alamedica é nulo")
    public void atualizarQuarto_IdAlamedicaNulo_AtualizaQuarto() {

        // Configura o QuartoDTO com `Id_alamedica` nulo
        quartoDto.setId_alamedica(null);

        // Simula o paciente como válido
        when(paciente_repo.findById(quartoDto.getId_paciente())).thenReturn(Optional.of(paciente));

        // Cria um quarto sem ala médica
        Quarto quarto_atualizado = quartoDto.toQuarto(paciente, null);

        // Simula a atualização do quarto
        when(service.atualizarQuarto(Mockito.anyInt(), Mockito.any(Quarto.class))).thenReturn(Optional.of(quarto_atualizado));

        ResponseEntity<Quarto> response = controller.update(1, quartoDto); // Chama o método PUT com ID do quarto

        // Verifica se o quarto foi atualizado corretamente
        assertEquals(quarto_atualizado, response.getBody());
    }
    
    //teste sucesso paciente nulo
    @Test
    @DisplayName("Deve atualizar um quarto mesmo quando Id_paciente é nulo")
    public void atualizarQuarto_IdPacienteNulo_AtualizaQuarto() {
    
        // Simula a ala médica como válida
        when(alamedica_repo.findById(quartoDto.getId_alamedica())).thenReturn(Optional.of(alamedica));

        // Configura o QuartoDto com `Id_paciente` nulo
        quartoDto.setId_paciente(null);
    
        // Cria um quarto sem paciente
        Quarto quarto_atualizado = quartoDto.toQuarto(null, alamedica);
    
        // Simula a atualização do quarto
        when(service.atualizarQuarto(Mockito.anyInt(), Mockito.any(Quarto.class))).thenReturn(Optional.of(quarto_atualizado));
    
        ResponseEntity<Quarto> response = controller.update(1, quartoDto); // Chama o método PUT com ID do quarto
    
        // Verifica se o quarto foi atualizado corretamente
        assertEquals(quarto_atualizado, response.getBody());
    }

}
