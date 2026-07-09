package venda.siscom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.VendaProdutoDAO;
import venda.siscom.model.VendaProduto;

public class VendaProdutoController {

    private static final Logger logger =
            LoggerFactory.getLogger(VendaProdutoController.class);

    private final VendaProdutoDAO vendaProdutoDAO = new VendaProdutoDAO();

    public boolean salvar(VendaProduto vendaProduto) {
        logger.info("Executando salvar item da venda.");

        boolean resultado = vendaProdutoDAO.salvar(vendaProduto);

        logger.info("Resultado salvar item da venda: {}", resultado);

        return resultado;
    }

    public boolean alterar(VendaProduto vendaProduto) {
        logger.info("Executando alterar item da venda.");

        boolean resultado = vendaProdutoDAO.alterar(vendaProduto);

        logger.info("Resultado alterar item da venda: {}", resultado);

        return resultado;
    }

    public boolean excluir(int id) {
        logger.info("Executando excluir item da venda. ID: {}", id);

        boolean resultado = vendaProdutoDAO.excluir(id);

        logger.info("Resultado excluir item da venda. ID: {}, resultado: {}",
                id,
                resultado);

        return resultado;
    }

    public VendaProduto pesquisar(int id) {
        logger.info("Executando pesquisar item da venda. ID: {}", id);

        VendaProduto vendaProduto = vendaProdutoDAO.pesquisar(id);

        logger.info("Resultado pesquisar item da venda. ID: {}, encontrado: {}",
                id,
                vendaProduto != null);

        return vendaProduto;
    }
}
