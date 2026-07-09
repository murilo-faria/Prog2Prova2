package venda.siscom.view;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import venda.siscom.relatorio.RelatorioCompra;
import venda.siscom.relatorio.RelatorioContasPagarReceber;
import venda.siscom.relatorio.RelatorioVenda;


public class PrincipalView extends JFrame {

    public PrincipalView() {

        setTitle("SISCOM - Sistema de Gestão Comercial");

        setSize(1000, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        inicializarMenu();

        inicializarComponentes();
    }

    private void inicializarMenu() {

        JMenuBar barra = new JMenuBar();

        /* CADASTROS */

        JMenu menuCadastros =
                new JMenu("Cadastros");

        JMenuItem itemCliente =
                new JMenuItem("Clientes");

        itemCliente.addActionListener(e ->
                new ClienteView().setVisible(true));

        JMenuItem itemFornecedor =
                new JMenuItem("Fornecedores");

        itemFornecedor.addActionListener(e ->
                new FornecedorView().setVisible(true));

        JMenuItem itemCategoria =
                new JMenuItem("Categorias");

        itemCategoria.addActionListener(e ->
                new CategoriaView().setVisible(true));

        JMenuItem itemProduto =
                new JMenuItem("Produtos");

        itemProduto.addActionListener(e ->
                new ProdutoView().setVisible(true));

        JMenuItem itemFornecedorProduto =
        new JMenuItem("Fornecedor x Produto");

        itemFornecedorProduto.addActionListener(e ->
        new FornecedorProdutoView().setVisible(true));

        JMenuItem itemFormaPagamento =
                new JMenuItem("Forma de Pagamento");

        itemFormaPagamento.addActionListener(e ->
                new FormaPagamentoView().setVisible(true));

        JMenuItem itemTipoConta =
                new JMenuItem("Tipo de Conta");

        itemTipoConta.addActionListener(e ->
                new TipoContaView().setVisible(true));

        menuCadastros.add(itemCliente);
        menuCadastros.add(itemFornecedor);
        menuCadastros.add(itemCategoria);
        menuCadastros.add(itemProduto);
        menuCadastros.add(itemFornecedorProduto);
        menuCadastros.addSeparator();
        menuCadastros.add(itemFormaPagamento);
        menuCadastros.add(itemTipoConta);   

        barra.add(menuCadastros);

        /*
         * ==========================
         * MOVIMENTOS
         * ==========================
         */

        JMenu menuMovimentos =
                new JMenu("Movimentos");

        JMenuItem itemCompra =
                new JMenuItem("Compras");

        itemCompra.addActionListener(e ->
                new CompraView().setVisible(true));

        JMenuItem itemVenda =
                new JMenuItem("Vendas");

        itemVenda.addActionListener(e ->
                new VendaView().setVisible(true));

        JMenuItem itemFinanceiro =
                new JMenuItem("Financeiro");

        itemFinanceiro.addActionListener(e ->
                new FinanceiroView().setVisible(true));

        menuMovimentos.add(itemCompra);
        menuMovimentos.add(itemVenda);
        menuMovimentos.addSeparator();
        menuMovimentos.add(itemFinanceiro);

        barra.add(menuMovimentos);

                /*
         * ==========================
         * RELATÓRIOS
         * ==========================
         */

        JMenu menuRelatorios =
                new JMenu("Relatórios");

        JMenuItem itemRelCompra =
                new JMenuItem("Relatório de Compras");

        itemRelCompra.addActionListener(e ->
                mostrarRelatorio(
                        new RelatorioCompra().gerarPdf()));

        JMenuItem itemRelVenda =
                new JMenuItem("Relatório de Vendas");

        itemRelVenda.addActionListener(e ->
                mostrarRelatorio(
                        new RelatorioVenda().gerarPdf()));


        JMenuItem itemRelContas =
                new JMenuItem("Contas a Pagar / Receber");

        itemRelContas.addActionListener(e ->
                mostrarRelatorio(
                        new RelatorioContasPagarReceber().gerarPdf()));

        menuRelatorios.add(itemRelCompra);
        menuRelatorios.add(itemRelVenda);
        menuRelatorios.add(itemRelContas);

        barra.add(menuRelatorios);

        /*
         * ==========================
         * SISTEMA
         * ==========================
         */

        JMenu menuSistema =
                new JMenu("Sistema");

        JMenuItem itemLogout =
                new JMenuItem("Logout");

        itemLogout.addActionListener(e -> {

            dispose();

            new LoginView();

        });

        JMenuItem itemSair =
                new JMenuItem("Sair");

        itemSair.addActionListener(e ->
                System.exit(0));

        menuSistema.add(itemLogout);
        menuSistema.addSeparator();
        menuSistema.add(itemSair);

        barra.add(menuSistema);

        setJMenuBar(barra);
    }

    private void inicializarComponentes() {

        JPanel painel =
                new JPanel(new BorderLayout());

        JLabel titulo =
                new JLabel(
                        "<html><center>"
                        + "<h1>SISCOM</h1>"
                        + "<br>"
                        + "<h2>Sistema de Gestão Comercial</h2>"
                        + "<br><br>"
                        + "Trabalho de Programação II"
                        + "</center></html>",
                        SwingConstants.CENTER);

        painel.add(titulo, BorderLayout.CENTER);

        add(painel);
    }

    private void mostrarRelatorio(File arquivo) {

        if (arquivo == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar o relatório.");

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Relatório gerado em:\n"
                        + arquivo.getAbsolutePath());
    }

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() ->

                new PrincipalView().setVisible(true)

        );
    }
}
