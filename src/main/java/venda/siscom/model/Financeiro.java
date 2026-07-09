package venda.siscom.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "financeiro")
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;

    @OneToOne
    @JoinColumn(name = "compra_id")
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "tipo_conta_id", nullable = false)
    private TipoConta tipoConta;

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id", nullable = false)
    private FormaPagamento formaPagamento;

    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento;

    @Column(name = "pagar_ou_receber", nullable = false)
    private Integer pagarOuReceber;
    /*
        0 = Pagar
        1 = Receber
    */

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @Column(length = 300)
    private String descricao;

    @OneToMany(mappedBy = "financeiro",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    private List<FinanceiroParcela> parcelas = new ArrayList<>();

    public Financeiro() {
    }

    public void adicionarParcela(FinanceiroParcela parcela) {
        parcelas.add(parcela);
        parcela.setFinanceiro(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Integer getPagarOuReceber() {
        return pagarOuReceber;
    }

    public void setPagarOuReceber(Integer pagarOuReceber) {
        this.pagarOuReceber = pagarOuReceber;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<FinanceiroParcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<FinanceiroParcela> parcelas) {
        this.parcelas = parcelas;
    }
}