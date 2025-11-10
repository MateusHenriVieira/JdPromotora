package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class Produto {
    private String id;
    private String codigo;
    private String descricao;
    private String bancoId; // FK para Banco
    private String tipo;
    private Timestamp dataCadastro;
}