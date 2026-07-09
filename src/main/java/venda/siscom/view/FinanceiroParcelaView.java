package venda.siscom.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.controller.FinanceiroParcelaController;
import venda.siscom.model.Financeiro;
import venda.siscom.model.FinanceiroParcela;
import venda.siscom.util.FormatadorMoeda;

public class FinanceiroParcelaView extends JFrame {

    private static final Logger logger =
            LoggerFactory.getLogger(FinanceiroParcelaView.class);

    private final Financeiro financeiro;

    private JTable tabela;

    private DefaultTableModel modeloTabela;

    private JTextField txtNumero;
    private JTextField txtVencimento;
    private JTextField txtValorOriginal;
    private JTextField txtAcrescimo;
    private JTextField txtDesconto;
    private JTextField txtValorFinal;
    private JTextField txtStatus;

    private JButton btnAtualizar;
    private JButton btnBaixar;
    private JButton btnFechar;

    private FinanceiroParcela parcelaSelecionada;

    private final FinanceiroParcelaController controller =
            new FinanceiroParcelaController();

    public FinanceiroParcelaView(Financeiro financeiro) {

        this.financeiro = financeiro;

        setTitle("Parcelas da Transação");

        setSize(900,600);

        setLocationRelativeTo(null);

        setResizable(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        carregarTabela();

        configurarEventos();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        modeloTabela = new DefaultTableModel(

                new Object[]{
                        "Parcela",
                        "Vencimento",
                        "Valor",
                        "Status"
                },0){

            @Override
            public boolean isCellEditable(int row,int column){

                return false;
            }

        };

        tabela = new JTable(modeloTabela);

        tabela.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabela),
                BorderLayout.CENTER);

        JPanel painelCampos =
                new JPanel(new GridLayout(7,2,5,5));

        painelCampos.add(new JLabel("Parcela"));

        txtNumero = new JTextField();
        txtNumero.setEditable(false);
        painelCampos.add(txtNumero);

        painelCampos.add(new JLabel("Vencimento"));

        txtVencimento = new JTextField();
        txtVencimento.setEditable(false);
        painelCampos.add(txtVencimento);

        painelCampos.add(new JLabel("Valor Original"));

        txtValorOriginal = new JTextField();
        txtValorOriginal.setEditable(false);
        painelCampos.add(txtValorOriginal);

        painelCampos.add(new JLabel("Acréscimo"));

        txtAcrescimo = new JTextField("0,00");
        painelCampos.add(txtAcrescimo);

        painelCampos.add(new JLabel("Desconto"));

        txtDesconto = new JTextField("0,00");
        painelCampos.add(txtDesconto);

        painelCampos.add(new JLabel("Valor Final"));

        txtValorFinal = new JTextField();
        txtValorFinal.setEditable(false);
        painelCampos.add(txtValorFinal);

        painelCampos.add(new JLabel("Status"));

        txtStatus = new JTextField();
        txtStatus.setEditable(false);
        painelCampos.add(txtStatus);

        add(painelCampos,BorderLayout.NORTH);

        JPanel painelBotoes =
                new JPanel(new FlowLayout());

        btnAtualizar =
                new JButton("Atualizar Valor");

        btnBaixar =
                new JButton("Baixar Parcela");

        btnFechar =
                new JButton("Fechar");

        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnBaixar);
        painelBotoes.add(btnFechar);

        add(painelBotoes,
                BorderLayout.SOUTH);
    }

        private void carregarTabela() {

        modeloTabela.setRowCount(0);

        if (financeiro == null) {
            return;
        }

        List<FinanceiroParcela> parcelas =
                financeiro.getParcelas();

        if (parcelas == null) {
            return;
        }

        for (FinanceiroParcela p : parcelas) {

            modeloTabela.addRow(new Object[]{

                    p.getNumeroParcela(),

                    p.getDataVencimento(),

                    FormatadorMoeda.formatar(
                            p.getValorFinal()),

                    p.getStatus() == 0
                            ? "Pendente"
                            : "Pago"

            });

        }
    }

    private void configurarEventos() {

        tabela.getSelectionModel()
                .addListSelectionListener(e -> {

            int linha = tabela.getSelectedRow();

            if (linha < 0) {
                return;
            }

            parcelaSelecionada =
                    financeiro.getParcelas().get(linha);

            txtNumero.setText(
                    String.valueOf(
                            parcelaSelecionada.getNumeroParcela()));

            txtVencimento.setText(
                    parcelaSelecionada
                            .getDataVencimento()
                            .toString());

            txtValorOriginal.setText(
                    FormatadorMoeda.formatar(
                            parcelaSelecionada
                                    .getValorOriginal()));

            txtAcrescimo.setText(
                    FormatadorMoeda.formatar(
                            parcelaSelecionada
                                    .getAcrescimo()));

            txtDesconto.setText(
                    FormatadorMoeda.formatar(
                            parcelaSelecionada
                                    .getDesconto()));

            txtValorFinal.setText(
                    FormatadorMoeda.formatar(
                            parcelaSelecionada
                                    .getValorFinal()));

            txtStatus.setText(

                    parcelaSelecionada.getStatus() == 0

                            ? "Pendente"

                            : "Pago");

        });

        btnAtualizar.addActionListener(e -> atualizarValor());

        btnBaixar.addActionListener(e -> baixarParcela());

        btnFechar.addActionListener(e -> dispose());

    }

    private void atualizarValor() {

    if (parcelaSelecionada == null) {

        JOptionPane.showMessageDialog(
                this,
                "Selecione uma parcela.");

        return;
    }

    try {

        double acrescimo =
                FormatadorMoeda.converter(
                        txtAcrescimo.getText());

        double desconto =
                FormatadorMoeda.converter(
                        txtDesconto.getText());

        double valorFinal =
                parcelaSelecionada.getValorOriginal()
                + acrescimo
                - desconto;

        parcelaSelecionada.setAcrescimo(acrescimo);
        parcelaSelecionada.setDesconto(desconto);
        parcelaSelecionada.setValorFinal(valorFinal);

        controller.alterar(parcelaSelecionada);

        txtValorFinal.setText(
                FormatadorMoeda.formatar(valorFinal));

        int linha = tabela.getSelectedRow();

        if (linha >= 0) {

            modeloTabela.setValueAt(
                    FormatadorMoeda.formatar(valorFinal),
                    linha,
                    2);
        }

        JOptionPane.showMessageDialog(
                this,
                "Valor atualizado com sucesso.");

         } catch (Exception ex) {

        logger.warn("Valores invalidos ao atualizar parcela financeira.", ex);

        JOptionPane.showMessageDialog(
                this,
                "Valores inválidos.");
        }
        }

        private void baixarParcela() {

        if (parcelaSelecionada == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma parcela.");

            return;
        }

        try {

            double acrescimo =
                    FormatadorMoeda.converter(
                            txtAcrescimo.getText());

            double desconto =
                    FormatadorMoeda.converter(
                            txtDesconto.getText());

            controller.baixarParcela(
                    parcelaSelecionada,
                    acrescimo,
                    desconto);

            txtValorFinal.setText(
                    FormatadorMoeda.formatar(
                            parcelaSelecionada.getValorFinal()));

            txtStatus.setText("Pago");

            int linha = tabela.getSelectedRow();

            if (linha >= 0) {

                modeloTabela.setValueAt(
                        FormatadorMoeda.formatar(
                                parcelaSelecionada.getValorFinal()),
                        linha,
                        2);

                modeloTabela.setValueAt(
                        "Pago",
                        linha,
                        3);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Parcela baixada com sucesso.");

        } catch (Exception ex) {

            logger.error("Erro ao baixar parcela financeira.", ex);

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao baixar parcela.");
        }

    }

}
