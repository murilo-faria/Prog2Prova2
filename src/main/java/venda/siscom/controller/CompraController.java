package venda.siscom.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.CompraDAO;
import venda.siscom.dao.FinanceiroDAO;
import venda.siscom.model.Compra;
import venda.siscom.model.CompraProduto;
import venda.siscom.model.Financeiro;
import venda.siscom.model.FinanceiroParcela;
import venda.siscom.model.FormaPagamento;
import venda.siscom.model.Produto;
import venda.siscom.model.TipoConta;

public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    private final CompraDAO compraDAO = new CompraDAO();
    private final FinanceiroDAO financeiroDAO = new FinanceiroDAO();
    private final ProdutoController produtoController = new ProdutoController();

    /**
     * Finaliza uma compra.
     *
     * Salva:
     * Compra
     * Itens
     * Atualiza Estoque
     * Gera Financeiro
     * Gera Parcelas
     */
    public boolean finalizarCompra(
            Compra compra,
            List<CompraProduto> itens,
            FormaPagamento formaPagamento,
            TipoConta tipoConta,
            LocalDate dataPrimeiraParcela,
            Integer quantidadeParcelas,
            Integer prazoDias,
            Double desconto) {

        logger.info("Iniciando finalização da compra.");

        if (itens == null || itens.isEmpty()) {
            logger.warn("Tentativa de finalizar compra sem itens.");
            return false;
        }

        double subtotal = 0;

        for (CompraProduto item : itens) {

            Produto produto = item.getProduto();

            logger.info("Processando produto ID: {}", produto.getId());

            Produto produtoBanco =
                    produtoController.pesquisar(produto.getId());

            if (produtoBanco == null) {

                logger.error("Produto não encontrado. ID: {}", produto.getId());

                return false;
            }

            subtotal +=
                    item.getQuantidade()
                    * item.getValorUnitario();

            compra.adicionarProduto(item);
        }

        if (desconto == null) {
            desconto = 0.0;
        }

        compra.setValorTotal(subtotal - desconto);

        logger.info("Valor total da compra: {}", compra.getValorTotal());

        logger.info("Salvando compra.");

        if (!compraDAO.salvar(compra)) {

            logger.error("Erro ao salvar a compra.");
            return false;
        }

        logger.info("Atualizando estoque dos produtos.");

        /*
         * Atualiza estoque
         * Atualiza preço médio
         * Atualiza última compra
         */
        for (CompraProduto item : itens) {

            produtoController.registrarCompraProduto(

                    item.getProduto(),

                    item.getQuantidade(),

                    item.getValorUnitario()

            );

        }

        logger.info("Gerando lançamento financeiro.");

        /*
         * Gera Financeiro
         */

        Financeiro financeiro = new Financeiro();

        financeiro.setCompra(compra);

        financeiro.setTipoConta(tipoConta);

        financeiro.setFormaPagamento(formaPagamento);

        financeiro.setDataLancamento(dataPrimeiraParcela);

        // 0 = PAGAR
        financeiro.setPagarOuReceber(0);

        financeiro.setValorTotal(compra.getValorTotal());

        logger.info("Gerando {} parcela(s).", quantidadeParcelas);

        gerarParcelas(

                financeiro,

                dataPrimeiraParcela,

                quantidadeParcelas,

                prazoDias,

                compra.getValorTotal()

        );

        if (!financeiroDAO.salvar(financeiro)) {

            logger.error("Erro ao salvar o lançamento financeiro.");
            return false;
        }

        logger.info("Compra finalizada com sucesso.");

        return true;
    }

    /**
     * Gera automaticamente as parcelas do financeiro.
     */
    private void gerarParcelas(
            Financeiro financeiro,
            LocalDate dataPrimeiraParcela,
            Integer quantidadeParcelas,
            Integer prazoDias,
            Double valorTotal) {

        logger.info("Gerando parcelas do financeiro.");

        if (quantidadeParcelas == null || quantidadeParcelas <= 0) {
            quantidadeParcelas = 1;
        }

        if (prazoDias == null || prazoDias <= 0) {
            prazoDias = 30;
        }

        logger.info("Quantidade de parcelas: {}", quantidadeParcelas);
        logger.info("Prazo entre parcelas: {} dias.", prazoDias);

        double valorParcela = valorTotal / quantidadeParcelas;

        // Arredonda para duas casas
        valorParcela = Math.round(valorParcela * 100.0) / 100.0;

        double somaParcelas = 0.0;

        for (int i = 1; i <= quantidadeParcelas; i++) {

            FinanceiroParcela parcela = new FinanceiroParcela();

            parcela.setNumeroParcela(i);

            parcela.setDataVencimento(
                    dataPrimeiraParcela.plusDays(
                            (long) (i - 1) * prazoDias));

            parcela.setValorOriginal(valorParcela);

            parcela.setDesconto(0.0);

            parcela.setAcrescimo(0.0);

            parcela.setValorFinal(valorParcela);

            // 0 = PENDENTE
            parcela.setStatus(0);

            somaParcelas += valorParcela;

            financeiro.adicionarParcela(parcela);

            logger.info("Parcela {} gerada. Valor: {}", i, valorParcela);
        }

        // Corrige diferença de arredondamento na última parcela
        double diferenca = Math.round((valorTotal - somaParcelas) * 100.0) / 100.0;

        if (Math.abs(diferenca) > 0.0) {

            logger.info("Aplicando ajuste de arredondamento na última parcela.");

            FinanceiroParcela ultima =
                    financeiro.getParcelas()
                               .get(financeiro.getParcelas().size() - 1);

            ultima.setValorOriginal(
                    ultima.getValorOriginal() + diferenca);

            ultima.setValorFinal(
                    ultima.getValorFinal() + diferenca);
        }

        logger.info("Parcelas geradas com sucesso.");
    }

}