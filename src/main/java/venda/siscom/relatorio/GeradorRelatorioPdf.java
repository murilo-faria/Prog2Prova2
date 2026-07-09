package venda.siscom.relatorio;

import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class GeradorRelatorioPdf {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/SisCom_comercial_prog2_Murilo";

    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    public File gerar(String caminhoJrxml, String nomeArquivo) {

        try {

            File arquivoJrxml = new File(caminhoJrxml);

            if (!arquivoJrxml.exists()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Arquivo do relatorio nao encontrado:\n"
                                + caminhoJrxml);
                return null;
            }

            Class.forName("org.postgresql.Driver");

            try (Connection conexao =
                    DriverManager.getConnection(URL, USUARIO, SENHA)) {

                JasperReport relatorio =
                        JasperCompileManager.compileReport(caminhoJrxml);

                Map<String, Object> parametros =
                        new HashMap<>();

                JasperPrint impressao =
                        JasperFillManager.fillReport(
                                relatorio,
                                parametros,
                                conexao);

                File pasta = new File("relatorios");

                if (!pasta.exists()) {
                    pasta.mkdirs();
                }

                File pdf = new File(pasta, nomeArquivo);

                JasperExportManager.exportReportToPdfFile(
                        impressao,
                        pdf.getAbsolutePath());

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdf);
                }

                return pdf;
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao gerar relatorio:\n" + e.getMessage());

            return null;
        }
    }
}
