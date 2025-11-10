package com.synko.api.controller;

import com.synko.api.model.Gerente;
import com.synko.api.model.MetaGerente;
import com.synko.api.service.GerenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/gerentes")
@CrossOrigin(origins = "*")
public class GerenteController {

    @Autowired
    private GerenteService gerenteService;

    // --- ENDPOINTS DE GERENTE ---

    @PostMapping
    public ResponseEntity<String> createGerente(@RequestBody Gerente gerente) {
        try {
            String id = gerenteService.createGerente(gerente);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) { // FK não encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar gerente: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGerenteById(@PathVariable String id) {
        try {
            Gerente gerente = gerenteService.getGerenteById(id);
            return ResponseEntity.ok(gerente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar gerente: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGerentes() {
        try {
            List<Gerente> gerentes = gerenteService.getAllGerentes();
            return ResponseEntity.ok(gerentes);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar gerentes: " + e.getMessage());
        }
    }

    @GetMapping("/empresa/{id}")
    public ResponseEntity<?> getGerentesByEmpresaId(@PathVariable String id) {
        try {
            List<Gerente> gerentes = gerenteService.getGerentesByEmpresaId(id);
            return ResponseEntity.ok(gerentes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar gerentes por empresa: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGerente(@PathVariable String id, @RequestBody Gerente gerente) {
        try {
            gerenteService.updateGerente(id, gerente);
            return ResponseEntity.ok("Gerente atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar gerente: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGerente(@PathVariable String id) {
        try {
            gerenteService.deleteGerente(id);
            return ResponseEntity.ok("Gerente deletado com sucesso");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar gerente: " + e.getMessage());
        }
    }

    // --- ENDPOINTS DE METAS_GERENTE (Sub-recurso) ---

    @PostMapping("/{id}/metas")
    public ResponseEntity<String> createMeta(@PathVariable String id, @RequestBody MetaGerente meta) {
        try {
            String metaId = gerenteService.createMeta(id, meta);
            return ResponseEntity.status(HttpStatus.CREATED).body(metaId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar meta: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/metas")
    public ResponseEntity<?> getMetasByGerenteId(@PathVariable String id) {
        try {
            List<MetaGerente> metas = gerenteService.getMetasByGerenteId(id);
            return ResponseEntity.ok(metas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar metas: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/metas/{metaId}")
    public ResponseEntity<String> updateMeta(@PathVariable String id, @PathVariable String metaId, @RequestBody MetaGerente meta) {
        try {
            gerenteService.updateMeta(id, metaId, meta);
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
            gerenteService.deleteMeta(id, metaId);
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