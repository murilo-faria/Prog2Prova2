package venda.siscom.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

import venda.siscom.controller.FormaPagamentoController;
import venda.siscom.model.FormaPagamento;

public class FormaPagamentoView extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtQtdeParcela;
    private JTextField txtPrazo;

    private JComboBox<String> cbAvistaPrazo;

    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private JButton btnLimpar;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final FormaPagamentoController controller =
            new FormaPagamentoController();

    public FormaPagamentoView() {

        setTitle("Cadastro de Forma de Pagamento");

        setSize(700, 450);

        setResizable(false);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        configurarEventos();

        atualizarTabela();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JPanel painelCampos =
                new JPanel(new GridLayout(5, 2, 5, 5));

        painelCampos.add(new JLabel("Código"));

        txtId = new JTextField();
        txtId.setEditable(false);
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Nome"));

        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Quantidade de Parcelas"));

        txtQtdeParcela = new JTextField("1");
        painelCampos.add(txtQtdeParcela);

        painelCampos.add(new JLabel("Prazo entre Parcelas"));

        txtPrazo = new JTextField("30");
        painelCampos.add(txtPrazo);

        painelCampos.add(new JLabel("Tipo"));

        cbAvistaPrazo =
                new JComboBox<>(
                        new String[]{
                                "À Vista",
                                "A Prazo"
                        });

        painelCampos.add(cbAvistaPrazo);

        add(painelCampos, BorderLayout.NORTH);

        modeloTabela =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Nome",
                                "Parcelas",
                                "Prazo",
                                "Tipo"
                        }, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        tabela =
                new JTable(modeloTabela);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelBotoes =
                new JPanel(new FlowLayout());

        btnSalvar =
                new JButton("Salvar");

        btnAlterar =
                new JButton("Alterar");

        btnExcluir =
                new JButton("Excluir");

        btnPesquisar =
                new JButton("Pesquisar");

        btnLimpar =
                new JButton("Limpar");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnPesquisar);
        painelBotoes.add(btnLimpar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void configurarEventos() {

        btnSalvar.addActionListener(e -> salvar());

        btnAlterar.addActionListener(e -> alterar());

        btnExcluir.addActionListener(e -> excluir());

        btnPesquisar.addActionListener(e -> pesquisarPorNome());

        btnLimpar.addActionListener(e -> limparCampos());

        tabela.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (e.getValueIsAdjusting()) {

                        return;
                    }

                    int linha =
                            tabela.getSelectedRow();

                    if (linha < 0) {

                        return;
                    }

                    Integer id =
                            (Integer) modeloTabela.getValueAt(linha, 0);

                    FormaPagamento forma =
                            controller.pesquisar(id);

                    if (forma != null) {

                        preencherCampos(forma);
                    }
                });
    }

    private void salvar() {

        if (!validarCampos()) {

            return;
        }

        FormaPagamento forma =
                new FormaPagamento();

        forma.setNome(
                txtNome.getText().trim());

        forma.setQtdeParcela(
                Integer.parseInt(
                        txtQtdeParcela.getText().trim()));

        forma.setPrazo(
                Integer.parseInt(
                        txtPrazo.getText().trim()));

        forma.setAvistaPrazo(
                cbAvistaPrazo.getSelectedIndex());

        if (controller.salvar(forma)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Forma de pagamento salva com sucesso.");

            limparCampos();

            atualizarTabela();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao salvar forma de pagamento.");
        }
    }

    private void alterar() {

        if (txtId.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma forma de pagamento.");

            return;
        }

        if (!validarCampos()) {

            return;
        }

        FormaPagamento forma =
                new FormaPagamento();

        forma.setId(
                Integer.parseInt(
                        txtId.getText()));

        forma.setNome(
                txtNome.getText().trim());

        forma.setQtdeParcela(
                Integer.parseInt(
                        txtQtdeParcela.getText().trim()));

        forma.setPrazo(
                Integer.parseInt(
                        txtPrazo.getText().trim()));

        forma.setAvistaPrazo(
                cbAvistaPrazo.getSelectedIndex());

        if (controller.alterar(forma)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Forma de pagamento alterada com sucesso.");

            limparCampos();

            atualizarTabela();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao alterar forma de pagamento.");
        }
    }

    private void excluir() {

        if (txtId.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma forma de pagamento.");

            return;
        }

        int opcao =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente excluir esta forma de pagamento?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

        if (opcao != JOptionPane.YES_OPTION) {

            return;
        }

        if (controller.excluir(
                Integer.parseInt(txtId.getText()))) {

            JOptionPane.showMessageDialog(
                    this,
                    "Forma de pagamento excluída com sucesso.");

            limparCampos();

            atualizarTabela();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir forma de pagamento.\n"
                            + "Ela pode estar vinculada a uma venda, compra ou financeiro.");
        }
    }

    private void pesquisarPorNome() {

        String nomePesquisa =
                JOptionPane.showInputDialog(
                        this,
                        "Informe o nome da forma de pagamento:");

        if (nomePesquisa == null
                || nomePesquisa.trim().isEmpty()) {

            return;
        }

        modeloTabela.setRowCount(0);

        List<FormaPagamento> lista =
                controller.pesquisarTodos();

        if (lista == null) {

            return;
        }

        boolean encontrou =
                false;

        for (FormaPagamento forma : lista) {

            String nome =
                    forma.getNome();

            if (nome != null
                    && nome.toLowerCase()
                            .contains(
                                    nomePesquisa.trim()
                                            .toLowerCase())) {

                modeloTabela.addRow(new Object[]{

                        forma.getId(),

                        forma.getNome(),

                        forma.getQtdeParcela(),

                        forma.getPrazo(),

                        forma.getAvistaPrazo() == 0
                                ? "À Vista"
                                : "A Prazo"
                });

                encontrou =
                        true;
            }
        }

        if (!encontrou) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhuma forma de pagamento encontrada com esse nome.");

            atualizarTabela();
        }
    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        List<FormaPagamento> lista =
                controller.pesquisarTodos();

        if (lista == null) {

            return;
        }

        for (FormaPagamento forma : lista) {

            modeloTabela.addRow(new Object[]{

                    forma.getId(),

                    forma.getNome(),

                    forma.getQtdeParcela(),

                    forma.getPrazo(),

                    forma.getAvistaPrazo() == 0
                            ? "À Vista"
                            : "A Prazo"
            });
        }
    }

    private void preencherCampos(
            FormaPagamento forma) {

        txtId.setText(
                String.valueOf(
                        forma.getId()));

        txtNome.setText(
                forma.getNome());

        txtQtdeParcela.setText(
                String.valueOf(
                        forma.getQtdeParcela()));

        txtPrazo.setText(
                String.valueOf(
                        forma.getPrazo()));

        cbAvistaPrazo.setSelectedIndex(
                forma.getAvistaPrazo());
    }

    private boolean validarCampos() {

        if (txtNome.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome da forma de pagamento.");

            return false;
        }

        try {

            int parcelas =
                    Integer.parseInt(
                            txtQtdeParcela.getText().trim());

            int prazo =
                    Integer.parseInt(
                            txtPrazo.getText().trim());

            if (parcelas <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "A quantidade de parcelas deve ser maior que zero.");

                return false;
            }

            if (prazo < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "O prazo não pode ser negativo.");

                return false;
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Parcelas e prazo devem ser números inteiros.");

            return false;
        }

        return true;
    }

    private void limparCampos() {

        txtId.setText("");

        txtNome.setText("");

        txtQtdeParcela.setText("1");

        txtPrazo.setText("30");

        cbAvistaPrazo.setSelectedIndex(0);

        tabela.clearSelection();

        atualizarTabela();

        txtNome.requestFocus();
    }
}