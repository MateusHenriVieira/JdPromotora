package com.synko.api.controller;

import com.synko.api.model.Empresa;
import com.synko.api.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<String> createEmpresa(@RequestBody Empresa empresa) {
        try {
            String id = empresaService.createEmpresa(empresa);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar empresa: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmpresaById(@PathVariable String id) {
        try {
            Empresa empresa = empresaService.getEmpresaById(id);
            return ResponseEntity.ok(empresa);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar empresa: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllEmpresas() {
        try {
            List<Empresa> empresas = empresaService.getAllEmpresas();
            return ResponseEntity.ok(empresas);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar empresas: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchEmpresas(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String token) {
        try {
            List<Empresa> empresas = empresaService.searchEmpresas(nome, cnpj, token);
            return ResponseEntity.ok(empresas);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar empresas: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmpresa(
            @PathVariable String id,
            @RequestBody Empresa empresa) {
        try {
            empresaService.updateEmpresa(id, empresa);
            return ResponseEntity.ok("Empresa atualizada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar empresa: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmpresa(@PathVariable String id) {
        try {
            empresaService.deleteEmpresa(id);
            return ResponseEntity.ok("Empresa deletada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar empresa: " + e.getMessage());
        }
    }
}