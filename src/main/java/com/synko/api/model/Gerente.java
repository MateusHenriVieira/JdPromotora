package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;
import java.util.Date;


@Data
public class Gerente {
    private String id;
    private String empresaId;
    private String nome;
    private String apelido;
    private String cpf;
    private String rg;
    private String orgao;
    private Date nascimento;
    private String endereco;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String celular;
    private String email;
    private String fax;
    private String foneComercial;
    private String titular;
    private String banco;
    private String conta;
    private String cpfTitular;
    private String agencia;
    private String login;
    private String ipAcesso;
    private Integer ranking;
    private Double percentualComissao;
    private String senha;
    private Boolean emailRecebe;
    private Timestamp dataCadastro;
}