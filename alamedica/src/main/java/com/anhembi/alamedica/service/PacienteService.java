package com.anhembi.alamedica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anhembi.alamedica.model.Paciente;
import com.anhembi.alamedica.model.Quarto;
import com.anhembi.alamedica.repository.PacienteRepository;
import com.anhembi.alamedica.repository.QuartoRepository;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repo;

    @Autowired
    private QuartoRepository quarto_repo;

    // get
    public List<Paciente> getAllPacientes(){
        return (List<Paciente>) repo.findAll();
    }

    // get por id
    public Optional<Paciente> getPacienteById(Integer id) {
        return repo.findById(id);
    }

    // post
    public Optional<Paciente> cadastrarPaciente(Paciente paciente){
        if (paciente.getId() != null){
            return Optional.empty();
        }

        // Regra: O paciente deve iniciar com alta = false
        paciente.setInternado(false);

        // Regra: Verificar se o quarto está disponível
        if (paciente.getQuarto() != null) {
            Optional<Quarto> quartoOptional = quarto_repo.findById(paciente.getQuarto().getId());
            if (quartoOptional.isPresent()) {
                Quarto quarto = quartoOptional.get();
                if (quarto.getPaciente() != null) {
                    // Retornar vazio se o quarto já está ocupado por outro paciente
                    return Optional.empty();
                }
                // Associar o paciente ao quarto
                quarto.setPaciente(paciente);
                paciente.setQuarto(quarto);
            } else {
                // Retornar vazio se o quarto não existe
                return Optional.empty();
            }
        }

        return Optional.of(repo.save(paciente));
    }

    // put: atualizar os dados do paciente, com possibilidade de troca de quarto
    public Optional<Paciente> atualizarPaciente(Integer id, Paciente paciente_atualizado){

        Optional<Paciente> paciente_optional = repo.findById(id);

        if (paciente_optional.isEmpty()){
            return Optional.empty();
        }

        Paciente paciente_existente = paciente_optional.get();

        // atualização dos dados

        paciente_existente.setNome(paciente_atualizado.getNome());
        paciente_existente.setInternado(paciente_atualizado.getInternado());
        paciente_existente.setData_de_nascimento(paciente_atualizado.getData_de_nascimento());
        paciente_existente.setEnfermidade(paciente_atualizado.getEnfermidade());
        paciente_existente.setAlergia(paciente_atualizado.getAlergia());

        // regra de verificação de troca de quarto, se necessário (se o usuário quer mudar o paciente de quarto na requisição)

        if (paciente_atualizado.getQuarto() != null){

            // ve se tem um quarto na requisição e se tiver pega ele no banco de dados e coloca numa variável nova
            Optional<Quarto> novoQuartoOptional = quarto_repo.findById(paciente_atualizado.getQuarto().getId());

            if (novoQuartoOptional.isPresent()){
                Quarto novo_quarto = novoQuartoOptional.get();

                // ve se o quarto solicitado já possui um paciente ou não (se o quarto está ocupado)
                if (novo_quarto.getPaciente() == null || novo_quarto.getPaciente().getId().equals(id)){
                    
                    // desassocia o paciente do quarto antigo se ele existir, altera o quarto para vazio e salva ele no banco de dados
                    if (paciente_existente.getQuarto() != null){
                        Quarto quarto_antigo = paciente_existente.getQuarto();
                        quarto_antigo.setPaciente(null);
                        quarto_repo.save(quarto_antigo);
                    }

                    // associar o novo quarto ao paciente
                    novo_quarto.setPaciente(paciente_existente);
                    paciente_existente.setQuarto(novo_quarto);
                } else {
                    // caso o novo quarto esteja ocupado por um paciente, retorna vazio (não é possível trocar de quarto)
                    return Optional.empty();
                }
            } else {
                // retorna vazio caso o quarto novo não exista
                return Optional.empty();
            }
        }
        // atualiza os dados do paciente com sucesso
        return Optional.of(repo.save(paciente_existente));
    }

     // DELETE: Remover um paciente, liberando o quarto, se ocupado
     public boolean deletarPaciente(Integer id) {

        Optional<Paciente> pacienteOptional = repo.findById(id);

        if (pacienteOptional.isEmpty()) {
            return false;
        }
        
        Paciente paciente = pacienteOptional.get();

        // Regra: Desocupar o quarto ao deletar o paciente
        if (paciente.getQuarto() != null) {
            Quarto quarto = paciente.getQuarto();
            quarto.setPaciente(null);
            quarto_repo.save(quarto);
        }

        repo.deleteById(id);
        return true;
    }
}
