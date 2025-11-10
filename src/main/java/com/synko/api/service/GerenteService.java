package com.synko.api.service;

import com.synko.api.model.Gerente;
import com.synko.api.model.MetaGerente;
import com.synko.api.model.Supervisor;
import com.synko.api.repository.GerenteRepository;
import com.synko.api.repository.MetaGerenteRepository;
import com.synko.api.repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class GerenteService {

    @Autowired
    private GerenteRepository gerenteRepository;

    @Autowired
    private MetaGerenteRepository metaGerenteRepository;

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private EmpresaService empresaService; // Para validar a FK empresaId

    // --- LÓGICA DE GERENTE ---

    public String createGerente(Gerente gerente) throws ExecutionException, InterruptedException {
        // Validação de unicidade (CPF)
        if (gerente.getCpf() == null || gerente.getCpf().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        Gerente existenteCpf = gerenteRepository.findByCpf(gerente.getCpf());
        if (existenteCpf != null) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        // Validação de Chave Estrangeira (FK)
        empresaService.getEmpresaById(gerente.getEmpresaId());

        return gerenteRepository.save(gerente);
    }

    public Gerente getGerenteById(String id) throws ExecutionException, InterruptedException {
        Gerente gerente = gerenteRepository.findById(id);
        if (gerente == null) {
            throw new RuntimeException("Gerente não encontrado com ID: " + id);
        }
        return gerente;
    }

    public List<Gerente> getAllGerentes() throws ExecutionException, InterruptedException {
        return gerenteRepository.findAll();
    }

    public List<Gerente> getGerentesByEmpresaId(String empresaId) throws ExecutionException, InterruptedException {
        empresaService.getEmpresaById(empresaId); // Valida se a empresa existe
        return gerenteRepository.findAllByEmpresaId(empresaId);
    }

    public String updateGerente(String id, Gerente gerente) throws ExecutionException, InterruptedException {
        Gerente existente = getGerenteById(id); // Valida se existe

        // Validação de CPF:
        if (gerente.getCpf() != null && !gerente.getCpf().equals(existente.getCpf())) {
            Gerente outroComCpf = gerenteRepository.findByCpf(gerente.getCpf());
            if (outroComCpf != null && !outroComCpf.getId().equals(id)) {
                throw new IllegalArgumentException("Este CPF já está cadastrado em outro gerente.");
            }
        }

        // Validação de FK
        empresaService.getEmpresaById(gerente.getEmpresaId());

        gerente.setId(id);
        gerente.setDataCadastro(existente.getDataCadastro()); // Preserva data original
        return gerenteRepository.save(gerente);
    }

    public void deleteGerente(String id) throws ExecutionException, InterruptedException {
        getGerenteById(id); // Valida se existe

        // 1. Verifica se existem Supervisores vinculados
        List<Supervisor> supervisores = supervisorRepository.findAllByGerenteId(id);
        if (supervisores != null && !supervisores.isEmpty()) {
            throw new IllegalStateException(
                    "Não é possível excluir este gerente pois ele possui " +
                            supervisores.size() + " supervisor(es) vinculados."
            );
        }

        // 2. Deleta as Metas filhas (limpeza)
        List<MetaGerente> metas = metaGerenteRepository.findAllByGerenteId(id);
        for (MetaGerente meta : metas) {
            metaGerenteRepository.delete(meta.getId());
        }

        // 3. Deleta o gerente
        gerenteRepository.delete(id);
    }

    // --- LÓGICA DE META_GERENTE ---

    public String createMeta(String gerenteId, MetaGerente meta) throws ExecutionException, InterruptedException {
        getGerenteById(gerenteId); // Valida se o gerente pai existe
        meta.setGerenteId(gerenteId);
        return metaGerenteRepository.save(meta);
    }

    public List<MetaGerente> getMetasByGerenteId(String gerenteId) throws ExecutionException, InterruptedException {
        getGerenteById(gerenteId); // Valida se o gerente pai existe
        return metaGerenteRepository.findAllByGerenteId(gerenteId);
    }

    public MetaGerente getMetaById(String metaId) throws ExecutionException, InterruptedException {
        MetaGerente meta = metaGerenteRepository.findById(metaId);
        if (meta == null) {
            throw new RuntimeException("Meta não encontrada com ID: " + metaId);
        }
        return meta;
    }

    public String updateMeta(String gerenteId, String metaId, MetaGerente meta) throws ExecutionException, InterruptedException {
        getGerenteById(gerenteId); // Valida gerente
        MetaGerente existente = getMetaById(metaId); // Valida meta

        if (!existente.getGerenteId().equals(gerenteId)) {
            throw new IllegalArgumentException("Esta meta não pertence ao gerente informado.");
        }

        meta.setId(metaId);
        meta.setGerenteId(gerenteId);
        meta.setDataCadastro(existente.getDataCadastro());
        return metaGerenteRepository.save(meta);
    }

    public void deleteMeta(String gerenteId, String metaId) throws ExecutionException, InterruptedException {
        MetaGerente existente = getMetaById(metaId); // Valida meta

        if (!existente.getGerenteId().equals(gerenteId)) {
            throw new IllegalArgumentException("Esta meta não pertence ao gerente informado.");
        }

        metaGerenteRepository.delete(metaId);
    }
}