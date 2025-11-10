package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class Tarifa {
    private String id;
    private String bancoId; // Chave estrangeira para Banco
    private String descricao;
    private Double valor;
    private String tipo;
    private Timestamp dataCadastro;
}