package venda.siscom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.CompraProdutoDAO;
import venda.siscom.model.Compra;
import venda.siscom.model.CompraProduto;

public class CompraProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(CompraProdutoController.class);

    private final CompraProdutoDAO compraProdutoDAO = new CompraProdutoDAO();

    
    public boolean salvar(CompraProduto compraProduto) {

        logger.info("Iniciando salvamento do item da compra.");

        boolean resultado = compraProdutoDAO.salvar(compraProduto);

        if (resultado) {
            logger.info("Item da compra salvo com sucesso.");
        } else {
            logger.warn("Falha ao salvar item da compra.");
        }

        return resultado;
    }

    
    public boolean alterar(CompraProduto compraProduto) {

        logger.info("Iniciando alteração do item da compra.");

        boolean resultado = compraProdutoDAO.alterar(compraProduto);

        if (resultado) {
            logger.info("Item da compra alterado com sucesso.");
        } else {
            logger.warn("Falha ao alterar item da compra.");
        }

        return resultado;
    }

    
    public boolean excluir(Integer id) {

        logger.info("Excluindo item da compra. ID: {}", id);

        boolean resultado = compraProdutoDAO.excluir(id);

        if (resultado) {
            logger.info("Item da compra excluído com sucesso.");
        } else {
            logger.warn("Falha ao excluir item da compra. ID: {}", id);
        }

        return resultado;
    }

    
    public CompraProduto pesquisar(Integer id) {

        logger.info("Pesquisando item da compra por ID: {}", id);

        CompraProduto compraProduto = compraProdutoDAO.pesquisar(id);

        if (compraProduto != null) {
            logger.info("Item da compra encontrado.");
        } else {
            logger.warn("Item da compra não encontrado. ID: {}", id);
        }

        return compraProduto;
    }

    
    public List<CompraProduto> pesquisarTodos() {

        logger.info("Listando todos os itens de compra.");

        List<CompraProduto> lista = compraProdutoDAO.pesquisarTodos();

        logger.info("Total de itens encontrados: {}", lista.size());

        return lista;
    }

    
    public List<CompraProduto> pesquisarPorCompra(Compra compra) {

        logger.info("Listando itens da compra. ID da compra: {}", compra.getId());

        List<CompraProduto> lista = compraProdutoDAO.pesquisarPorCompra(compra);

        logger.info("Total de itens encontrados: {}", lista.size());

        return lista;
    }

}