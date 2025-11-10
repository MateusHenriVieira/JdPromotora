package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class Empresa {
    private String id;
    private String nome;
    private String razaoSocial;
    private String cnpj;
    private String cpf;
    private String email;
    private String celular;
    private String fone;
    private String fax;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String responsavel;
    private String tokenAutenticacao;
    private String tokenAutentique;
    private String observacoes;
    private Timestamp dataCadastro;
    private Timestamp dataAtualizacao;
}