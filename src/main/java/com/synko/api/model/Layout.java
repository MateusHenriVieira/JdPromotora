package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class Layout {
    private String id;
    private String bancoId; // Chave estrangeira para Banco
    private String tipo; // PRODUCAO, COMISSAO_RECEBIDA, COMISSAO_PREVISTA
    private String estrutura;
    private Timestamp dataCadastro;
}