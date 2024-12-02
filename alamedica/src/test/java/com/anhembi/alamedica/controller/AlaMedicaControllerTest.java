package com.anhembi.alamedica.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.anhembi.alamedica.dto.AlamedicaDTO;
import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Enfermeiro;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.EnfermeiroRepository;
import com.anhembi.alamedica.repository.QuartoRepository;
import com.anhembi.alamedica.service.AlamedicaService;

@ExtendWith(MockitoExtension.class)
public class AlaMedicaControllerTest {
    
    @InjectMocks
    private AlamedicaController controller;

    @Mock
    private AlamedicaService service;

    @Mock
    private EnfermeiroRepository enfermeiro_repo;

    @Mock
    private QuartoRepository quarto_repo;

    private Quarto quarto;
    private Enfermeiro enfermeiro;
    private Alamedica ala1;
    private Alamedica ala2;
    private AlamedicaDTO alaDto;

    @BeforeEach
    public void setUp() {
        ala1 = new Alamedica(1, 2, Collections.emptyList(), Collections.emptyList());
        ala2 = new Alamedica(2, 3, Collections.emptyList(), Collections.emptyList());
        alaDto = new AlamedicaDTO();
        quarto = new Quarto();
        enfermeiro = new Enfermeiro();
    }

    //Testes getAll
    @Test
    @DisplayName("Deve retornar todas as alas")
    public void RetornarTodasAlas_retornaTodasAsAlas() {
        
        when(service.getAlasMedicas()).thenReturn(List.of(ala1, ala2));

        ResponseEntity<List<Alamedica>> response = controller.getAll();

        assertEquals(2, response.getBody().size());
                
        // Verifica se o conteúdo está correto
        assertEquals(ala1, response.getBody().get(0));
        assertEquals(ala2, response.getBody().get(1));
    }

    //Testes getById
    @Test
    @DisplayName("Deve retornar a ala especificada pelo ID")
    public void RetornarAlaPeloId_IdValido_RetornaAlaPeloId() {

        when(service.getAlaMedica(1)).thenReturn(Optional.of(ala1));

        ResponseEntity<Alamedica> response = controller.getById(1);

        assertEquals(ala1, response.getBody());
    }

    @Test
    @DisplayName("Deve retornar NotFound se a Ala não for encontrada")
    public void RetornarAlaPeloId_IdInvalido_RetornaNotFound() {

        when(service.getAlaMedica(999)).thenReturn(Optional.empty());

        ResponseEntity<Alamedica> response = controller.getById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //Testes delete
    @Test
    @DisplayName("Deve verificaro caso de sucesso da função delete")
    public void DeletarAla_AlaValida_RetornaNoContent() {

        when(service.deletarAlaMedica(1)).thenReturn(true);

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificaro caso de falha da função delete")
    public void DeletarAla_AlaInvalida_RetornaBadRequest() {

        when(service.deletarAlaMedica(1)).thenReturn(false);

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //Testes Post
    //teste sucesso criar ala medica sem enfermeiros e quartos
    @Test
    @DisplayName("Deve criar uma ala médica vazia")
    void criarAlaMedica_SemEnfermeiroEQuarto_CriaAlaMedica() {

        // Configuração inicial
        ala1.setId(null);

        alaDto.setAndar(4);

        List<Quarto> quartos = new ArrayList<>();
        List<Enfermeiro> enfermeiros = new ArrayList<>();

        Alamedica alaNova = alaDto.toaAlamedica(quartos, enfermeiros);

        // Simulações dos repositórios
        when(service.criarAlaMedica(Mockito.any(Alamedica.class))).thenReturn(Optional.of(alaNova));

        // Execução do método
        ResponseEntity<Alamedica> response = controller.create(alaDto);

        // Verificações
        assertEquals(alaNova, response.getBody());
        verify(service, times(1)).criarAlaMedica(any());
    }

        //teste sucesso criar ala medica sem enfermeiros e com quartos
        @Test
        @DisplayName("Deve criar uma ala médica apenas com quartos")
        void criarAlaMedica_SemEnfermeiroEComQuarto_CriaAlaMedica() {
    
            // Configuração inicial
            ala1.setId(null);

            quarto.setId(1);

            List<Integer> quartosIds = List.of(1); // IDs dos quartos
            //List<Integer> enfermeirosIds = List.of(10, 11); // IDs dos enfermeiros
    
            alaDto.setAndar(4);
            alaDto.setQuartos_ids(quartosIds);
            alaDto.setEnfermeiros_ids(null);
    
            List<Quarto> quartos = new ArrayList<>();
            List<Enfermeiro> enfermeiros = new ArrayList<>();
    
            Alamedica alaNova = alaDto.toaAlamedica(quartos, enfermeiros);
    
            // Simulações dos repositórios
            when(service.criarAlaMedica(Mockito.any(Alamedica.class))).thenReturn(Optional.of(alaNova));
            when(quarto_repo.findById(1)).thenReturn(Optional.of(quarto));
    
            // Execução do método
            ResponseEntity<Alamedica> response = controller.create(alaDto);
    
            // Verificações
            assertEquals(alaNova, response.getBody());
            verify(service, times(1)).criarAlaMedica(any());
        }

        //teste sucesso criar ala medica com enfermeiros e sem quartos
        @Test
        @DisplayName("Deve criar uma ala médica apenas com quartos")
        void criarAlaMedica_ComEnfermeiroESemQuarto_CriaAlaMedica() {
    
            // Configuração inicial
            ala1.setId(null);

            enfermeiro.setId(10);

            List<Integer> enfermeirosIds = List.of(10); // IDs dos enfermeiros
    
            alaDto.setAndar(4);
            alaDto.setQuartos_ids(null);
            alaDto.setEnfermeiros_ids(enfermeirosIds);
    
            List<Quarto> quartos = new ArrayList<>();
            List<Enfermeiro> enfermeiros = new ArrayList<>();
    
            Alamedica alaNova = alaDto.toaAlamedica(quartos, enfermeiros);
    
            // Simulações dos repositórios
            when(service.criarAlaMedica(Mockito.any(Alamedica.class))).thenReturn(Optional.of(alaNova));
            when(enfermeiro_repo.findById(10)).thenReturn(Optional.of(enfermeiro));
    
            // Execução do método
            ResponseEntity<Alamedica> response = controller.create(alaDto);
    
            // Verificações
            assertEquals(alaNova, response.getBody());
            verify(service, times(1)).criarAlaMedica(any());
        }

}