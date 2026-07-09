package venda.siscom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.FornecedorProdutoDAO;
import venda.siscom.model.Fornecedor;
import venda.siscom.model.FornecedorProduto;
import venda.siscom.model.Produto;

public class FornecedorProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(FornecedorProdutoController.class);

    private final FornecedorProdutoDAO fornecedorProdutoDAO = new FornecedorProdutoDAO();

    
    public boolean salvar(FornecedorProduto fornecedorProduto) {
        logger.info("Iniciando salvamento da relação Fornecedor-Produto.");
        
        return fornecedorProdutoDAO.salvar(fornecedorProduto);
    }

    
    public boolean alterar(FornecedorProduto fornecedorProduto) {
        logger.info("Iniciando alteração da relação Fornecedor-Produto.");
        
        return fornecedorProdutoDAO.alterar(fornecedorProduto);
    }

    
    public boolean excluir(Integer id) {
        logger.info("Excluindo relação Fornecedor-Produto ID: {}", id);
        
        return fornecedorProdutoDAO.excluir(id);
    }

    
    public FornecedorProduto pesquisar(Integer id) {
        logger.info("Pesquisando relação Fornecedor-Produto ID: {}", id);
        
        return fornecedorProdutoDAO.pesquisar(id);
    }

    
    public List<FornecedorProduto> pesquisarTodos() {
        logger.info("Listando todas as relações Fornecedor-Produto.");
        
        return fornecedorProdutoDAO.pesquisarTodos();
    }

    
    public List<FornecedorProduto> pesquisarPorProduto(Produto produto) {
        logger.info("Listando fornecedores para o produto ID: {}", (produto != null ? produto.getId() : "null"));
        
        return fornecedorProdutoDAO.pesquisarPorProduto(produto);
    }

    
    public List<FornecedorProduto> pesquisarPorFornecedor(Fornecedor fornecedor) {
        logger.info("Listando produtos para o fornecedor ID: {}", (fornecedor != null ? fornecedor.getId() : "null"));
        
        return fornecedorProdutoDAO.pesquisarPorFornecedor(fornecedor);
    }

}