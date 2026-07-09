package venda.siscom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.FormaPagamentoDAO;
import venda.siscom.model.FormaPagamento;

public class FormaPagamentoController {

    private static final Logger logger = LoggerFactory.getLogger(FormaPagamentoController.class);

    private final FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();

    
    public boolean salvar(FormaPagamento formaPagamento) {
        logger.info("Iniciando salvamento de forma de pagamento.");
        
        return formaPagamentoDAO.salvar(formaPagamento);
    }

    
    public boolean alterar(FormaPagamento formaPagamento) {
        logger.info("Iniciando alteração de forma de pagamento.");
        
        return formaPagamentoDAO.alterar(formaPagamento);
    }

    
    public boolean excluir(Integer id) {
        logger.info("Excluindo forma de pagamento ID: {}", id);
        
        return formaPagamentoDAO.excluir(id);
    }

    
    public FormaPagamento pesquisar(Integer id) {
        logger.info("Pesquisando forma de pagamento ID: {}", id);
        
        return formaPagamentoDAO.pesquisar(id);
    }

    
    public List<FormaPagamento> pesquisarTodos() {
        logger.info("Listando todas as formas de pagamento.");
        
        return formaPagamentoDAO.pesquisarTodos();
    }

    
    public FormaPagamento buscarOuCriarForma(FormaPagamento formaPagamento) {

        if (formaPagamento == null) {
            logger.warn("Tentativa de buscar ou criar forma de pagamento com objeto nulo.");
            return null;
        }

        if (formaPagamento.getNome() == null
                || formaPagamento.getNome().trim().isEmpty()) {
            logger.warn("Nome da forma de pagamento inválido.");
            return null;
        }

        logger.info("Buscando ou criando forma de pagamento: {}", formaPagamento.getNome());

        return formaPagamentoDAO.buscarOuCriarForma(formaPagamento);
    }

}