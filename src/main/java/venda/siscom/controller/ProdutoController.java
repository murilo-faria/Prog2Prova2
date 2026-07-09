package venda.siscom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.ProdutoDAO;
import venda.siscom.model.Categoria;
import venda.siscom.model.Produto;

public class ProdutoController {

    private static final Logger logger =
            LoggerFactory.getLogger(ProdutoController.class);

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    
    public boolean salvar(Produto produto) {
        logger.info("Iniciando salvamento de produto.");

        boolean resultado = produtoDAO.salvar(produto);

        if (resultado) {
            logger.info("Produto salvo com sucesso.");
        } else {
            logger.warn("Falha ao salvar produto.");
        }

        return resultado;
    }

    
    public boolean alterar(Produto produto) {
        logger.info("Iniciando alteracao de produto.");

        boolean resultado = produtoDAO.alterar(produto);

        if (resultado) {
            logger.info("Produto alterado com sucesso.");
        } else {
            logger.warn("Falha ao alterar produto.");
        }

        return resultado;
    }

    
    public boolean excluir(Integer id) {
        logger.info("Excluindo produto. ID: {}", id);

        boolean resultado = produtoDAO.excluir(id);

        if (resultado) {
            logger.info("Produto excluido com sucesso. ID: {}", id);
        } else {
            logger.warn("Falha ao excluir produto. ID: {}", id);
        }

        return resultado;
    }

    
    public Produto pesquisar(Integer id) {
        logger.info("Pesquisando produto. ID: {}", id);

        Produto produto = produtoDAO.pesquisar(id);

        if (produto != null) {
            logger.info("Produto encontrado. ID: {}", id);
        } else {
            logger.warn("Produto nao encontrado. ID: {}", id);
        }

        return produto;
    }

    
    public List<Produto> pesquisarTodos() {
        logger.info("Listando todos os produtos.");

        List<Produto> lista = produtoDAO.listarTodos();

        logger.info("Total de produtos encontrados: {}",
                lista == null ? 0 : lista.size());

        return lista;
    }

    
    public boolean atualizarEstoque(Produto produto, int quantidade) {

        if (produto == null) {
            logger.warn("Tentativa de atualizar estoque com produto nulo.");
            return false;
        }

        Produto produtoExistente = produtoDAO.pesquisar(produto.getId());

        if (produtoExistente == null) {
            logger.warn("Produto nao encontrado para atualizar estoque. ID: {}",
                    produto.getId());
            return false;
        }

        produtoExistente.setQtdEstoque(
                produtoExistente.getQtdEstoque() + quantidade);

        boolean resultado = produtoDAO.alterar(produtoExistente);

        logger.info(
                "Atualizacao de estoque do produto ID {} concluida: {}",
                produto.getId(),
                resultado);

        return resultado;
    }

    
    public boolean baixarEstoque(Produto produto, int quantidade) {

        if (produto == null) {
            logger.warn("Tentativa de baixar estoque com produto nulo.");
            return false;
        }

        Produto produtoExistente = produtoDAO.pesquisar(produto.getId());

        if (produtoExistente == null) {
            logger.warn("Produto nao encontrado para baixar estoque. ID: {}",
                    produto.getId());
            return false;
        }

        if (produtoExistente.getQtdEstoque() < quantidade) {
            logger.warn(
                    "Estoque insuficiente para produto ID {}. Estoque: {}, solicitado: {}",
                    produto.getId(),
                    produtoExistente.getQtdEstoque(),
                    quantidade);
            return false;
        }

        produtoExistente.setQtdEstoque(
                produtoExistente.getQtdEstoque() - quantidade);

        produtoExistente.setValorUltimaVenda(produto.getPreco());

        
        produtoExistente.setPreco(produto.getPreco());

        boolean resultado = produtoDAO.alterar(produtoExistente);

        logger.info(
                "Baixa de estoque do produto ID {} concluida: {}",
                produto.getId(),
                resultado);

        return resultado;
    }

    
    public boolean verificarEstoque(Produto produto, int quantidade) {

        if (produto == null) {
            logger.warn("Tentativa de verificar estoque com produto nulo.");
            return false;
        }

        Produto produtoExistente = produtoDAO.pesquisar(produto.getId());

        if (produtoExistente == null) {
            logger.warn("Produto nao encontrado para verificar estoque. ID: {}",
                    produto.getId());
            return false;
        }

        boolean resultado = produtoExistente.getQtdEstoque() > quantidade;

        logger.info(
                "Verificacao de estoque do produto ID {}. Estoque: {}, solicitado: {}, disponivel: {}",
                produto.getId(),
                produtoExistente.getQtdEstoque(),
                quantidade,
                resultado);

        return resultado;
    }

    
    public boolean verificaEstoqueExistente(Produto produto) {

        if (produto == null) {
            logger.warn("Tentativa de verificar estoque existente com produto nulo.");
            return false;
        }

        Produto produtoExistente = produtoDAO.pesquisar(produto.getId());

        if (produtoExistente == null) {
            logger.warn(
                    "Produto nao encontrado para verificar estoque existente. ID: {}",
                    produto.getId());
            return false;
        }

        boolean resultado = produtoExistente.getQtdEstoque() > 0;

        logger.info(
                "Verificacao de estoque existente do produto ID {}: {}",
                produto.getId(),
                resultado);

        return resultado;
    }

    
    public Categoria buscarOuCriarCategoria(String nomeCategoria) {

        if (nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            logger.warn("Tentativa de buscar ou criar categoria com nome vazio.");
            return null;
        }

        logger.info("Buscando ou criando categoria: {}", nomeCategoria.trim());

        return produtoDAO.buscarOuCriarCategoria(nomeCategoria.trim());
    }

    
    public boolean registrarCompraProduto(
            Produto produto,
            int quantidadeComprada,
            double valorCustoNovo) {

        if (produto == null) {
            logger.warn("Tentativa de registrar compra com produto nulo.");
            return false;
        }

        Produto produtoExistente = produtoDAO.pesquisar(produto.getId());

        if (produtoExistente == null) {
            logger.warn("Produto nao encontrado para registrar compra. ID: {}",
                    produto.getId());
            return false;
        }

        int estoqueAtual = produtoExistente.getQtdEstoque();

        double precoMedioAtual =
                produtoExistente.getPrecoMedio() == null
                        ? 0.0
                        : produtoExistente.getPrecoMedio();

        int novoEstoque = estoqueAtual + quantidadeComprada;

        double novoPrecoMedio;

        if (novoEstoque > 0) {

            double valorEstoqueAnterior =
                    estoqueAtual * precoMedioAtual;

            double valorCompraNova =
                    quantidadeComprada * valorCustoNovo;

            novoPrecoMedio =
                    (valorEstoqueAnterior + valorCompraNova)
                            / novoEstoque;

        } else {

            novoPrecoMedio = valorCustoNovo;
        }

        produtoExistente.setQtdEstoque(novoEstoque);
        produtoExistente.setPrecoMedio(novoPrecoMedio);
        produtoExistente.setValorUltimaCompra(valorCustoNovo);

        boolean resultado = produtoDAO.alterar(produtoExistente);

        logger.info(
                "Registro de compra do produto ID {} concluido: {}",
                produto.getId(),
                resultado);

        return resultado;
    }

}
