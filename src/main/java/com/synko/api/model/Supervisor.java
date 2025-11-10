package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class Supervisor {
    private String id;
    private String nome;
    private String apelido;
    private String cpf;
    private String telefone;
    private String celular;
    private String email;
    private String gerenteId; // FK para Gerente
    private Timestamp dataCadastro;
}