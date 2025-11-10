package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class Rota {
    private String id;
    private String supervisorId; // FK para Supervisor
    private String nome;
    private Double valorHospedagem;
    private Double valorRefeicao;
    private Double valorHospedagemPorLitro;
    private Double consumoVeiculoKmPorLitro;
    private String termoResponsabilidade;
    private Timestamp dataCadastro;
}