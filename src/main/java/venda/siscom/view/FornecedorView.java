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
import javax.swing.table.DefaultTableModel;

import venda.siscom.controller.FornecedorController;
import venda.siscom.model.Fornecedor;

public class FornecedorView extends JFrame {

    private JTextField txtId;
    private JTextField txtNomeFantasia;
    private JTextField txtRazaoSocial;
    private JTextField txtCnpj;

    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private JButton btnLimpar;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final FornecedorController fornecedorController =
            new FornecedorController();

    public FornecedorView() {

        setTitle("Cadastro de Fornecedores");

        setSize(900, 600);

        setResizable(false);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        configurarAcoes();

        atualizarTabela();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JPanel painelCampos =
                new JPanel(new GridLayout(4, 2, 5, 5));

        painelCampos.add(new JLabel("Código"));

        txtId = new JTextField();
        txtId.setEditable(false);
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Nome Fantasia"));

        txtNomeFantasia = new JTextField();
        painelCampos.add(txtNomeFantasia);

        painelCampos.add(new JLabel("Razão Social"));

        txtRazaoSocial = new JTextField();
        painelCampos.add(txtRazaoSocial);

        painelCampos.add(new JLabel("CNPJ"));

        txtCnpj = new JTextField();
        painelCampos.add(txtCnpj);

        JPanel painelBotoes =
                new JPanel(new FlowLayout());

        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");
        btnLimpar = new JButton("Limpar");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnPesquisar);
        painelBotoes.add(btnLimpar);

        modeloTabela =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Nome Fantasia",
                                "Razão Social",
                                "CNPJ"
                        }, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        tabela = new JTable(modeloTabela);

        JScrollPane scroll =
                new JScrollPane(tabela);

        add(painelCampos, BorderLayout.NORTH);

        add(scroll, BorderLayout.CENTER);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void configurarAcoes() {

        btnSalvar.addActionListener(e -> salvar());

        btnAlterar.addActionListener(e -> alterar());

        btnExcluir.addActionListener(e -> excluir());

        btnPesquisar.addActionListener(e -> pesquisar());

        btnLimpar.addActionListener(e -> limparCampos());

        tabela.getSelectionModel().addListSelectionListener(e -> {

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

            Fornecedor fornecedor =
                    fornecedorController.pesquisar(id);

            if (fornecedor != null) {

                preencherCampos(fornecedor);
            }
        });
    }

    private void salvar() {

        if (txtNomeFantasia.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome fantasia do fornecedor.");

            return;
        }

        try {

            Fornecedor fornecedor =
                    new Fornecedor();

            fornecedor.setNomeFantasia(
                    txtNomeFantasia.getText().trim());

            fornecedor.setRazaoSocial(
                    txtRazaoSocial.getText().trim());

            fornecedor.setCnpj(
                    txtCnpj.getText().trim());

            if (fornecedorController.salvar(fornecedor)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Fornecedor salvo com sucesso.");

                atualizarTabela();

                limparCampos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao salvar fornecedor.");
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao salvar fornecedor.");
        }
    }

    private void alterar() {

        if (txtId.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um fornecedor.");

            return;
        }

        if (txtNomeFantasia.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome fantasia do fornecedor.");

            return;
        }

        Fornecedor fornecedor =
                new Fornecedor();

        fornecedor.setId(
                Integer.parseInt(txtId.getText()));

        fornecedor.setNomeFantasia(
                txtNomeFantasia.getText().trim());

        fornecedor.setRazaoSocial(
                txtRazaoSocial.getText().trim());

        fornecedor.setCnpj(
                txtCnpj.getText().trim());

        if (fornecedorController.alterar(fornecedor)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fornecedor alterado com sucesso.");

            atualizarTabela();

            limparCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao alterar fornecedor.");
        }
    }

    private void excluir() {

        if (txtId.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um fornecedor.");

            return;
        }

        int opcao =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente excluir este fornecedor?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

        if (opcao != JOptionPane.YES_OPTION) {

            return;
        }

        if (fornecedorController.excluir(
                Integer.parseInt(txtId.getText()))) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fornecedor excluído com sucesso.");

            atualizarTabela();

            limparCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir fornecedor.");
        }
    }

    private void pesquisar() {

        String nomePesquisa =
                JOptionPane.showInputDialog(
                        this,
                        "Informe o nome fantasia do fornecedor:");

        if (nomePesquisa == null || nomePesquisa.trim().isEmpty()) {

            return;
        }

        modeloTabela.setRowCount(0);

        List<Fornecedor> lista =
                fornecedorController.pesquisarTodos();

        if (lista == null) {

            return;
        }

        boolean encontrou =
                false;

        for (Fornecedor fornecedor : lista) {

            String nomeFornecedor =
                    fornecedor.getNomeFantasia();

            if (nomeFornecedor != null &&
                    nomeFornecedor.toLowerCase()
                            .contains(nomePesquisa.trim().toLowerCase())) {

                modeloTabela.addRow(new Object[]{

                        fornecedor.getId(),

                        fornecedor.getNomeFantasia(),

                        fornecedor.getRazaoSocial(),

                        fornecedor.getCnpj()
                });

                encontrou = true;
            }
        }

        if (!encontrou) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhum fornecedor encontrado com esse nome.");

            atualizarTabela();
        }
    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        List<Fornecedor> lista =
                fornecedorController.pesquisarTodos();

        if (lista == null) {
            return;
        }

        for (Fornecedor fornecedor : lista) {

            modeloTabela.addRow(new Object[]{

                    fornecedor.getId(),

                    fornecedor.getNomeFantasia(),

                    fornecedor.getRazaoSocial(),

                    fornecedor.getCnpj()
            });
        }
    }

    private void preencherCampos(Fornecedor fornecedor) {

        txtId.setText(
                String.valueOf(fornecedor.getId()));

        txtNomeFantasia.setText(
                fornecedor.getNomeFantasia());

        txtRazaoSocial.setText(
                fornecedor.getRazaoSocial());

        txtCnpj.setText(
                fornecedor.getCnpj());
    }

    private void limparCampos() {

        txtId.setText("");

        txtNomeFantasia.setText("");

        txtRazaoSocial.setText("");

        txtCnpj.setText("");

        tabela.clearSelection();

        atualizarTabela();

        txtNomeFantasia.requestFocus();
    }
}