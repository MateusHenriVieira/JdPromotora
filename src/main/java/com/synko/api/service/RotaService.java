package com.synko.api.service;

import com.synko.api.model.Corretor;
import com.synko.api.model.Rota;
import com.synko.api.repository.CorretorRepository;
import com.synko.api.repository.RotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RotaService {

    @Autowired
    private RotaRepository rotaRepository;

    @Autowired
    private SupervisorService supervisorService; // Para validar a FK supervisorId

    @Autowired
    private CorretorRepository corretorRepository; // Para verificar deleção

    public String createRota(Rota rota) throws ExecutionException, InterruptedException {
        // Validação de Chave Estrangeira (FK)
        supervisorService.getSupervisorById(rota.getSupervisorId());

        return rotaRepository.save(rota);
    }

    public Rota getRotaById(String id) throws ExecutionException, InterruptedException {
        Rota rota = rotaRepository.findById(id);
        if (rota == null) {
            throw new RuntimeException("Rota não encontrada com ID: " + id);
        }
        return rota;
    }

    public List<Rota> getAllRotas() throws ExecutionException, InterruptedException {
        return rotaRepository.findAll();
    }

    public List<Rota> getRotasBySupervisorId(String supervisorId) throws ExecutionException, InterruptedException {
        supervisorService.getSupervisorById(supervisorId); // Valida se o supervisor existe
        return rotaRepository.findAllBySupervisorId(supervisorId);
    }

    public String updateRota(String id, Rota rota) throws ExecutionException, InterruptedException {
        Rota existente = getRotaById(id); // Valida se existe

        // Validação de FK
        supervisorService.getSupervisorById(rota.getSupervisorId());

        rota.setId(id);
        rota.setDataCadastro(existente.getDataCadastro()); // Preserva data original
        return rotaRepository.save(rota);
    }

    public void deleteRota(String id) throws ExecutionException, InterruptedException {
        getRotaById(id); // Valida se existe

        // 1. Verifica se existem Corretores vinculados
        // (Lógica de deleção atualizada)

        List<Corretor> corretores = corretorRepository.findAllByRotaId(id);
        if (corretores != null && !corretores.isEmpty()) {
            throw new IllegalStateException(
                    "Não é possível excluir esta rota pois ela possui " +
                            corretores.size() + " corretor(es) vinculados."
            );
        }

        // 2. Deleta a rota
        rotaRepository.delete(id);
    }
}