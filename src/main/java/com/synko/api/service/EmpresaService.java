package com.synko.api.service;

import com.synko.api.model.Corretor;
import com.synko.api.model.Empresa;
import com.synko.api.model.Gerente;
import com.synko.api.repository.CorretorRepository;
import com.synko.api.repository.EmpresaRepository;
import com.synko.api.repository.GerenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private CorretorRepository corretorRepository; // Injetado para verificar deleção

    @Autowired
    private GerenteRepository gerenteRepository; // Injetado para verificar deleção

    public String createEmpresa(Empresa empresa) throws ExecutionException, InterruptedException {
        // Validações
        if (empresa.getCnpj() == null || empresa.getCnpj().isEmpty()) {
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }

        // Verifica se CNPJ já existe
        Empresa existente = empresaRepository.findByCnpj(empresa.getCnpj());
        if (existente != null) {
            throw new IllegalArgumentException("CNPJ já cadastrado");
        }

        return empresaRepository.save(empresa);
    }

    public Empresa getEmpresaById(String id) throws ExecutionException, InterruptedException {
        Empresa empresa = empresaRepository.findById(id);
        if (empresa == null) {
            throw new RuntimeException("Empresa não encontrada com ID: " + id);
        }
        return empresa;
    }

    public List<Empresa> getAllEmpresas() throws ExecutionException, InterruptedException {
        return empresaRepository.findAll();
    }

    /**
     * Atualiza uma empresa com validação de CNPJ duplicado.
     */
    public String updateEmpresa(String id, Empresa empresa) throws ExecutionException, InterruptedException {
        Empresa existente = getEmpresaById(id); // Valida se a empresa existe

        // Validação de CNPJ:
        // Verifica se o CNPJ foi alterado e se o novo CNPJ já pertence a outra empresa
        if (empresa.getCnpj() != null && !empresa.getCnpj().equals(existente.getCnpj())) {
            Empresa outraEmpresaComCnpj = empresaRepository.findByCnpj(empresa.getCnpj());
            if (outraEmpresaComCnpj != null && !outraEmpresaComCnpj.getId().equals(id)) {
                throw new IllegalArgumentException("Este CNPJ já está cadastrado em outra empresa.");
            }
        }

        empresa.setId(id);
        empresa.setDataCadastro(existente.getDataCadastro()); // Preserva a data de cadastro original
        return empresaRepository.save(empresa);
    }

    /**
     * Deleta uma empresa após verificar se ela não possui "filhos" (Corretores ou Gerentes).
     */
    public void deleteEmpresa(String id) throws ExecutionException, InterruptedException {
        getEmpresaById(id); // Valida se a empresa existe

        // TODO: Lógica de deleção em cascata

        // 1. Verifica se existem Corretores vinculados
        List<Corretor> corretores = corretorRepository.findAllByEmpresaId(id);
        if (corretores != null && !corretores.isEmpty()) {
            throw new IllegalStateException(
                    "Não é possível excluir esta empresa pois ela possui " +
                            corretores.size() + " corretor(es) vinculados."
            );
        }

        // 2. Verifica se existem Gerentes vinculados
        List<Gerente> gerentes = gerenteRepository.findAllByEmpresaId(id);
        if (gerentes != null && !gerentes.isEmpty()) {
            throw new IllegalStateException(
                    "Não é possível excluir esta empresa pois ela possui " +
                            gerentes.size() + " gerente(s) vinculados."
            );
        }

        // Se passou em ambas as verificações, pode deletar.
        empresaRepository.delete(id);
    }

    public List<Empresa> searchEmpresas(String nome, String cnpj, String token)
            throws ExecutionException, InterruptedException {
        return empresaRepository.search(nome, cnpj, token);
    }
}