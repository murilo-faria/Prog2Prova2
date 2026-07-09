package venda.siscom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.FornecedorDAO;
import venda.siscom.model.Fornecedor;

public class FornecedorController {

    private static final Logger logger = LoggerFactory.getLogger(FornecedorController.class);

    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();

    // SALVAR
    public boolean salvar(Fornecedor fornecedor) {
        logger.info("Iniciando salvamento de fornecedor.");
        
        return fornecedorDAO.salvar(fornecedor);
    }

    // ALTERAR
    public boolean alterar(Fornecedor fornecedor) {
        logger.info("Iniciando alteração de fornecedor.");
        
        return fornecedorDAO.alterar(fornecedor);
    }

    // EXCLUIR
    public boolean excluir(Integer id) {
        logger.info("Excluindo fornecedor ID: {}", id);
        
        return fornecedorDAO.excluir(id);
    }

    // PESQUISAR POR ID
    public Fornecedor pesquisar(Integer id) {
        logger.info("Pesquisando fornecedor ID: {}", id);
        
        return fornecedorDAO.pesquisar(id);
    }

    // LISTAR TODOS
    public List<Fornecedor> pesquisarTodos() {
        logger.info("Listando todos os fornecedores.");
        
        return fornecedorDAO.pesquisarTodos();
    }

}