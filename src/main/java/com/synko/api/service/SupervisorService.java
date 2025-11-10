package com.synko.api.service;

import com.synko.api.model.Rota;
import com.synko.api.model.Supervisor;
import com.synko.api.repository.RotaRepository;
import com.synko.api.repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class SupervisorService {

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private GerenteService gerenteService; // Para validar a FK gerenteId

    @Autowired
    private RotaRepository rotaRepository; // Para verificar deleção

    public String createSupervisor(Supervisor supervisor) throws ExecutionException, InterruptedException {
        // Validação de unicidade (CPF)
        if (supervisor.getCpf() == null || supervisor.getCpf().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        Supervisor existenteCpf = supervisorRepository.findByCpf(supervisor.getCpf());
        if (existenteCpf != null) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        // Validação de Chave Estrangeira (FK)
        gerenteService.getGerenteById(supervisor.getGerenteId());

        return supervisorRepository.save(supervisor);
    }

    public Supervisor getSupervisorById(String id) throws ExecutionException, InterruptedException {
        Supervisor supervisor = supervisorRepository.findById(id);
        if (supervisor == null) {
            throw new RuntimeException("Supervisor não encontrado com ID: " + id);
        }
        return supervisor;
    }

    public List<Supervisor> getAllSupervisores() throws ExecutionException, InterruptedException {
        return supervisorRepository.findAll();
    }

    public List<Supervisor> getSupervisoresByGerenteId(String gerenteId) throws ExecutionException, InterruptedException {
        gerenteService.getGerenteById(gerenteId); // Valida se o gerente existe
        return supervisorRepository.findAllByGerenteId(gerenteId);
    }

    public String updateSupervisor(String id, Supervisor supervisor) throws ExecutionException, InterruptedException {
        Supervisor existente = getSupervisorById(id); // Valida se existe

        // Validação de CPF:
        if (supervisor.getCpf() != null && !supervisor.getCpf().equals(existente.getCpf())) {
            Supervisor outroComCpf = supervisorRepository.findByCpf(supervisor.getCpf());
            if (outroComCpf != null && !outroComCpf.getId().equals(id)) {
                throw new IllegalArgumentException("Este CPF já está cadastrado em outro supervisor.");
            }
        }

        // Validação de FK
        gerenteService.getGerenteById(supervisor.getGerenteId());

        supervisor.setId(id);
        supervisor.setDataCadastro(existente.getDataCadastro()); // Preserva data original
        return supervisorRepository.save(supervisor);
    }

    public void deleteSupervisor(String id) throws ExecutionException, InterruptedException {
        getSupervisorById(id); // Valida se existe

        // 1. Verifica se existem Rotas vinculadas
        List<Rota> rotas = rotaRepository.findAllBySupervisorId(id);
        if (rotas != null && !rotas.isEmpty()) {
            throw new IllegalStateException(
                    "Não é possível excluir este supervisor pois ele possui " +
                            rotas.size() + " rota(s) vinculadas."
            );
        }

        // 2. Deleta o supervisor
        supervisorRepository.delete(id);
    }
}