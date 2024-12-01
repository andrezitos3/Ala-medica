package com.anhembi.alamedica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
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
    private Enfermeiro enfermeiroAtualizado;
    private Enfermeiro enfermeiro1;
    private Enfermeiro enfermeiro2;

    @BeforeEach
    public void setUp() {
        enfermeiroAtualizado = new Enfermeiro();

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
    @Test
    @DisplayName("Deve atualizar um enfermeiro com sucesso quando o enfermeiro e a ala médica forem encontrados")
    public void atualizarEnfermeiro_EnfermeiroEAlaMedicaEncontrados_RetornaEnfermeiroAtualizado() {
        // Configuração inicial
        enfermeiro1.setId(1);
        enfermeiro1.setAlaMedica(alaMedica);

        enfermeiroAtualizado.setNome("Kleber");
        enfermeiroAtualizado.setAlaMedica(alaMedica);

        List<Enfermeiro> enfermeiros = new ArrayList<>();
        enfermeiros.add(new Enfermeiro());
        enfermeiros.add(new Enfermeiro());

        alaMedica.setEnfermeiros(enfermeiros);

        // Simulações dos repositórios
        when(repo.findById(1)).thenReturn(Optional.of(enfermeiro1));
        when(alamedica_repo.findById(alaMedica.getId())).thenReturn(Optional.of(alaMedica));
        when(repo.save(enfermeiro1)).thenReturn(enfermeiro1);


        // Execução do método
        Optional<Enfermeiro> resultado = service.atualizarEnfermeiro(1, enfermeiroAtualizado);

        // Verificações
        assertTrue(resultado.isPresent(), "Enfermeiro deve ser atualizado");
        assertEquals("Kleber", resultado.get().getNome(), "O nome do enfermeiro deve ser atualizado");
        assertEquals(alaMedica, resultado.get().getAlaMedica(), "A ala médica do enfermeiro deve ser atualizada");
    }

    @Test
    @DisplayName("Deve retornar Optional.empty() quando o enfermeiro não for encontrado")
    public void atualizarEnfermeiro_EnfermeiroNaoEncontrado_RetornaOptionalEmpty() {
        // Configuração inicial
        Enfermeiro enfermeiroAtualizado = new Enfermeiro();
        enfermeiroAtualizado.setId(1);

        // Mocking
        when(repo.findById(1)).thenReturn(Optional.empty()); // Enfermeiro não encontrado

        // Execução
        Optional<Enfermeiro> resultado = service.atualizarEnfermeiro(1, enfermeiroAtualizado);

        // Verificações
        assertTrue(resultado.isEmpty(), "Se o enfermeiro não for encontrado, deve retornar Optional.empty()");
    }

    @Test
    @DisplayName("Deve retornar Optional.empty() quando a nova ala médica não for encontrada")
    public void atualizarEnfermeiro_NovaAlaNaoEncontrada_RetornaOptionalEmpty() {
        // Configuração inicial
        Enfermeiro enfermeiroExistente = new Enfermeiro();
        enfermeiroExistente.setId(1);
        enfermeiroExistente.setNome("João");

        Enfermeiro enfermeiroAtualizado = new Enfermeiro();
        enfermeiroAtualizado.setId(1);
        enfermeiroAtualizado.setNome("Carlos");
        Alamedica novaAlaMedica = new Alamedica();
        novaAlaMedica.setId(2);
        enfermeiroAtualizado.setAlaMedica(novaAlaMedica);

        // Mocking
        when(repo.findById(1)).thenReturn(Optional.of(enfermeiroExistente)); // Enfermeiro encontrado
        when(alamedica_repo.findById(2)).thenReturn(Optional.empty()); // Nova ala médica não encontrada

        // Execução
        Optional<Enfermeiro> resultado = service.atualizarEnfermeiro(1, enfermeiroAtualizado);

        // Verificações
        assertTrue(resultado.isEmpty(), "Se a nova ala médica não for encontrada, deve retornar Optional.empty()");
    }

    @Test
    @DisplayName("Deve retornar Optional.empty() quando a nova ala médica estiver cheia")
    public void atualizarEnfermeiro_AlaCheia_RetornaOptionalEmpty() {
        // Configuração inicial
        Enfermeiro enfermeiroExistente = new Enfermeiro();
        enfermeiroExistente.setId(1);
        enfermeiroExistente.setNome("João");

        Alamedica novaAlaMedica = new Alamedica();
        novaAlaMedica.setId(2);
        List<Enfermeiro> enfermeiros = new ArrayList<>(Collections.nCopies(10, new Enfermeiro())); // Ala cheia
        novaAlaMedica.setEnfermeiros(enfermeiros);

        Enfermeiro enfermeiroAtualizado = new Enfermeiro();
        enfermeiroAtualizado.setId(1);
        enfermeiroAtualizado.setNome("Carlos");
        enfermeiroAtualizado.setAlaMedica(novaAlaMedica);

        // Mocking
        when(repo.findById(1)).thenReturn(Optional.of(enfermeiroExistente)); // Enfermeiro encontrado
        when(alamedica_repo.findById(2)).thenReturn(Optional.of(novaAlaMedica)); // Nova ala médica encontrada

        // Execução
        Optional<Enfermeiro> resultado = service.atualizarEnfermeiro(1, enfermeiroAtualizado);

        // Verificações
        assertTrue(resultado.isEmpty(), "Se a nova ala médica estiver cheia, deve retornar Optional.empty()");
    }

    @Test
    @DisplayName("Deve deletar um enfermeiro com sucesso quando ele for encontrado")
    public void deletarEnfermeiro_EnfermeiroEncontrado_RetornaTrue() {
        // Configuração inicial
        Enfermeiro enfermeiroExistente = new Enfermeiro();
        enfermeiroExistente.setId(1);

        Alamedica alaMedica = new Alamedica();
        alaMedica.setId(1);
        List<Enfermeiro> enfermeiros = new ArrayList<>();
        enfermeiros.add(enfermeiroExistente);
        alaMedica.setEnfermeiros(enfermeiros);
        enfermeiroExistente.setAlaMedica(alaMedica);

        // Mocking
        when(repo.findById(1)).thenReturn(Optional.of(enfermeiroExistente)); // Enfermeiro encontrado
        when(alamedica_repo.save(Mockito.any(Alamedica.class))).thenReturn(alaMedica); // Mocking a atualização da ala médica

        // Execução
        boolean resultado = service.deletarEnfermeiro(1);

        // Verificações
        assertTrue(resultado, "Enfermeiro deve ser deletado com sucesso.");
        verify(repo, times(1)).deleteById(1); // Verifica se o enfermeiro foi excluído
        verify(alamedica_repo, times(1)).save(alaMedica); // Verifica se a ala médica foi salva
    }

    @Test
    @DisplayName("Deve retornar false quando o enfermeiro não for encontrado")
    public void deletarEnfermeiro_EnfermeiroNaoEncontrado_RetornaFalse() {
        // Mocking
        when(repo.findById(1)).thenReturn(Optional.empty()); // Enfermeiro não encontrado

        // Execução
        boolean resultado = service.deletarEnfermeiro(1);

        // Verificações
        assertFalse(resultado, "Deve retornar false quando o enfermeiro não for encontrado.");
    }
}
