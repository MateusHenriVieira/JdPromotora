package com.synko.api.service;

import com.synko.api.model.MetaProduto;
import com.synko.api.model.Produto;
import com.synko.api.repository.BancoRepository;
import com.synko.api.repository.MetaProdutoRepository;
import com.synko.api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MetaProdutoRepository metaProdutoRepository;

    @Autowired
    private BancoService bancoService; // Para validar a FK bancoId

    // --- LÓGICA DE PRODUTO ---

    public String createProduto(Produto produto) throws ExecutionException, InterruptedException {
        // Validação de Chave Estrangeira (FK)
        bancoService.getBancoById(produto.getBancoId());

        return produtoRepository.save(produto);
    }

    public Produto getProdutoById(String id) throws ExecutionException, InterruptedException {
        Produto produto = produtoRepository.findById(id);
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado com ID: " + id);
        }
        return produto;
    }

    public List<Produto> getAllProdutos() throws ExecutionException, InterruptedException {
        return produtoRepository.findAll();
    }

    public List<Produto> getProdutosByBancoId(String bancoId) throws ExecutionException, InterruptedException {
        bancoService.getBancoById(bancoId); // Valida se o banco existe
        return produtoRepository.findAllByBancoId(bancoId);
    }

    public List<Produto> getProdutosByTipo(String tipo) throws ExecutionException, InterruptedException {
        // Você pode querer validar se o 'tipo' é um dos valores esperados (Enum)
        return produtoRepository.findAllByTipo(tipo);
    }

    public String updateProduto(String id, Produto produto) throws ExecutionException, InterruptedException {
        Produto existente = getProdutoById(id); // Valida se existe

        // Validação de FK
        bancoService.getBancoById(produto.getBancoId());

        produto.setId(id);
        produto.setDataCadastro(existente.getDataCadastro()); // Preserva data original
        return produtoRepository.save(produto);
    }

    public void deleteProduto(String id) throws ExecutionException, InterruptedException {
        getProdutoById(id); // Valida se existe

        // 1. Deleta as Metas filhas (limpeza)
        List<MetaProduto> metas = metaProdutoRepository.findAllByProdutoId(id);
        for (MetaProduto meta : metas) {
            metaProdutoRepository.delete(meta.getId());
        }

        // 2. Deleta o produto
        produtoRepository.delete(id);
    }

    // --- LÓGICA DE META_PRODUTO ---

    public String createMeta(String produtoId, MetaProduto meta) throws ExecutionException, InterruptedException {
        getProdutoById(produtoId); // Valida se o produto pai existe
        meta.setProdutoId(produtoId);
        return metaProdutoRepository.save(meta);
    }

    public List<MetaProduto> getMetasByProdutoId(String produtoId) throws ExecutionException, InterruptedException {
        getProdutoById(produtoId); // Valida se o produto pai existe
        return metaProdutoRepository.findAllByProdutoId(produtoId);
    }

    public MetaProduto getMetaById(String metaId) throws ExecutionException, InterruptedException {
        MetaProduto meta = metaProdutoRepository.findById(metaId);
        if (meta == null) {
            throw new RuntimeException("Meta não encontrada com ID: " + metaId);
        }
        return meta;
    }

    public String updateMeta(String produtoId, String metaId, MetaProduto meta) throws ExecutionException, InterruptedException {
        getProdutoById(produtoId); // Valida produto
        MetaProduto existente = getMetaById(metaId); // Valida meta

        if (!existente.getProdutoId().equals(produtoId)) {
            throw new IllegalArgumentException("Esta meta não pertence ao produto informado.");
        }

        meta.setId(metaId);
        meta.setProdutoId(produtoId);
        meta.setDataCadastro(existente.getDataCadastro());
        return metaProdutoRepository.save(meta);
    }

    public void deleteMeta(String produtoId, String metaId) throws ExecutionException, InterruptedException {
        MetaProduto existente = getMetaById(metaId); // Valida meta

        if (!existente.getProdutoId().equals(produtoId)) {
            throw new IllegalArgumentException("Esta meta não pertence ao produto informado.");
        }

        metaProdutoRepository.delete(metaId);
    }
}