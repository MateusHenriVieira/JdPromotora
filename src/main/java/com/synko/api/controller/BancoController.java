package com.synko.api.controller;

import com.synko.api.model.Banco;
import com.synko.api.model.Layout;
import com.synko.api.model.Tarifa;
import com.synko.api.service.BancoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/bancos")
@CrossOrigin(origins = "*")
public class BancoController {

    @Autowired
    private BancoService bancoService;

    // --- ENDPOINTS DE BANCO ---

    @PostMapping
    public ResponseEntity<String> createBanco(@RequestBody Banco banco) {
        try {
            String id = bancoService.createBanco(banco);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar banco: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBancoById(@PathVariable String id) {
        try {
            Banco banco = bancoService.getBancoById(id);
            return ResponseEntity.ok(banco);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar banco: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBancos() {
        try {
            List<Banco> bancos = bancoService.getAllBancos();
            return ResponseEntity.ok(bancos);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar bancos: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBancos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean ativo) {
        try {
            List<Banco> bancos = bancoService.searchBancos(nome, ativo);
            return ResponseEntity.ok(bancos);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar bancos: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBanco(@PathVariable String id, @RequestBody Banco banco) {
        try {
            bancoService.updateBanco(id, banco);
            return ResponseEntity.ok("Banco atualizado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar banco: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBanco(@PathVariable String id) {
        try {
            bancoService.deleteBanco(id);
            return ResponseEntity.ok("Banco deletado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar banco: " + e.getMessage());
        }
    }

    // --- ENDPOINTS DE TARIFA (Sub-recurso) ---

    @PostMapping("/{id}/tarifas")
    public ResponseEntity<String> createTarifa(@PathVariable String id, @RequestBody Tarifa tarifa) {
        try {
            String tarifaId = bancoService.createTarifa(id, tarifa);
            return ResponseEntity.status(HttpStatus.CREATED).body(tarifaId);
        } catch (RuntimeException e) { // Captura o "Banco não encontrado"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar tarifa: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/tarifas")
    public ResponseEntity<?> getTarifasByBancoId(@PathVariable String id) {
        try {
            List<Tarifa> tarifas = bancoService.getTarifasByBancoId(id);
            return ResponseEntity.ok(tarifas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar tarifas: " + e.getMessage());
        }
    }

    // --- ENDPOINTS DE LAYOUT (Sub-recurso) ---

    @PostMapping("/{id}/layouts")
    public ResponseEntity<String> createLayout(@PathVariable String id, @RequestBody Layout layout) {
        try {
            String layoutId = bancoService.createLayout(id, layout);
            return ResponseEntity.status(HttpStatus.CREATED).body(layoutId);
        } catch (RuntimeException e) { // Captura o "Banco não encontrado"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar layout: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/layouts")
    public ResponseEntity<?> getLayoutsByBancoId(@PathVariable String id) {
        try {
            List<Layout> layouts = bancoService.getLayoutsByBancoId(id);
            return ResponseEntity.ok(layouts);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar layouts: " + e.getMessage());
        }
    }
}