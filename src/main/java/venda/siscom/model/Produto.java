package venda.siscom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    @Column(nullable = false)
    private Double preco;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private Double precoMedio;

    @Column(nullable = false)
    private Integer qtdEstoque;

    @Column(nullable = false)
    private Double valorUltimaCompra;

    @Column(nullable = false)
    private Double valorUltimaVenda;

    public Produto() {
    }

    public Produto(Integer id, String nome, Double preco, Categoria categoria,
                   Double precoMedio, Integer qtdEstoque,
                   Double valorUltimaCompra, Double valorUltimaVenda) {

        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.precoMedio = precoMedio;
        this.qtdEstoque = qtdEstoque;
        this.valorUltimaCompra = valorUltimaCompra;
        this.valorUltimaVenda = valorUltimaVenda;
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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Double getPrecoMedio() {
        return precoMedio;
    }

    public void setPrecoMedio(Double precoMedio) {
        this.precoMedio = precoMedio;
    }

    public Integer getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(Integer qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public Double getValorUltimaCompra() {
        return valorUltimaCompra;
    }

    public void setValorUltimaCompra(Double valorUltimaCompra) {
        this.valorUltimaCompra = valorUltimaCompra;
    }

    public Double getValorUltimaVenda() {
        return valorUltimaVenda;
    }

    public void setValorUltimaVenda(Double valorUltimaVenda) {
        this.valorUltimaVenda = valorUltimaVenda;
    }

    @Override
    public String toString() {
        return nome;
    }

}