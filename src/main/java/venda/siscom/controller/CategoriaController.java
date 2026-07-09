package venda.siscom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.CategoriaDAO;
import venda.siscom.model.Categoria;

public class CategoriaController {

    private static final Logger logger =
            LoggerFactory.getLogger(CategoriaController.class);

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    // SALVAR
    public boolean salvar(Categoria categoria) {
        logger.info("Iniciando salvamento de categoria.");

        boolean resultado = categoriaDAO.salvar(categoria);

        if (resultado) {
            logger.info("Categoria salva com sucesso.");
        } else {
            logger.warn("Falha ao salvar categoria.");
        }

        return resultado;
    }

    // ALTERAR
    public boolean alterar(Categoria categoria) {
        logger.info("Iniciando alteracao de categoria.");

        boolean resultado = categoriaDAO.alterar(categoria);

        if (resultado) {
            logger.info("Categoria alterada com sucesso.");
        } else {
            logger.warn("Falha ao alterar categoria.");
        }

        return resultado;
    }

    // EXCLUIR
    public boolean excluir(Integer id) {
        logger.info("Excluindo categoria. ID: {}", id);

        boolean resultado = categoriaDAO.excluir(id);

        if (resultado) {
            logger.info("Categoria excluida com sucesso. ID: {}", id);
        } else {
            logger.warn("Falha ao excluir categoria. ID: {}", id);
        }

        return resultado;
    }

    // PESQUISAR POR ID
    public Categoria pesquisar(Integer id) {
        logger.info("Pesquisando categoria. ID: {}", id);

        Categoria categoria = categoriaDAO.pesquisar(id);

        if (categoria != null) {
            logger.info("Categoria encontrada. ID: {}", id);
        } else {
            logger.warn("Categoria nao encontrada. ID: {}", id);
        }

        return categoria;
    }

    // LISTAR TODAS
    public List<Categoria> pesquisarTodos() {
        logger.info("Listando todas as categorias.");

        List<Categoria> lista = categoriaDAO.pesquisarTodos();

        logger.info("Total de categorias encontradas: {}",
                lista == null ? 0 : lista.size());

        return lista;
    }

    // PESQUISAR POR NOME
    public Categoria pesquisarPorNome(String nome) {
        logger.info("Pesquisando categoria por nome: {}", nome);

        Categoria categoria = categoriaDAO.pesquisarPorNome(nome);

        if (categoria != null) {
            logger.info("Categoria encontrada por nome: {}", nome);
        } else {
            logger.warn("Categoria nao encontrada por nome: {}", nome);
        }

        return categoria;
    }

}
