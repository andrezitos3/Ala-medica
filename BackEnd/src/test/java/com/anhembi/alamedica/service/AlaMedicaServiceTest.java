package com.anhembi.alamedica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.AlamedicaRepository;

@ExtendWith(MockitoExtension.class)
public class AlaMedicaServiceTest {

    @InjectMocks
    private AlamedicaService service;

    @Mock
    private AlamedicaRepository repo;

    private Alamedica alaMedica1;
    private Alamedica alaMedica2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        alaMedica1 = new Alamedica();
        alaMedica1.setId(1);
        alaMedica1.setAndar(1);
        alaMedica1.setQuartos(new ArrayList<>());
        alaMedica1.setEnfermeiros(new ArrayList<>());

        alaMedica2 = new Alamedica();
        alaMedica2.setId(2);
        alaMedica2.setAndar(2);
        alaMedica2.setQuartos(new ArrayList<>());
        alaMedica2.setEnfermeiros(new ArrayList<>());
    }

    @Test
    @DisplayName("Deve retornar todas as alas médicas")
    public void getAlasMedicas_DeveRetornarListaDeAlas() {
        List<Alamedica> alas = List.of(alaMedica1, alaMedica2);
        when(repo.findAll()).thenReturn(alas);

        List<Alamedica> resultado = service.getAlasMedicas();

        assertEquals(2, resultado.size(), "Deve retornar duas alas médicas");
        verify(repo, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma ala médica pelo ID")
    public void getAlaMedica_DeveRetornarAlaPorId() {
        when(repo.findById(1)).thenReturn(Optional.of(alaMedica1));

        Optional<Alamedica> resultado = service.getAlaMedica(1);

        assertTrue(resultado.isPresent(), "A ala médica deve ser encontrada");
        assertEquals(alaMedica1, resultado.get(), "A ala médica deve ser a esperada");
        verify(repo, times(1)).findById(1);
    }

    @Test
    @DisplayName("Deve retornar vazio quando a ala médica não existir")
    public void getAlaMedica_DeveRetornarVazio() {
        when(repo.findById(3)).thenReturn(Optional.empty());

        Optional<Alamedica> resultado = service.getAlaMedica(3);

        assertTrue(resultado.isEmpty(), "A ala médica não deve ser encontrada");
        verify(repo, times(1)).findById(3);
    }

    @Test
    @DisplayName("Deve criar uma nova ala médica")
    public void criarAlaMedica_DeveCriarNovaAla() {
        alaMedica1.setId(null); // Nova ala médica sem ID

        when(repo.existsByAndar(alaMedica1.getAndar())).thenReturn(false);
        when(repo.save(alaMedica1)).thenReturn(alaMedica1);

        Optional<Alamedica> resultado = service.criarAlaMedica(alaMedica1);

        assertTrue(resultado.isPresent(), "A ala médica deve ser criada");
        assertEquals(alaMedica1, resultado.get(), "A ala médica criada deve ser retornada");
        verify(repo, times(1)).existsByAndar(alaMedica1.getAndar());
        verify(repo, times(1)).save(alaMedica1);
    }

    @Test
    @DisplayName("Não deve criar uma ala médica com ID já definido")
    public void criarAlaMedica_ComIdDefinido_DeveRetornarVazio() {
        //when(repo.existsByAndar(alaMedica1.getAndar())).thenReturn(false);

        Optional<Alamedica> resultado = service.criarAlaMedica(alaMedica1);

        assertTrue(resultado.isEmpty(), "A ala médica não deve ser criada");
        verify(repo, never()).save(any(Alamedica.class));
    }

    @Test
    @DisplayName("Não deve criar uma ala médica quando o número do andar já existir")
    public void criarAlaMedica_AndarJaExistente_DeveRetornarVazio() {
        alaMedica1.setId(null); // Nova ala médica sem ID

        when(repo.existsByAndar(alaMedica1.getAndar())).thenReturn(true); // Simula que o andar já existe

        Optional<Alamedica> resultado = service.criarAlaMedica(alaMedica1);

        assertTrue(resultado.isEmpty(), "A ala médica não deve ser criada porque o andar já existe");
        verify(repo, times(1)).existsByAndar(alaMedica1.getAndar());
        verify(repo, never()).save(any(Alamedica.class));
    }

    @Test
    @DisplayName("Não deve criar uma ala médica com mais de 5 quartos")
    public void criarAlaMedica_ComMaisDeCincoQuartos_DeveRetornarVazio() {
        alaMedica1.setId(null); // Nova ala médica sem ID
        List<Quarto> quartos = new ArrayList<>();
        for (int i = 0; i < 6; i++) { // Adiciona 6 quartos
            quartos.add(new Quarto());
        }
        alaMedica1.setQuartos(quartos);

        when(repo.existsByAndar(alaMedica1.getAndar())).thenReturn(false); // O andar não existe

        Optional<Alamedica> resultado = service.criarAlaMedica(alaMedica1);

        assertTrue(resultado.isEmpty(), "A ala médica não deve ser criada porque possui mais de 5 quartos");
        verify(repo, never()).save(any(Alamedica.class));
    }

    @Test
    @DisplayName("Não deve criar uma ala médica com mais de 9 enfermeiros")
    public void criarAlaMedica_ComMaisDeNoveEnfermeiros_DeveRetornarVazio() {
        alaMedica1.setId(null); // Nova ala médica sem ID
        List<Enfermeiro> enfermeiros = new ArrayList<>();
        for (int i = 0; i < 10; i++) { // Adiciona 10 enfermeiros
            enfermeiros.add(new Enfermeiro());
        }
        alaMedica1.setEnfermeiros(enfermeiros);

        when(repo.existsByAndar(alaMedica1.getAndar())).thenReturn(false); // O andar não existe

        Optional<Alamedica> resultado = service.criarAlaMedica(alaMedica1);

        assertTrue(resultado.isEmpty(), "A ala médica não deve ser criada porque possui mais de 9 enfermeiros");
        verify(repo, never()).save(any(Alamedica.class));
    }

    @Test
    @DisplayName("Deve atualizar uma ala médica com sucesso")
    public void atualizarAlaMedica_DeveAtualizarComSucesso() {
        alaMedica1.setAndar(3);

        when(repo.findById(1)).thenReturn(Optional.of(alaMedica1));
        //when(repo.existsByAndar(3)).thenReturn(false);
        when(repo.save(alaMedica1)).thenReturn(alaMedica1);

        Optional<Alamedica> resultado = service.atualizarAlaMedica(1, alaMedica1);

        assertTrue(resultado.isPresent(), "A ala médica deve ser atualizada");
        assertEquals(3, resultado.get().getAndar(), "O andar deve ser atualizado");
        verify(repo, times(1)).findById(1);
        verify(repo, times(1)).save(alaMedica1);
    }

    @Test
    @DisplayName("Não deve atualizar uma ala médica inexistente")
    public void atualizarAlaMedica_AlaNaoExistente_DeveRetornarVazio() {
        Alamedica alaAtualizada = new Alamedica();
        alaAtualizada.setAndar(2);

        when(repo.findById(1)).thenReturn(Optional.empty());

        Optional<Alamedica> resultado = service.atualizarAlaMedica(1, alaAtualizada);

        assertTrue(resultado.isEmpty(), "A ala médica inexistente não deve ser atualizada");
        verify(repo, times(1)).findById(1);
        verify(repo, never()).save(any(Alamedica.class));
    }

    @Test
    @DisplayName("Não deve atualizar o andar se causar duplicação")
    public void atualizarAlaMedica_AndarDuplicado_DeveRetornarVazio() {
        Alamedica alaAtualizada = new Alamedica();
        alaAtualizada.setAndar(2);

        when(repo.findById(alaMedica1.getId())).thenReturn(Optional.of(alaMedica1));
        when(repo.existsByAndar(2)).thenReturn(true);

        Optional<Alamedica> resultado = service.atualizarAlaMedica(alaMedica1.getId(), alaAtualizada);

        assertTrue(resultado.isEmpty(), "A atualização não deve ocorrer se o andar já existir");
        verify(repo, times(1)).findById(alaMedica1.getId());
        verify(repo, times(1)).existsByAndar(2);
        verify(repo, never()).save(any(Alamedica.class));
    }

    @Test
    @DisplayName("Não deve atualizar a ala médica com mais de 5 quartos")
    public void atualizarAlaMedica_MaisDeCincoQuartos_DeveRetornarVazio() {
        Alamedica alaAtualizada = new Alamedica();
        List<Quarto> quartos = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            quartos.add(new Quarto());
        }
        alaAtualizada.setQuartos(quartos);

        when(repo.findById(alaMedica1.getId())).thenReturn(Optional.of(alaMedica1));

        Optional<Alamedica> resultado = service.atualizarAlaMedica(alaMedica1.getId(), alaAtualizada);

        assertTrue(resultado.isEmpty(), "A atualização não deve ocorrer com mais de 5 quartos");
        verify(repo, times(1)).findById(alaMedica1.getId());
        verify(repo, never()).save(any(Alamedica.class));
    }

    @Test
    @DisplayName("Não deve atualizar a ala médica com mais de 9 enfermeiros")
    public void atualizarAlaMedica_MaisDeNoveEnfermeiros_DeveRetornarVazio() {
        Alamedica alaAtualizada = new Alamedica();
        List<Enfermeiro> enfermeiros = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            enfermeiros.add(new Enfermeiro());
        }
        alaAtualizada.setEnfermeiros(enfermeiros);

        when(repo.findById(alaMedica1.getId())).thenReturn(Optional.of(alaMedica1));

        Optional<Alamedica> resultado = service.atualizarAlaMedica(alaMedica1.getId(), alaAtualizada);

        assertTrue(resultado.isEmpty(), "A atualização não deve ocorrer com mais de 9 enfermeiros");
        verify(repo, times(1)).findById(alaMedica1.getId());
        verify(repo, never()).save(any(Alamedica.class));
    }

    @Test
    @DisplayName("Deve deletar uma ala médica quando não tiver quartos ou enfermeiros associados")
    public void deletarAlaMedica_SemQuartosOuEnfermeiros_DeveDeletar() {
        when(repo.findById(1)).thenReturn(Optional.of(alaMedica1));

        boolean resultado = service.deletarAlaMedica(1);

        assertTrue(resultado, "A ala médica deve ser deletada");
        verify(repo, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Não deve deletar uma ala médica com quartos ou enfermeiros associados")
    public void deletarAlaMedica_ComQuartosOuEnfermeiros_DeveRetornarFalso() {

        List<Quarto> quartos = new ArrayList<>();
        quartos.add(new Quarto()); // Adiciona um quarto
        alaMedica1.setQuartos(quartos); // Define os quartos na ala médica

        when(repo.findById(1)).thenReturn(Optional.of(alaMedica1));

        boolean resultado = service.deletarAlaMedica(1);

        assertFalse(resultado, "A ala médica não deve ser deletada");
        verify(repo, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Não deve deletar uma ala médica quando ela não for encontrada")
    public void deletarAlaMedica_NaoEncontrada_DeveRetornarFalso() {
        when(repo.findById(1)).thenReturn(Optional.empty()); // Simula que a ala não foi encontrada

        boolean resultado = service.deletarAlaMedica(1);

        assertFalse(resultado, "A ala médica não deve ser deletada porque não foi encontrada");
        verify(repo, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Não deve deletar uma ala médica com enfermeiros associados")
    public void deletarAlaMedica_ComEnfermeiros_DeveRetornarFalso() {
        List<Enfermeiro> enfermeiros = new ArrayList<>();
        enfermeiros.add(new Enfermeiro()); // Adiciona um enfermeiro
        alaMedica1.setEnfermeiros(enfermeiros); // Define os enfermeiros na ala médica

        when(repo.findById(1)).thenReturn(Optional.of(alaMedica1));

        boolean resultado = service.deletarAlaMedica(1);

        assertFalse(resultado, "A ala médica não deve ser deletada porque possui enfermeiros associados");
        verify(repo, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Não deve deletar uma ala médica com quartos e enfermeiros associados")
    public void deletarAlaMedica_ComQuartosEEnfermeiros_DeveRetornarFalso() {
        // Configurando a ala médica com quartos e enfermeiros
        List<Quarto> quartos = new ArrayList<>();
        quartos.add(new Quarto()); // Adiciona um quarto

        List<Enfermeiro> enfermeiros = new ArrayList<>();
        enfermeiros.add(new Enfermeiro()); // Adiciona um enfermeiro

        alaMedica1.setQuartos(quartos);
        alaMedica1.setEnfermeiros(enfermeiros);

        when(repo.findById(1)).thenReturn(Optional.of(alaMedica1));

        boolean resultado = service.deletarAlaMedica(1);

        assertFalse(resultado, "A ala médica não deve ser deletada porque possui quartos e enfermeiros associados");
        verify(repo, never()).deleteById(anyInt());
    }
}
