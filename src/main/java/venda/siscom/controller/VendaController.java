package venda.siscom.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.FinanceiroDAO;
import venda.siscom.dao.VendaDAO;
import venda.siscom.model.Financeiro;
import venda.siscom.model.FinanceiroParcela;
import venda.siscom.model.FormaPagamento;
import venda.siscom.model.Produto;
import venda.siscom.model.TipoConta;
import venda.siscom.model.Venda;
import venda.siscom.model.VendaProduto;

public class VendaController {

    private static final Logger logger =
            LoggerFactory.getLogger(VendaController.class);

    private final VendaDAO vendaDAO = new VendaDAO();
    private final FinanceiroDAO financeiroDAO = new FinanceiroDAO();
    private final ProdutoController produtoController = new ProdutoController();

    


    public boolean finalizarVenda(
            Venda venda,
            List<VendaProduto> itens,
            FormaPagamento formaPagamento,
            TipoConta tipoConta,
            Double desconto,
            Integer quantidadeParcelas,
            Integer prazoDias) {

        logger.info("Executando finalizar venda.");

        LocalDate dataPrimeiraParcela = LocalDate.now();

        

        if (vendaDAO.contarVendasPorCpfNoMes(
                venda.getCliente().getCpf()) >= 3) {

            logger.warn("Cliente atingiu o limite de vendas no mes. CPF: {}",
                    venda.getCliente().getCpf());

            return false;
        }

        

        for (VendaProduto item : itens) {

            Produto produto = item.getProduto();

            Produto banco =
                    produtoController.pesquisar(produto.getId());

            if (banco == null) {

                logger.warn("Produto inexistente na venda. ID: {}",
                        produto.getId());

                return false;
            }

                if (banco.getQtdEstoque() <= 1) {

                logger.warn(
                "Produto sem estoque. Produto ID: {}, estoque: {}",
                produto.getId(),
                banco.getQtdEstoque());

                return false;
        }

            if (banco.getQtdEstoque() < item.getQuantidade()) {

                logger.warn(
                        "Estoque insuficiente na venda. Produto ID: {}, estoque: {}, solicitado: {}",
                        produto.getId(),
                        banco.getQtdEstoque(),
                        item.getQuantidade());

                return false;
            }

        }

        double subtotal = 0;

        for (VendaProduto item : itens) {

            subtotal +=
                    item.getQuantidade()
                    * item.getValorUnitario();

            venda.adicionarProduto(item);

        }

        if (desconto == null) {
            desconto = 0.0;
        }

        venda.setValorTotal(subtotal - desconto);

        if (!vendaDAO.salvar(venda)) {

            logger.error("Falha ao salvar venda.");

            return false;

        }

        

        for (VendaProduto item : itens) {

            produtoController.baixarEstoque(
                    item.getProduto(),
                    item.getQuantidade());

        }

        

        Financeiro financeiro = new Financeiro();

        financeiro.setVenda(venda);

        financeiro.setTipoConta(tipoConta);

        financeiro.setFormaPagamento(formaPagamento);

        financeiro.setDataLancamento(dataPrimeiraParcela);

        financeiro.setPagarOuReceber(1);

        financeiro.setValorTotal(venda.getValorTotal());

                gerarParcelas(
                financeiro,
                dataPrimeiraParcela,
                quantidadeParcelas,
                prazoDias,
                venda.getValorTotal());

        if (!financeiroDAO.salvar(financeiro)) {

            logger.error("Falha ao salvar financeiro da venda.");

            return false;

        }

        logger.info("Venda finalizada com sucesso. ID: {}, valor: {}",
                venda.getId(),
                venda.getValorTotal());

        return true;
    }

    


    private void gerarParcelas(
            Financeiro financeiro,
            LocalDate dataPrimeiraParcela,
            Integer quantidadeParcelas,
            Integer prazoDias,
            Double valorTotal) {

        logger.info(
                "Executando gerar parcelas da venda. Parcelas: {}, prazo: {}, valor: {}",
                quantidadeParcelas,
                prazoDias,
                valorTotal);

        if (quantidadeParcelas == null || quantidadeParcelas <= 0) {
            logger.warn("Quantidade de parcelas invalida. Usando 1 parcela.");
            quantidadeParcelas = 1;
        }

        if (prazoDias == null || prazoDias <= 0) {
            logger.warn("Prazo de parcelas invalido. Usando 30 dias.");
            prazoDias = 30;
        }

        double valorParcela = valorTotal / quantidadeParcelas;

        valorParcela =
                Math.round(valorParcela * 100.0) / 100.0;

        double somaParcelas = 0.0;

        for (int i = 1; i <= quantidadeParcelas; i++) {

            FinanceiroParcela parcela =
                    new FinanceiroParcela();

            parcela.setNumeroParcela(i);

            parcela.setDataVencimento(
                    dataPrimeiraParcela.plusDays(
                            (long) (i - 1) * prazoDias));

            parcela.setValorOriginal(valorParcela);

            parcela.setDesconto(0.0);

            parcela.setAcrescimo(0.0);

            parcela.setValorFinal(valorParcela);

            parcela.setStatus(0);

            somaParcelas += valorParcela;

            financeiro.adicionarParcela(parcela);

            logger.info("Parcela da venda gerada. Numero: {}, vencimento: {}, valor: {}",
                    i,
                    parcela.getDataVencimento(),
                    parcela.getValorFinal());

        }

        double diferenca =
                Math.round((valorTotal - somaParcelas) * 100.0)
                        / 100.0;

        if (Math.abs(diferenca) > 0) {

            FinanceiroParcela ultima =
                    financeiro.getParcelas()
                            .get(financeiro.getParcelas().size() - 1);

            ultima.setValorOriginal(
                    ultima.getValorOriginal() + diferenca);

            ultima.setValorFinal(
                    ultima.getValorFinal() + diferenca);

            logger.info("Ajuste de arredondamento aplicado na ultima parcela: {}",
                    diferenca);

        }

        logger.info("Geracao de parcelas da venda concluida. Total parcelas: {}",
                financeiro.getParcelas().size());

    }

}
