package venda.siscom.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.controller.FinanceiroController;
import venda.siscom.controller.FormaPagamentoController;
import venda.siscom.controller.TipoContaController;
import venda.siscom.model.Financeiro;
import venda.siscom.model.FormaPagamento;
import venda.siscom.model.TipoConta;
import venda.siscom.util.FormatadorMoeda;

public class FinanceiroView extends JFrame {

    private static final Logger logger =
            LoggerFactory.getLogger(FinanceiroView.class);

    private JTextField txtId;
    private JTextField txtDescricao;
    private JTextField txtValorTotal;

    private JComboBox<TipoConta> cbTipoConta;
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private JComboBox<String> cbPagarReceber;
    private JComboBox<Object> cbFiltroTipoConta;
    private JComboBox<Object> cbFiltroFormaPagamento;
    private JComboBox<String> cbFiltroMovimentacao;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnBuscar;
    private JButton btnLimparPesquisa;
    private JButton btnParcelas;
    private JButton btnLimpar;

    private final FinanceiroController financeiroController =
            new FinanceiroController();

    private final TipoContaController tipoContaController =
            new TipoContaController();

    private final FormaPagamentoController formaPagamentoController =
            new FormaPagamentoController();

    private List<Financeiro> listaFinanceiro =
            new ArrayList<>();

    private Financeiro financeiroSelecionado;

    public FinanceiroView() {

        setTitle("Financeiro");

        setSize(1050,700);

        setResizable(false);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        carregarCombos();

        atualizarTabela();

        configurarEventos();
    }

    private void inicializarComponentes() {

        JPanel painelPesquisa =
        new JPanel(new GridLayout(2,4,5,5));

        painelPesquisa.setBorder(
        javax.swing.BorderFactory.createTitledBorder("Pesquisar")); 

        painelPesquisa.add(new JLabel("Tipo Conta"));

        cbFiltroTipoConta = new JComboBox<>();

        painelPesquisa.add(cbFiltroTipoConta);

        painelPesquisa.add(new JLabel("Forma Pagamento"));

        cbFiltroFormaPagamento = new JComboBox<>();

        painelPesquisa.add(cbFiltroFormaPagamento);

        painelPesquisa.add(new JLabel("Movimentação"));

        cbFiltroMovimentacao = new JComboBox<>();

        painelPesquisa.add(cbFiltroMovimentacao);

        btnBuscar =
                new JButton("Buscar");

        btnLimparPesquisa =
                new JButton("Limpar Filtro");

        painelPesquisa.add(btnBuscar);
        painelPesquisa.add(btnLimparPesquisa);

        setLayout(new BorderLayout());

        JPanel painelCampos =
                new JPanel(new GridLayout(6,2,5,5));

        painelCampos.add(new JLabel("Código"));

        txtId = new JTextField();
        txtId.setEditable(false);
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Descrição"));

        txtDescricao = new JTextField();
        painelCampos.add(txtDescricao);

        painelCampos.add(new JLabel("Valor"));

        txtValorTotal = new JTextField();
        painelCampos.add(txtValorTotal);

        painelCampos.add(new JLabel("Tipo Conta"));

        cbTipoConta = new JComboBox<>();
        painelCampos.add(cbTipoConta);

        painelCampos.add(new JLabel("Forma Pagamento"));

        cbFormaPagamento = new JComboBox<>();
        painelCampos.add(cbFormaPagamento);

        painelCampos.add(new JLabel("Movimentação"));

        cbPagarReceber =
                new JComboBox<>(
                        new String[]{
                                "Pagar",
                                "Receber"
                        });

        painelCampos.add(cbPagarReceber);

        add(painelCampos, BorderLayout.NORTH);

        modeloTabela =
                new DefaultTableModel(

                        new Object[]{
                                "ID",
                                "Descrição",
                                "Tipo",
                                "Forma",
                                "Valor",
                                "Movimentação"
                        },0){

            @Override
            public boolean isCellEditable(
                    int row,
                    int column){

                return false;
            }

        };

        tabela = new JTable(modeloTabela);

        JPanel painelCentro =
        new JPanel(new BorderLayout());

        painelCentro.add(
        new JScrollPane(tabela),
        BorderLayout.CENTER);

        painelCentro.add(
        painelPesquisa,
        BorderLayout.SOUTH);

        add(painelCentro,
        BorderLayout.CENTER);

        JPanel painelBotoes =
                new JPanel(new FlowLayout());

        btnSalvar =
                new JButton("Salvar");

        btnAlterar =
                new JButton("Alterar");

        btnExcluir =
                new JButton("Excluir");

        btnParcelas =
                new JButton("Parcelas");

        btnLimpar =
                new JButton("Limpar");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnParcelas);
        painelBotoes.add(btnLimpar);

        add(painelBotoes,
                BorderLayout.SOUTH);
    }

        private void carregarCombos() {

        cbTipoConta.removeAllItems();

        List<TipoConta> tipos =
                tipoContaController.pesquisarTodos();

        if (tipos != null) {

            for (TipoConta tipo : tipos) {

                cbTipoConta.addItem(tipo);

            }

        }

        cbFormaPagamento.removeAllItems();

        List<FormaPagamento> formas =
                formaPagamentoController.pesquisarTodos();

        if (formas != null) {

            for (FormaPagamento forma : formas) {

                cbFormaPagamento.addItem(forma);

            }

        }

        carregarFiltros(tipos, formas);

    }

    private void carregarFiltros(
            List<TipoConta> tipos,
            List<FormaPagamento> formas) {

        cbFiltroTipoConta.removeAllItems();
        cbFiltroTipoConta.addItem("Todos");

        if (tipos != null) {

            for (TipoConta tipo : tipos) {

                cbFiltroTipoConta.addItem(tipo);

            }
        }

        cbFiltroFormaPagamento.removeAllItems();
        cbFiltroFormaPagamento.addItem("Todos");

        if (formas != null) {

            for (FormaPagamento forma : formas) {

                cbFiltroFormaPagamento.addItem(forma);

            }
        }

        cbFiltroMovimentacao.removeAllItems();
        cbFiltroMovimentacao.addItem("Todos");
        cbFiltroMovimentacao.addItem("Pagar");
        cbFiltroMovimentacao.addItem("Receber");
    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        listaFinanceiro =
                financeiroController.pesquisarFiltros(
                        obterFiltroTipoConta(),
                        obterFiltroFormaPagamento(),
                        obterFiltroMovimentacao());

        if (listaFinanceiro == null) {

            listaFinanceiro = new ArrayList<>();

            return;

        }

        for (Financeiro financeiro : listaFinanceiro) {

            modeloTabela.addRow(new Object[]{

                    financeiro.getId(),

                    financeiro.getDescricao(),

                    financeiro.getTipoConta(),

                    financeiro.getFormaPagamento(),

                    FormatadorMoeda.formatar(
                            financeiro.getValorTotal()),

                    financeiro.getPagarOuReceber() == 0

                            ? "Pagar"

                            : "Receber"

            });

        }

    }

    private TipoConta obterFiltroTipoConta() {

        Object selecionado =
                cbFiltroTipoConta.getSelectedItem();

        if (selecionado instanceof TipoConta) {
            return (TipoConta) selecionado;
        }

        return null;
    }

    private FormaPagamento obterFiltroFormaPagamento() {

        Object selecionado =
                cbFiltroFormaPagamento.getSelectedItem();

        if (selecionado instanceof FormaPagamento) {
            return (FormaPagamento) selecionado;
        }

        return null;
    }

    private Integer obterFiltroMovimentacao() {

        int selecionado =
                cbFiltroMovimentacao.getSelectedIndex();

        if (selecionado == 1) {
            return 0;
        }

        if (selecionado == 2) {
            return 1;
        }

        return null;
    }

    private void limparCampos() {

        txtId.setText("");

        txtDescricao.setText("");

        txtValorTotal.setText("");

        if (cbTipoConta.getItemCount() > 0) {

            cbTipoConta.setSelectedIndex(0);

        }

        if (cbFormaPagamento.getItemCount() > 0) {

            cbFormaPagamento.setSelectedIndex(0);

        }

        cbPagarReceber.setSelectedIndex(0);

        tabela.clearSelection();

        financeiroSelecionado = null;

    }

        private void configurarEventos() {

        btnSalvar.addActionListener(e -> salvar());

        btnAlterar.addActionListener(e -> alterar());

        btnExcluir.addActionListener(e -> excluir());

        btnLimpar.addActionListener(e -> limparCampos());

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                int linha = tabela.getSelectedRow();

                if (linha < 0) {
                    return;
                }

                financeiroSelecionado =
                        listaFinanceiro.get(linha);

                txtId.setText(
                        String.valueOf(
                                financeiroSelecionado.getId()));

                txtDescricao.setText(
                        financeiroSelecionado.getDescricao());

                txtValorTotal.setText(
                        FormatadorMoeda.formatar(
                                financeiroSelecionado.getValorTotal()));

                cbTipoConta.setSelectedItem(
                        financeiroSelecionado.getTipoConta());

                cbFormaPagamento.setSelectedItem(
                        financeiroSelecionado.getFormaPagamento());

                cbPagarReceber.setSelectedIndex(
                        financeiroSelecionado.getPagarOuReceber());

            }

        });

        btnBuscar.addActionListener(e -> atualizarTabela());

        btnLimparPesquisa.addActionListener(e -> limparPesquisa());

        btnParcelas.addActionListener(e -> abrirParcelas());

    }

    private void limparPesquisa() {

        if (cbFiltroTipoConta.getItemCount() > 0) {
            cbFiltroTipoConta.setSelectedIndex(0);
        }

        if (cbFiltroFormaPagamento.getItemCount() > 0) {
            cbFiltroFormaPagamento.setSelectedIndex(0);
        }

        if (cbFiltroMovimentacao.getItemCount() > 0) {
            cbFiltroMovimentacao.setSelectedIndex(0);
        }

        atualizarTabela();
    }

    private void salvar() {

        try {

            Financeiro financeiro =
                    new Financeiro();

            financeiro.setDescricao(
                    txtDescricao.getText());

            financeiro.setValorTotal(
                    FormatadorMoeda.converter(
                            txtValorTotal.getText()));

            financeiro.setTipoConta(
                    (TipoConta) cbTipoConta.getSelectedItem());

            financeiro.setFormaPagamento(
                    (FormaPagamento) cbFormaPagamento.getSelectedItem());

            financeiro.setPagarOuReceber(
                    cbPagarReceber.getSelectedIndex());

            financeiro.setDataLancamento(
                    java.time.LocalDate.now());

            if (financeiroController.salvarManual(financeiro)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Lançamento salvo com sucesso.");

                limparCampos();

                atualizarTabela();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao salvar.");

            }

        } catch (Exception ex) {

            logger.warn("Dados invalidos ao salvar lancamento financeiro.", ex);

            JOptionPane.showMessageDialog(
                    this,
                    "Dados inválidos.");

        }

    }

    private void alterar() {

        if (financeiroSelecionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um lançamento.");

            return;
        }

        try {

            financeiroSelecionado.setDescricao(
                    txtDescricao.getText());

            financeiroSelecionado.setValorTotal(
                    FormatadorMoeda.converter(
                            txtValorTotal.getText()));

            financeiroSelecionado.setTipoConta(
                    (TipoConta) cbTipoConta.getSelectedItem());

            financeiroSelecionado.setFormaPagamento(
                    (FormaPagamento) cbFormaPagamento.getSelectedItem());

            financeiroSelecionado.setPagarOuReceber(
                    cbPagarReceber.getSelectedIndex());

            financeiroController.alterar(financeiroSelecionado);

            atualizarTabela();

            limparCampos();

        } catch (Exception ex) {

            logger.warn("Dados invalidos ao alterar lancamento financeiro.", ex);

            JOptionPane.showMessageDialog(
                    this,
                    "Dados inválidos.");
        }

    }

    private void excluir() {

        if (financeiroSelecionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um lançamento.");

            return;
        }

        financeiroController.excluir(
                financeiroSelecionado.getId());

        atualizarTabela();

        limparCampos();

    }

        private void abrirParcelas() {

        if (financeiroSelecionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um lançamento.");

            return;
        }

        if (financeiroSelecionado.getParcelas() == null
                || financeiroSelecionado.getParcelas().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Este lançamento não possui parcelas.");

            return;
        }

        FinanceiroParcelaView tela =
                new FinanceiroParcelaView(financeiroSelecionado);

        tela.setVisible(true);
    }

}
