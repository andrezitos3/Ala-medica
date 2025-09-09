package com.anhembi.alamedica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Alamedica;
import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.AlamedicaRepository;
import com.anhembi.alamedica.repository.PacienteRepository;
import com.anhembi.alamedica.repository.QuartoRepository;

@Service
public class QuartoService {

    @Autowired
    private QuartoRepository repo;

    @Autowired
    private PacienteRepository paciente_repo;

    @Autowired
    private AlamedicaRepository alamedica_repo;

    // get
    public List<Quarto> getAllQuartos(){
        return (List<Quarto>) repo.findAll();
    }

    // get por id especifico
    public Optional<Quarto> getQuartoById(Integer id){
        return repo.findById(id);
    }

    // post
    public Optional<Quarto> criarQuarto(Quarto quarto){
        if (quarto.getId() != null){
            return Optional.empty();
        }

        // verifica se o número do quarto é único
        if (repo.existsByNumero(quarto.getNumero())){
            return Optional.empty();
        }

        // regra: criar quarto sempre vazio (sem paciente)
        quarto.setPaciente(null);

         // Regra: A ala médica é obrigatória 
         if (quarto.getAlaMedica() == null || !alamedica_repo.existsById(quarto.getAlaMedica().getId())) {
            return Optional.empty();
        }

        return Optional.of(repo.save(quarto));
    }
    
    // put
    public Optional<Quarto> atualizarQuarto(Integer id, Quarto quarto_atualizado){

        Optional<Quarto> quarto_optional = repo.findById(id);

        if (quarto_optional.isEmpty()){
            return Optional.empty();
        }

        Quarto quarto_existente = quarto_optional.get();

        // verifica se o numero cadastrado não é igual ao número do quarto existente e se o numero cadastrado existe no banco de dados

        if (!quarto_existente.getNumero().equals(quarto_atualizado.getNumero()) && repo.existsByNumero(quarto_atualizado.getNumero())){
            return Optional.empty(); // quarto duplicado
        }

        quarto_existente.setNumero(quarto_atualizado.getNumero());

        // atualizar a ala médica, se houver a alteração (e ver se ela é diferente)

        if (quarto_atualizado.getAlaMedica() != null && !quarto_atualizado.getAlaMedica().equals(quarto_existente.getAlaMedica())){

            Optional<Alamedica> alaMedica_optional = alamedica_repo.findById(quarto_atualizado.getAlaMedica().getId());

            if (alaMedica_optional.isPresent()){

                if (alaMedica_optional.get().getQuartos().size() >= 5){
                    return Optional.empty(); // nao pode ter mais de 5 quartos
                }

                quarto_existente.setAlaMedica(alaMedica_optional.get());

            } else {
                return Optional.empty(); // ala médica nao foi encontrada
            }
        } else if (quarto_atualizado.getAlaMedica() != null && quarto_atualizado.getAlaMedica().equals(quarto_existente.getAlaMedica())){

            quarto_existente.setAlaMedica(quarto_atualizado.getAlaMedica());

        }

        // atualizar o paciente assosciado, se houver a alteração

        if (quarto_atualizado.getPaciente() != null){
            
            Optional<Paciente> paciente_optional = paciente_repo.findById(quarto_atualizado.getPaciente().getId());

            if (paciente_optional.isPresent()){

                Paciente paciente_atualizado = paciente_optional.get();

                // Verifica se o paciente já está associado a outro quarto
                if (paciente_atualizado.getQuarto() != null && !paciente_atualizado.getQuarto().getId().equals(id)) {
                    return Optional.empty(); // Paciente já alocado em outro quarto
                }


                quarto_existente.setPaciente(paciente_atualizado);

            } else {

                return Optional.empty(); // paciente não foi encontrado
            }
        } else {

            // desassociar paciente caso tenha remoção (requisição nula no campo do paciente)
            if (quarto_existente.getPaciente() != null) {
                quarto_existente.getPaciente().setQuarto(null); // Remove a associação inversa
                quarto_existente.setPaciente(null);
            }

        }

        return Optional.of(repo.save(quarto_existente));

    }

    // delete - removendo o quarto pelo ID e desassociando o paciente, se houver

    public boolean deletarQuarto(Integer id){

        Optional<Quarto> quarto_optional = repo.findById(id);

        if (quarto_optional.isEmpty()){

            return false;

        }

        Quarto quarto = quarto_optional.get();

        // desassociar o paciente do quarto (se tiver) antes de excluir o quarto

        if (quarto.getPaciente() != null){

            Paciente paciente = quarto.getPaciente();

            paciente.setQuarto(null);

            paciente_repo.save(paciente);

        }

        if (quarto.getAlaMedica() != null){

            quarto.setAlaMedica(null); // quarto ainda associado a uma ala médica
        }

        repo.deleteById(id);

        return true;

    }

}
