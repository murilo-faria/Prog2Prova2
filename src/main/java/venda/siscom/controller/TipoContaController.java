package venda.siscom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.TipoContaDAO;
import venda.siscom.model.TipoConta;

public class TipoContaController {

    private static final Logger logger =
            LoggerFactory.getLogger(TipoContaController.class);

    private final TipoContaDAO tipoContaDAO = new TipoContaDAO();

    // SALVAR
    public boolean salvar(TipoConta tipoConta) {
        logger.info("Executando salvar tipo de conta.");

        boolean resultado = tipoContaDAO.salvar(tipoConta);

        logger.info("Resultado salvar tipo de conta: {}", resultado);

        return resultado;
    }

    // ALTERAR
    public boolean alterar(TipoConta tipoConta) {
        logger.info("Executando alterar tipo de conta.");

        boolean resultado = tipoContaDAO.alterar(tipoConta);

        logger.info("Resultado alterar tipo de conta: {}", resultado);

        return resultado;
    }

    // EXCLUIR
    public boolean excluir(Integer id) {
        logger.info("Executando excluir tipo de conta. ID: {}", id);

        boolean resultado = tipoContaDAO.excluir(id);

        logger.info("Resultado excluir tipo de conta. ID: {}, resultado: {}",
                id,
                resultado);

        return resultado;
    }

    // PESQUISAR POR ID
    public TipoConta pesquisar(Integer id) {
        logger.info("Executando pesquisar tipo de conta. ID: {}", id);

        TipoConta tipoConta = tipoContaDAO.pesquisar(id);

        logger.info("Resultado pesquisar tipo de conta. ID: {}, encontrado: {}",
                id,
                tipoConta != null);

        return tipoConta;
    }

    // LISTAR TODOS
    public List<TipoConta> pesquisarTodos() {
        logger.info("Executando pesquisar todos os tipos de conta.");

        List<TipoConta> lista = tipoContaDAO.pesquisarTodos();

        logger.info("Total de tipos de conta encontrados: {}",
                lista == null ? 0 : lista.size());

        return lista;
    }

    // BUSCAR OU CRIAR
    public TipoConta buscarOuCriarConta(String nomeConta) {

        logger.info("Executando buscar ou criar tipo de conta: {}", nomeConta);

        if (nomeConta == null || nomeConta.trim().isEmpty()) {
            logger.warn("Nome do tipo de conta invalido.");
            return null;
        }

        TipoConta tipoConta =
                tipoContaDAO.buscarOuCriarConta(nomeConta.trim());

        logger.info("Resultado buscar ou criar tipo de conta. Nome: {}, encontrado/criado: {}",
                nomeConta.trim(),
                tipoConta != null);

        return tipoConta;
    }

}
