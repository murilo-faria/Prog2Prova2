package venda.siscom.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.FinanceiroParcelaDAO;
import venda.siscom.model.Financeiro;
import venda.siscom.model.FinanceiroParcela;

public class FinanceiroParcelaController {

    private static final Logger logger = LoggerFactory.getLogger(FinanceiroParcelaController.class);

    private final FinanceiroParcelaDAO financeiroParcelaDAO = new FinanceiroParcelaDAO();

    // SALVAR
    public boolean salvar(FinanceiroParcela parcela) {
        logger.info("Iniciando salvamento da parcela.");
        
        return financeiroParcelaDAO.salvar(parcela);
    }

    // ALTERAR
    public boolean alterar(FinanceiroParcela parcela) {
        logger.info("Iniciando alteração da parcela.");
        
        return financeiroParcelaDAO.alterar(parcela);
    }

    // EXCLUIR
    public boolean excluir(Integer id) {
        logger.info("Excluindo parcela ID: {}", id);
        
        return financeiroParcelaDAO.excluir(id);
    }

    // PESQUISAR
    public FinanceiroParcela pesquisar(Integer id) {
        logger.info("Pesquisando parcela ID: {}", id);
        
        return financeiroParcelaDAO.pesquisar(id);
    }

    // LISTAR TODAS
    public List<FinanceiroParcela> pesquisarTodos() {
        logger.info("Listando todas as parcelas.");
        
        return financeiroParcelaDAO.pesquisarTodos();
    }

    // DAR BAIXA NA PARCELA
    public boolean baixarParcela(FinanceiroParcela parcela,
                                 Double acrescimo,
                                 Double desconto) {

        logger.info("Iniciando baixa da parcela.");

        if (parcela == null) {
            logger.warn("Parcela informada é nula.");
            return false;
        }

        if (acrescimo == null) {
            acrescimo = 0.0;
        }

        if (desconto == null) {
            desconto = 0.0;
        }

        parcela.setAcrescimo(acrescimo);

        parcela.setDesconto(desconto);

        parcela.setValorFinal(
                parcela.getValorOriginal()
                        + acrescimo
                        - desconto);

        parcela.setStatus(1); // 1 = PAGA

        parcela.setDataPagamento(LocalDate.now());

        logger.info("Baixa realizada com sucesso para parcela ID: {}", parcela.getId());

        return financeiroParcelaDAO.alterar(parcela);
    }

    // LISTAR PARCELAS DE UM FINANCEIRO
    public List<FinanceiroParcela> pesquisarPorFinanceiro(Financeiro financeiro) {
        logger.info("Listando parcelas para financeiro ID: {}", financeiro.getId());

        return financeiro.getParcelas();
    }

}