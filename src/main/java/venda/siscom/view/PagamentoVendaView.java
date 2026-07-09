package venda.siscom.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.controller.FormaPagamentoController;
import venda.siscom.controller.TipoContaController;
import venda.siscom.controller.VendaController;
import venda.siscom.model.FormaPagamento;
import venda.siscom.model.TipoConta;
import venda.siscom.model.Venda;
import venda.siscom.model.VendaProduto;
import venda.siscom.util.FormatadorMoeda;

public class PagamentoVendaView extends JDialog {

    private static final Logger logger =
            LoggerFactory.getLogger(PagamentoVendaView.class);

    private final JFrame parent;

    private final Venda vendaAtual;
    private final List<VendaProduto> itensVenda;

    private JTextField txtValorTotalVenda;
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private JComboBox<TipoConta> cbTipoConta;
    private JTextField txtDesconto;
    private JTextField txtQuantidadeParcelas;
    private JTextField txtPrazoParcelas;
    private JTextField txtValorFinal;

    private JButton btnConfirmar;
    private JButton btnCancelar;

    private final VendaController vendaController =
            new VendaController();

    private final FormaPagamentoController formaPagamentoController =
            new FormaPagamentoController();

    private final TipoContaController tipoContaController =
            new TipoContaController();

    public PagamentoVendaView(
            JFrame parent,
            Venda venda,
            List<VendaProduto> itens) {

        super(parent, "Pagamento da Venda", true);

        this.parent = parent;
        this.vendaAtual = venda;
        this.itensVenda = itens;

        configurarJanela();
        inicializarComponentes();
        carregarCombos();

        
    for (int i = 0; i < cbTipoConta.getItemCount(); i++) {  

         TipoConta tipo = cbTipoConta.getItemAt(i);

        if (tipo.getDescricao().equalsIgnoreCase("Venda")) {

        cbTipoConta.setSelectedIndex(i);
        break;
            }
        }

        
        cbTipoConta.setEnabled(false);

        carregarValores();
        configurarEventos();
    }

    private void configurarJanela() {

        setSize(420, 350);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {

        JPanel painel = new JPanel(new GridLayout(7, 2, 5, 8));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        painel.add(new JLabel("Valor da Venda"));
        txtValorTotalVenda = new JTextField();
        txtValorTotalVenda.setEditable(false);
        painel.add(txtValorTotalVenda);

        painel.add(new JLabel("Forma de Pagamento"));
        cbFormaPagamento = new JComboBox<>();
        painel.add(cbFormaPagamento);

        painel.add(new JLabel("Tipo de Conta"));
        cbTipoConta = new JComboBox<>();
        painel.add(cbTipoConta);

        painel.add(new JLabel("Desconto"));
        txtDesconto = new JTextField("0,00");
        painel.add(txtDesconto);

        painel.add(new JLabel("Quantidade Parcelas"));
        txtQuantidadeParcelas = new JTextField("1");
        txtQuantidadeParcelas.setEditable(false);
        painel.add(txtQuantidadeParcelas);

        painel.add(new JLabel("Prazo entre Parcelas"));
        txtPrazoParcelas = new JTextField("30");
        txtPrazoParcelas.setEditable(false);
        painel.add(txtPrazoParcelas);

        painel.add(new JLabel("Valor Final"));
        txtValorFinal = new JTextField();
        txtValorFinal.setEditable(false);
        painel.add(txtValorFinal);

        add(painel, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout());

        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");

        botoes.add(btnConfirmar);
        botoes.add(btnCancelar);

        add(botoes, BorderLayout.SOUTH);
    }

    private void carregarCombos() {

        cbFormaPagamento.removeAllItems();

        List<FormaPagamento> formas =
                formaPagamentoController.pesquisarTodos();

        if (formas != null) {

            for (FormaPagamento fp : formas) {
                cbFormaPagamento.addItem(fp);
            }
        }

        cbTipoConta.removeAllItems();

        List<TipoConta> tipos =
                tipoContaController.pesquisarTodos();

        if (tipos != null) {

            for (TipoConta tipo : tipos) {
                cbTipoConta.addItem(tipo);
            }
        }

        if (cbFormaPagamento.getItemCount() > 0) {

            cbFormaPagamento.setSelectedIndex(0);

            FormaPagamento forma =
                    (FormaPagamento) cbFormaPagamento.getSelectedItem();

            if (forma != null) {

                txtQuantidadeParcelas.setText(
                        String.valueOf(forma.getQtdeParcela()));

                txtPrazoParcelas.setText(
                        String.valueOf(forma.getPrazo()));
            }
        }
    }

        private void carregarValores() {

        txtValorTotalVenda.setText(
                FormatadorMoeda.formatar(vendaAtual.getValorTotal()));

        txtValorFinal.setText(
                txtValorTotalVenda.getText());
    }

    private void configurarEventos() {

        txtDesconto.addActionListener(
                e -> calcularValorFinal());

        txtDesconto.addFocusListener(
                new java.awt.event.FocusAdapter() {

                    @Override
                    public void focusLost(
                            java.awt.event.FocusEvent e) {

                        calcularValorFinal();
                    }

                });

        cbFormaPagamento.addActionListener(e -> {

            FormaPagamento forma =
                    (FormaPagamento)
                            cbFormaPagamento.getSelectedItem();

            if (forma != null) {

                txtQuantidadeParcelas.setText(
                        String.valueOf(
                                forma.getQtdeParcela()));

                txtPrazoParcelas.setText(
                        String.valueOf(
                                forma.getPrazo()));

                calcularValorFinal();
            }

        });

        btnCancelar.addActionListener(e -> {

            dispose();

            parent.setVisible(true);

        });

        btnConfirmar.addActionListener(
                e -> confirmarPagamento());

    }

    private void calcularValorFinal() {

        try {

            double total =
                    FormatadorMoeda.converter(
                            txtValorTotalVenda.getText());

            double desconto =
                    FormatadorMoeda.converter(
                            txtDesconto.getText());

            double valorFinal =
                    total - desconto;

            if (valorFinal < 0) {

                valorFinal = 0;

            }

            txtValorFinal.setText(
                    FormatadorMoeda.formatar(valorFinal));

        } catch (Exception e) {

            txtValorFinal.setText(
                    txtValorTotalVenda.getText());

        }

    }

    private void confirmarPagamento() {

        try {

            FormaPagamento forma =
                    (FormaPagamento)
                            cbFormaPagamento.getSelectedItem();

            TipoConta tipo =
                    (TipoConta)
                            cbTipoConta.getSelectedItem();

            if (forma == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecione uma forma de pagamento.");

                return;
            }

            if (tipo == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um tipo de conta.");

                return;
            }

            double desconto =
                    FormatadorMoeda.converter(
                            txtDesconto.getText());

            int parcelas =
                    forma.getQtdeParcela();

            int prazo =
                    forma.getPrazo();

            boolean sucesso =
                    vendaController.finalizarVenda(
                            vendaAtual,
                            itensVenda,
                            forma,
                            tipo,
                            desconto,
                            parcelas,
                            prazo);

            if (sucesso) {

                JOptionPane.showMessageDialog(
                        this,
                        "Venda realizada com sucesso!");

                dispose();

                parent.dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao finalizar a venda.");

            }

        } catch (Exception ex) {

            logger.error("Erro ao finalizar pagamento da venda.", ex);

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao finalizar a venda.");

        }

    }

}
