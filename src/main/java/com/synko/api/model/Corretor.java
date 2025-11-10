package com.synko.api.model;

import com.google.cloud.Timestamp;
import lombok.Data;
import java.util.Date;

@Data
public class Corretor {

    // Seção Principal
    private String id;
    private Timestamp dataCadastro;
    private String dadosResponsaveis;
    private Timestamp dataAtualizacao;
    private String empresaId; // FK
    private String nivelEsteira;
    private String grupoComissao;
    private String rotaId; // FK
    private String grupoAcesso;
    private String indicador;
    private String numeroCertificado;
    private Date validadeCertificado;
    private Date inicioOperacao;
    private Date fimOperacao;

    // Dados Pessoais
    private String nome;
    private String apelidoFantasia;
    private String cpfCnpj; // único
    private String cpfSocio;
    private String socioAdministrador;
    private String nomeMae;
    private String rg;
    private String orgaoEmissor;
    private String estadoCivil;
    private String profissao;
    private String observacao;

    // Endereço
    private String endereco;
    private String complemento;
    private String uf;
    private String numero;
}