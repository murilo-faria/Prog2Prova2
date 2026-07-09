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
import jakarta.persistence.Table;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(nullable = false)
    private LocalDate dataCompra;

    @Column(nullable = false)
    private Double valorTotal;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompraProduto> compraProdutos = new ArrayList<>();

    public Compra() {
    }

    public Compra(Integer id, Fornecedor fornecedor, LocalDate dataCompra,
                  Double valorTotal, List<CompraProduto> compraProdutos) {

        this.id = id;
        this.fornecedor = fornecedor;
        this.dataCompra = dataCompra;
        this.valorTotal = valorTotal;
        this.compraProdutos = compraProdutos;
    }

    /**
     * Método auxiliar para manter o relacionamento bidirecional.
     */
    public void adicionarProduto(CompraProduto produto) {
        this.compraProdutos.add(produto);
        produto.setCompra(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<CompraProduto> getCompraProdutos() {
        return compraProdutos;
    }

    public void setCompraProdutos(List<CompraProduto> compraProdutos) {
        this.compraProdutos = compraProdutos;
    }

}