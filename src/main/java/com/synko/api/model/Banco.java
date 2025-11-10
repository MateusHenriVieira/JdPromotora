package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class Banco {
    private String id;
    private String nome;
    private Boolean ativo;
    private Timestamp dataCadastro;
}