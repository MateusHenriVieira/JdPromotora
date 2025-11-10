package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class MetaProduto {
    private String id;
    private String produtoId; // FK para Produto
    private Double meta;
    private String status;
    private Timestamp dataCadastro;
}