package com.synko.api.service;

import com.synko.api.model.Banco;
import com.synko.api.model.Layout;
import com.synko.api.model.Tarifa;
import com.synko.api.repository.BancoRepository;
import com.synko.api.repository.LayoutRepository;
import com.synko.api.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class BancoService {

    @Autowired
    private BancoRepository bancoRepository;

    @Autowired
    private TarifaRepository tarifaRepository;

    @Autowired
    private LayoutRepository layoutRepository;

    // --- LÓGICA DE BANCO ---

    public String createBanco(Banco banco) throws ExecutionException, InterruptedException {
        if (banco.getNome() == null || banco.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do banco é obrigatório");
        }
        Banco existente = bancoRepository.findByNome(banco.getNome());
        if (existente != null) {
            throw new IllegalArgumentException("Um banco com este nome já está cadastrado");
        }
        if (banco.getAtivo() == null) {
            banco.setAtivo(true);
        }
        return bancoRepository.save(banco);
    }

    public Banco getBancoById(String id) throws ExecutionException, InterruptedException {
        Banco banco = bancoRepository.findById(id);
        if (banco == null) {
            throw new RuntimeException("Banco não encontrado com ID: " + id);
        }
        return banco;
    }

    public List<Banco> getAllBancos() throws ExecutionException, InterruptedException {
        return bancoRepository.findAll();
    }

    public String updateBanco(String id, Banco banco) throws ExecutionException, InterruptedException {
        Banco existente = getBancoById(id); // Reusa o método que já tem a checagem
        banco.setId(id);
        banco.setDataCadastro(existente.getDataCadastro());
        return bancoRepository.save(banco);
    }

    public void deleteBanco(String id) throws ExecutionException, InterruptedException {
        getBancoById(id); // Garante que existe antes de deletar
        // TODO: Adicionar lógica para deletar tarifas e layouts órfãos, se necessário
        bancoRepository.delete(id);
    }

    public List<Banco> searchBancos(String nome, Boolean ativo) throws ExecutionException, InterruptedException {
        return bancoRepository.search(nome, ativo);
    }

    // --- LÓGICA DE TARIFA (Vinculada ao Banco) ---

    public String createTarifa(String bancoId, Tarifa tarifa) throws ExecutionException, InterruptedException {
        getBancoById(bancoId); // Valida se o banco pai existe
        tarifa.setBancoId(bancoId);
        return tarifaRepository.save(tarifa);
    }

    public List<Tarifa> getTarifasByBancoId(String bancoId) throws ExecutionException, InterruptedException {
        getBancoById(bancoId); // Valida se o banco pai existe
        return tarifaRepository.findAllByBancoId(bancoId);
    }

    // --- LÓGICA DE LAYOUT (Vinculado ao Banco) ---

    public String createLayout(String bancoId, Layout layout) throws ExecutionException, InterruptedException {
        getBancoById(bancoId); // Valida se o banco pai existe
        layout.setBancoId(bancoId);
        return layoutRepository.save(layout);
    }

    public List<Layout> getLayoutsByBancoId(String bancoId) throws ExecutionException, InterruptedException {
        getBancoById(bancoId); // Valida se o banco pai existe
        return layoutRepository.findAllByBancoId(bancoId);
    }
}