package com.synko.api.controller;

import com.synko.api.model.Rota;
import com.synko.api.service.RotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/rotas")
@CrossOrigin(origins = "*")
public class RotaController {

    @Autowired
    private RotaService rotaService;

    @PostMapping
    public ResponseEntity<String> createRota(@RequestBody Rota rota) {
        try {
            String id = rotaService.createRota(rota);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (RuntimeException e) { // FK não encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar rota: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRotaById(@PathVariable String id) {
        try {
            Rota rota = rotaService.getRotaById(id);
            return ResponseEntity.ok(rota);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar rota: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRotas() {
        try {
            List<Rota> rotas = rotaService.getAllRotas();
            return ResponseEntity.ok(rotas);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar rotas: " + e.getMessage());
        }
    }

    @GetMapping("/supervisor/{id}")
    public ResponseEntity<?> getRotasBySupervisorId(@PathVariable String id) {
        try {
            List<Rota> rotas = rotaService.getRotasBySupervisorId(id);
            return ResponseEntity.ok(rotas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar rotas por supervisor: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRota(@PathVariable String id, @RequestBody Rota rota) {
        try {
            rotaService.updateRota(id, rota);
            return ResponseEntity.ok("Rota atualizada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar rota: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRota(@PathVariable String id) {
        try {
            rotaService.deleteRota(id);
            return ResponseEntity.ok("Rota deletada com sucesso");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar rota: " + e.getMessage());
        }
    }
}