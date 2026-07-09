package venda.siscom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.FinanceiroDAO;
import venda.siscom.model.Financeiro;
import venda.siscom.model.FinanceiroParcela;
import venda.siscom.model.FormaPagamento;
import venda.siscom.model.TipoConta;

public class FinanceiroController {

    private static final Logger logger = LoggerFactory.getLogger(FinanceiroController.class);

    private final FinanceiroDAO financeiroDAO = new FinanceiroDAO();

    // SALVAR
    public boolean salvar(Financeiro financeiro) {

        logger.info("Iniciando salvamento do lançamento financeiro.");

        boolean resultado = financeiroDAO.salvar(financeiro);

        if (resultado) {
            logger.info("Lançamento financeiro salvo com sucesso.");
        } else {
            logger.warn("Falha ao salvar lançamento financeiro.");
        }

        return resultado;
    }

    // ALTERAR
    public boolean alterar(Financeiro financeiro) {

        logger.info("Iniciando alteração do lançamento financeiro.");

        boolean resultado = financeiroDAO.alterar(financeiro);

        if (resultado) {
            logger.info("Lançamento financeiro alterado com sucesso.");
        } else {
            logger.warn("Falha ao alterar lançamento financeiro.");
        }

        return resultado;
    }

    // EXCLUIR
    public boolean excluir(Integer id) {

        logger.info("Excluindo lançamento financeiro. ID: {}", id);

        boolean resultado = financeiroDAO.excluir(id);

        if (resultado) {
            logger.info("Lançamento financeiro excluído com sucesso.");
        } else {
            logger.warn("Falha ao excluir lançamento financeiro. ID: {}", id);
        }

        return resultado;
    }

    // PESQUISAR
    public Financeiro pesquisar(Integer id) {

        logger.info("Pesquisando lançamento financeiro. ID: {}", id);

        Financeiro financeiro = financeiroDAO.pesquisar(id);

        if (financeiro != null) {
            logger.info("Lançamento financeiro encontrado.");
        } else {
            logger.warn("Lançamento financeiro não encontrado. ID: {}", id);
        }

        return financeiro;
    }

    // LISTAR TODOS
    public List<Financeiro> pesquisarTodos() {

        logger.info("Listando todos os lançamentos financeiros.");

        List<Financeiro> lista = financeiroDAO.pesquisarTodos();

        logger.info("Total de lançamentos encontrados: {}",
                lista == null ? 0 : lista.size());

        return lista;
    }

    public List<Financeiro> pesquisarFiltros(
            TipoConta tipoConta,
            FormaPagamento formaPagamento,
            Integer pagarOuReceber) {

        logger.info(
                "Pesquisando lancamentos financeiros com filtros. Tipo: {}, Forma: {}, Movimentacao: {}",
                tipoConta,
                formaPagamento,
                pagarOuReceber);

        List<Financeiro> lista =
                financeiroDAO.pesquisarFiltros(
                        tipoConta,
                        formaPagamento,
                        pagarOuReceber);

        logger.info("Total de lancamentos encontrados: {}",
                lista == null ? 0 : lista.size());

        return lista;
    }

    // SALVAR LANÇAMENTO MANUAL
    public boolean salvarManual(Financeiro financeiro) {

        logger.info("Iniciando salvamento manual do lançamento financeiro.");

        if (financeiro == null) {
            logger.warn("Financeiro informado é nulo.");
            return false;
        }

        if (financeiro.getParcelas() == null
                || financeiro.getParcelas().isEmpty()) {

            logger.info("Gerando parcelas automaticamente.");

            int qtdeParcelas =
                    financeiro.getFormaPagamento().getQtdeParcela();

            int prazo =
                    financeiro.getFormaPagamento().getPrazo();

            double valorParcela =
                    financeiro.getValorTotal() / qtdeParcelas;

            valorParcela =
                    Math.round(valorParcela * 100.0) / 100.0;

            double somaParcelas = 0;

            for (int i = 1; i <= qtdeParcelas; i++) {

                FinanceiroParcela parcela = new FinanceiroParcela();

                parcela.setNumeroParcela(i);

                parcela.setValorOriginal(valorParcela);

                parcela.setValorFinal(valorParcela);

                parcela.setDesconto(0.0);

                parcela.setAcrescimo(0.0);

                parcela.setStatus(0);

                parcela.setDataVencimento(
                        financeiro.getDataLancamento()
                                .plusDays((long) (i - 1) * prazo));

                somaParcelas += valorParcela;

                financeiro.adicionarParcela(parcela);

                logger.info("Parcela {} gerada.", i);
            }

            // Corrige diferença de arredondamento
            double diferenca =
                    Math.round((financeiro.getValorTotal() - somaParcelas) * 100.0)
                    / 100.0;

            if (Math.abs(diferenca) > 0) {

                logger.info("Aplicando ajuste de arredondamento na última parcela.");

                FinanceiroParcela ultima =
                        financeiro.getParcelas()
                                .get(financeiro.getParcelas().size() - 1);

                ultima.setValorOriginal(
                        ultima.getValorOriginal() + diferenca);

                ultima.setValorFinal(
                        ultima.getValorFinal() + diferenca);
            }
        }

        boolean resultado = financeiroDAO.salvar(financeiro);

        if (resultado) {
            logger.info("Lançamento financeiro manual salvo com sucesso.");
        } else {
            logger.error("Erro ao salvar lançamento financeiro manual.");
        }

        return resultado;
    }

}
