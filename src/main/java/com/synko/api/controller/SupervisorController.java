package com.synko.api.controller;

import com.synko.api.model.Supervisor;
import com.synko.api.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/supervisores")
@CrossOrigin(origins = "*")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @PostMapping
    public ResponseEntity<String> createSupervisor(@RequestBody Supervisor supervisor) {
        try {
            String id = supervisorService.createSupervisor(supervisor);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) { // FK não encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar supervisor: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupervisorById(@PathVariable String id) {
        try {
            Supervisor supervisor = supervisorService.getSupervisorById(id);
            return ResponseEntity.ok(supervisor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar supervisor: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSupervisores() {
        try {
            List<Supervisor> supervisores = supervisorService.getAllSupervisores();
            return ResponseEntity.ok(supervisores);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar supervisores: " + e.getMessage());
        }
    }

    @GetMapping("/gerente/{id}")
    public ResponseEntity<?> getSupervisoresByGerenteId(@PathVariable String id) {
        try {
            List<Supervisor> supervisores = supervisorService.getSupervisoresByGerenteId(id);
            return ResponseEntity.ok(supervisores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar supervisores por gerente: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSupervisor(@PathVariable String id, @RequestBody Supervisor supervisor) {
        try {
            supervisorService.updateSupervisor(id, supervisor);
            return ResponseEntity.ok("Supervisor atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar supervisor: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupervisor(@PathVariable String id) {
        try {
            supervisorService.deleteSupervisor(id);
            return ResponseEntity.ok("Supervisor deletado com sucesso");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar supervisor: " + e.getMessage());
        }
    }
}