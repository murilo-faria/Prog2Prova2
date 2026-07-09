package venda.siscom.relatorio;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RelatorioVenda {

    public File gerarPdf() {

        String dataHora =
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        return new GeradorRelatorioPdf().gerar(
                "src/main/java/venda/siscom/report/RelatorioVenda.jrxml",
                "relatorio_vendas_" + dataHora + ".pdf");
    }
}