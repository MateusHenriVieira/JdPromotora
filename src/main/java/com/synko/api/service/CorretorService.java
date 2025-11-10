package com.synko.api.service;

import com.synko.api.model.Corretor;
import com.synko.api.repository.CorretorRepository;
import com.synko.api.service.EmpresaService;
import com.synko.api.service.RotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CorretorService {

    @Autowired
    private CorretorRepository corretorRepository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private RotaService rotaService; // Injetado para validar a FK

    public String createCorretor(Corretor corretor) throws ExecutionException, InterruptedException {
        // Validação de unicidade (CPF/CNPJ)
        if (corretor.getCpfCnpj() == null || corretor.getCpfCnpj().isEmpty()) {
            throw new IllegalArgumentException("CPF/CNPJ é obrigatório");
        }
        Corretor existente = corretorRepository.findByCpfCnpj(corretor.getCpfCnpj());
        if (existente != null) {
            throw new IllegalArgumentException("CPF/CNPJ já cadastrado");
        }

        // Validação de Chaves Estrangeiras (FK)
        empresaService.getEmpresaById(corretor.getEmpresaId());
        rotaService.getRotaById(corretor.getRotaId()); // <-- Validação adicionada

        return corretorRepository.save(corretor);
    }

    public Corretor getCorretorById(String id) throws ExecutionException, InterruptedException {
        Corretor corretor = corretorRepository.findById(id);
        if (corretor == null) {
            throw new RuntimeException("Corretor não encontrado com ID: " + id);
        }
        return corretor;
    }

    public Corretor getCorretorByCpf(String cpf) throws ExecutionException, InterruptedException {
        Corretor corretor = corretorRepository.findByCpfCnpj(cpf);
        if (corretor == null) {
            throw new RuntimeException("Corretor não encontrado com CPF/CNPJ: " + cpf);
        }
        return corretor;
    }

    public List<Corretor> getAllCorretores() throws ExecutionException, InterruptedException {
        return corretorRepository.findAll();
    }

    public List<Corretor> getCorretoresByEmpresaId(String empresaId) throws ExecutionException, InterruptedException {
        empresaService.getEmpresaById(empresaId); // Valida se a empresa existe
        return corretorRepository.findAllByEmpresaId(empresaId);
    }

    public String updateCorretor(String id, Corretor corretor) throws ExecutionException, InterruptedException {
        Corretor existente = getCorretorById(id); // Valida se existe

        // Validação de CPF/CNPJ
        if (corretor.getCpfCnpj() != null && !corretor.getCpfCnpj().equals(existente.getCpfCnpj())) {
            Corretor outroComCpf = corretorRepository.findByCpfCnpj(corretor.getCpfCnpj());
            if (outroComCpf != null && !outroComCpf.getId().equals(id)) {
                throw new IllegalArgumentException("Este CPF/CNPJ já está cadastrado em outro corretor.");
            }
        }

        // Validar FKs
        empresaService.getEmpresaById(corretor.getEmpresaId());
        rotaService.getRotaById(corretor.getRotaId()); // <-- Validação adicionada

        corretor.setId(id);
        corretor.setDataCadastro(existente.getDataCadastro()); // Preserva data original
        return corretorRepository.save(corretor);
    }

    public void deleteCorretor(String id) throws ExecutionException, InterruptedException {
        getCorretorById(id); // Valida se existe
        corretorRepository.delete(id);
    }
}