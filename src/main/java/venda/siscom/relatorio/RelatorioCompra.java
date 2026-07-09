package venda.siscom.relatorio;

import java.io.File;

public class RelatorioCompra {

    public File gerarPdf() {

        return new GeradorRelatorioPdf().gerar(
                "src/main/java/venda/siscom/report/RelatorioCompra.jrxml",
                "relatorio_compras.pdf");
    }
}
