package venda.siscom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "forma_pagamento")
public class FormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    @Column(nullable = false)
    private Integer qtdeParcela;

    @Column(nullable = false)
    private Integer prazo;

    @Column(nullable = false)
    private Integer avistaPrazo;
    /*
     * 0 = À Vista
     * 1 = A Prazo
     */

    public FormaPagamento() {
    }

    public FormaPagamento(Integer id, String nome, Integer qtdeParcela,
                          Integer prazo, Integer avistaPrazo) {
        this.id = id;
        this.nome = nome;
        this.qtdeParcela = qtdeParcela;
        this.prazo = prazo;
        this.avistaPrazo = avistaPrazo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQtdeParcela() {
        return qtdeParcela;
    }

    public void setQtdeParcela(Integer qtdeParcela) {
        this.qtdeParcela = qtdeParcela;
    }

    public Integer getPrazo() {
        return prazo;
    }

    public void setPrazo(Integer prazo) {
        this.prazo = prazo;
    }

    public Integer getAvistaPrazo() {
        return avistaPrazo;
    }

    public void setAvistaPrazo(Integer avistaPrazo) {
        this.avistaPrazo = avistaPrazo;
    }

    @Override
    public String toString() {
        return nome;
    }
}