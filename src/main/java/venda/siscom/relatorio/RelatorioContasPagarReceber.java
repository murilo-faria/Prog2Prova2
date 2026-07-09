package venda.siscom.relatorio;

import java.io.File;

public class RelatorioContasPagarReceber {

    public File gerarPdf() {

        return new GeradorRelatorioPdf().gerar(
                "src/main/java/venda/siscom/report/RelatorioContasPagarReceber.jrxml",
                "relatorio_contas_pagar_receber.pdf");
    }
}
