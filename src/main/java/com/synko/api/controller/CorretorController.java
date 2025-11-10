package com.synko.api.controller;

import com.synko.api.model.Corretor;
import com.synko.api.service.CorretorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/corretores")
@CrossOrigin(origins = "*")
public class CorretorController {

    @Autowired
    private CorretorService corretorService;

    @PostMapping
    public ResponseEntity<String> createCorretor(@RequestBody Corretor corretor) {
        try {
            String id = corretorService.createCorretor(corretor);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar corretor: " + e.getMessage());
        } catch (RuntimeException e) { // Captura FKs não encontradas
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCorretorById(@PathVariable String id) {
        try {
            Corretor corretor = corretorService.getCorretorById(id);
            return ResponseEntity.ok(corretor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar corretor: " + e.getMessage());
        }
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<?> getCorretorByCpf(@PathVariable String cpf) {
        try {
            Corretor corretor = corretorService.getCorretorByCpf(cpf);
            return ResponseEntity.ok(corretor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar corretor por CPF: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCorretores() {
        try {
            List<Corretor> corretores = corretorService.getAllCorretores();
            return ResponseEntity.ok(corretores);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar corretores: " + e.getMessage());
        }
    }

    @GetMapping("/empresa/{id}")
    public ResponseEntity<?> getCorretoresByEmpresaId(@PathVariable String id) {
        try {
            List<Corretor> corretores = corretorService.getCorretoresByEmpresaId(id);
            return ResponseEntity.ok(corretores);
        } catch (RuntimeException e) { // Captura se a empresa não for encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar corretores por empresa: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCorretor(@PathVariable String id, @RequestBody Corretor corretor) {
        try {
            corretorService.updateCorretor(id, corretor);
            return ResponseEntity.ok("Corretor atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar corretor: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCorretor(@PathVariable String id) {
        try {
            corretorService.deleteCorretor(id);
            return ResponseEntity.ok("Corretor deletado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar corretor: " + e.getMessage());
        }
    }
}