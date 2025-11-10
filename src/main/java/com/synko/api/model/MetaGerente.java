package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;
import java.util.Date;

@Data
public class MetaGerente {
    private String id;
    private String gerenteId; // FK para Gerente
    private String banco;
    private Date dataInicial;
    private Double meta;
    private Timestamp dataCadastro;
}