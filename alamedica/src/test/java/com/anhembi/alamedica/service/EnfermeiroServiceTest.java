package com.anhembi.alamedica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.repository.AlamedicaRepository;
import com.anhembi.alamedica.repository.EnfermeiroRepository;

@ExtendWith(MockitoExtension.class)
public class EnfermeiroServiceTest {

    @InjectMocks
    private EnfermeiroService service;

    @Mock
    private EnfermeiroRepository repo;

    @Mock
    private AlamedicaRepository alamedica_repo;

    private Alamedica alaMedica;
    private Enfermeiro enfermeiro1;
    private Enfermeiro enfermeiro2;

    @BeforeEach
    public void setUp() {
        alaMedica = new Alamedica(1, 2, Collections.emptyList(), Collections.emptyList());
        
        enfermeiro1 = new Enfermeiro(1, "Douglas", alaMedica);
        enfermeiro2 = new Enfermeiro(2, "Leonardo", alaMedica);
    }

    //Teste getAllEnfermeiros()
    @Test
    @DisplayName("Deve retornar todos os enfermeiros cadastrados")
    public void retornarTodosEnfermeiros_RetornaTodosEnfermeiros() {

        when(repo.findAll()).thenReturn(List.of(enfermeiro1, enfermeiro2));

        List<Enfermeiro> response = service.getAllEnfermeiros();

        assertEquals(2, response.size());

        // Verifica se o conteúdo está correto
        assertEquals(enfermeiro1, response.get(0));
        assertEquals(enfermeiro2, response.get(1));
    }

    //Teste getById()
    @Test
    @DisplayName("Deve retornar o enfermeiro especificado pelo Id")
    public void retornarEnfermeiroPorId_RetornaEnfermeiroPorId() {

        when(repo.findById(1)).thenReturn(Optional.of(enfermeiro1));

        Optional<Enfermeiro> response = service.getEnfermeiroPorId(1);

        assertEquals(enfermeiro1, response.get());
    }

    //Teste Post
    //teste sucesso cria optional enfermeiro
    @Test
    @DisplayName("Deve criar um enfermeiro com sucesso")
    public void cadastrarEnfermeiro_EnfermeiroValido_CriaEnfermeiro() {

        //tem que setar o id do enfermeiro para null pois ele só cria um enfermeiro quando o id é null
        enfermeiro1.setId(null);

        when(alamedica_repo.findById(enfermeiro1.getAlaMedica().getId())).thenReturn(Optional.of(alaMedica));

        when(repo.save(enfermeiro1)).thenReturn(enfermeiro1);

        Optional<Enfermeiro> response = service.cadastrarEnfermeiro(enfermeiro1);

        assertEquals(enfermeiro1, response.get());
    }

    //teste falha id_enfermeiro null
    @Test
    @DisplayName("Não deve criar um enfermeiro com ID não nulo")
    public void cadastrarEnfermeiro_IdNaoNulo_RetornaOptionalEmpty() {

        Optional<Enfermeiro> response = service.cadastrarEnfermeiro(enfermeiro1);

        assertTrue(response.isEmpty());
    }

    //teste falha nome enfermeiro null 
    @Test
    @DisplayName("Não deve criar um enfermeiro com nome nulo")
    public void cadastrarEnfermeiro_NomeNulo_RetornaOptionalEmpty() {

        enfermeiro1.setId(null);
        enfermeiro1.setNome(null);

        Optional<Enfermeiro> result = service.cadastrarEnfermeiro(enfermeiro1);

        assertTrue(result.isEmpty());
    }

    //teste falha nome enfermeiro com espaços vazios
    @Test
    @DisplayName("Não deve criar um enfermeiro com nome vazio")
    public void cadastrarEnfermeiro_NomeVazio_RetornaOptionalEmpty() {

        enfermeiro1.setId(null);
        enfermeiro1.setNome("   ");

        Optional<Enfermeiro> result = service.cadastrarEnfermeiro(enfermeiro1);

        assertTrue(result.isEmpty());
    }

    //teste falha alaMedica invalida retorna optional empty 
    @Test
    @DisplayName("Não deve criar um enfermeiro com ala médica inválida")
    public void cadastrarEnfermeiro_AlaMedicaInvalida_RetornaOptionalEmpty() {

        enfermeiro1.setId(null);

        when(alamedica_repo.findById(enfermeiro1.getAlaMedica().getId())).thenReturn(Optional.empty());

        Optional<Enfermeiro> result = service.cadastrarEnfermeiro(enfermeiro1);

        assertTrue(result.isEmpty());
    }

    //teste falha alaMedica cheia (tem mais de 9 enfermeiros)
    @Test
    @DisplayName("Não deve criar um enfermeiro com ala médica cheia")
    public void cadastrarEnfermeiro_AlaMedicaCheia_RetornaOptionalEmpty() {

        enfermeiro1.setId(null);

        // Configura a ala médica com 9 enfermeiros
        alaMedica.setEnfermeiros(List.of(
                new Enfermeiro(), new Enfermeiro(), new Enfermeiro(),
                new Enfermeiro(), new Enfermeiro(), new Enfermeiro(),
                new Enfermeiro(), new Enfermeiro(), new Enfermeiro()
        ));

        when(alamedica_repo.findById(1)).thenReturn(Optional.of(alaMedica));

        Optional<Enfermeiro> result = service.cadastrarEnfermeiro(enfermeiro1);

        assertTrue(result.isEmpty());
    }

    //Testes Put
    //teste sucesso atualizar nome enfermeiro
    // @Test
    // @DisplayName("Deve atualizar apenas o nome do enfermeiro")
    // public void atualizarEnfermeiro_AtualizaApenasNome_AtualizaEnfermeiro() {

    //     Enfermeiro enfermeiroAtualizado = new Enfermeiro();
    //     enfermeiroAtualizado.setNome("Carlos");

    //     when(repo.findById(1)).thenReturn(Optional.of(enfermeiro1));

    //     when(repo.save(enfermeiro1)).thenReturn(enfermeiro1);

    //     Optional<Enfermeiro> result = service.atualizarEnfermeiro(1, enfermeiroAtualizado);

    //     assertEquals("Carlos", result.get().getNome());
    // }
    //teste sucesso atualizar ala medica do enfermeiro
    //teste sucesso atualizar ambas
    //teste falha id do enfermeiro não encontrado no BD
    //teste falha nome vazio
    //teste falha ala medica null
}
