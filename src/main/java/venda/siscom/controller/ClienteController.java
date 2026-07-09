package venda.siscom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.ClienteDAO;
import venda.siscom.model.Cliente;

public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteDAO clienteDAO = new ClienteDAO();

    // SALVAR
    public boolean salvar(Cliente cliente) {
        logger.info("Iniciando salvamento do cliente.");

        boolean resultado = clienteDAO.salvar(cliente);

        if (resultado) {
            logger.info("Cliente salvo com sucesso.");
        } else {
            logger.warn("Falha ao salvar cliente.");
        }

        return resultado;
    }

    // ALTERAR
    public boolean alterar(Cliente cliente) {
        logger.info("Iniciando alteração do cliente.");

        boolean resultado = clienteDAO.alterar(cliente);

        if (resultado) {
            logger.info("Cliente alterado com sucesso.");
        } else {
            logger.warn("Falha ao alterar cliente.");
        }

        return resultado;
    }

    // EXCLUIR
    public boolean excluir(Integer id) {
        logger.info("Excluindo cliente. ID: {}", id);

        boolean resultado = clienteDAO.excluir(id);

        if (resultado) {
            logger.info("Cliente excluído com sucesso.");
        } else {
            logger.warn("Falha ao excluir cliente. ID: {}", id);
        }

        return resultado;
    }

    // PESQUISAR POR ID
    public Cliente pesquisar(Integer id) {
        logger.info("Pesquisando cliente pelo ID: {}", id);

        Cliente cliente = clienteDAO.pesquisar(id);

        if (cliente != null) {
            logger.info("Cliente encontrado.");
        } else {
            logger.warn("Cliente não encontrado. ID: {}", id);
        }

        return cliente;
    }

    // LISTAR TODOS
    public List<Cliente> pesquisarTodos() {
        logger.info("Listando todos os clientes.");

        List<Cliente> lista = clienteDAO.pesquisarTodos();

        logger.info("Total de clientes encontrados: {}", lista.size());

        return lista;
    }

    // PESQUISAR POR CPF
    public Cliente pesquisarPorCpf(String cpf) {
        logger.info("Pesquisando cliente pelo CPF: {}", cpf);

        Cliente cliente = clienteDAO.pesquisarPorCpf(cpf);

        if (cliente != null) {
            logger.info("Cliente encontrado.");
        } else {
            logger.warn("Cliente não encontrado. CPF: {}", cpf);
        }

        return cliente;
    }

}