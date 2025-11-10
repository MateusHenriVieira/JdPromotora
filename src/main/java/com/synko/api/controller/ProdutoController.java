package com.synko.api.controller;

import com.synko.api.model.MetaProduto;
import com.synko.api.model.Produto;
import com.synko.api.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // --- ENDPOINTS DE PRODUTO ---

    @PostMapping
    public ResponseEntity<String> createProduto(@RequestBody Produto produto) {
        try {
            String id = produtoService.createProduto(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (RuntimeException e) { // FK não encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar produto: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProdutoById(@PathVariable String id) {
        try {
            Produto produto = produtoService.getProdutoById(id);
            return ResponseEntity.ok(produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar produto: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProdutos() {
        try {
            List<Produto> produtos = produtoService.getAllProdutos();
            return ResponseEntity.ok(produtos);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar produtos: " + e.getMessage());
        }
    }

    @GetMapping("/banco/{id}")
    public ResponseEntity<?> getProdutosByBancoId(@PathVariable String id) {
        try {
            List<Produto> produtos = produtoService.getProdutosByBancoId(id);
            return ResponseEntity.ok(produtos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar produtos por banco: " + e.getMessage());
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> getProdutosByTipo(@PathVariable String tipo) {
        try {
            List<Produto> produtos = produtoService.getProdutosByTipo(tipo);
            return ResponseEntity.ok(produtos);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar produtos por tipo: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduto(@PathVariable String id, @RequestBody Produto produto) {
        try {
            produtoService.updateProduto(id, produto);
            return ResponseEntity.ok("Produto atualizado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduto(@PathVariable String id) {
        try {
            produtoService.deleteProduto(id);
            return ResponseEntity.ok("Produto deletado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar produto: " + e.getMessage());
        }
    }

    // --- ENDPOINTS DE METAS_PRODUTO (Sub-recurso) ---

    @PostMapping("/{id}/metas")
    public ResponseEntity<String> createMeta(@PathVariable String id, @RequestBody MetaProduto meta) {
        try {
            String metaId = produtoService.createMeta(id, meta);
            return ResponseEntity.status(HttpStatus.CREATED).body(metaId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar meta: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/metas")
    public ResponseEntity<?> getMetasByProdutoId(@PathVariable String id) {
        try {
            List<MetaProduto> metas = produtoService.getMetasByProdutoId(id);
            return ResponseEntity.ok(metas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar metas: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/metas/{metaId}")
    public ResponseEntity<String> updateMeta(@PathVariable String id, @PathVariable String metaId, @RequestBody MetaProduto meta) {
        try {
            produtoService.updateMeta(id, metaId, meta);
            return ResponseEntity.ok("Meta atualizada com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar meta: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/metas/{metaId}")
    public ResponseEntity<String> deleteMeta(@PathVariable String id, @PathVariable String metaId) {
        try {
            produtoService.deleteMeta(id, metaId);
            return ResponseEntity.ok("Meta deletada com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar meta: " + e.getMessage());
        }
    }
}