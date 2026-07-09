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
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import venda.siscom.controller.FornecedorController;
import venda.siscom.controller.FornecedorProdutoController;
import venda.siscom.controller.ProdutoController;
import venda.siscom.model.Fornecedor;
import venda.siscom.model.FornecedorProduto;
import venda.siscom.model.Produto;

public class FornecedorProdutoView extends JFrame {

    private JTextField txtId;

    private JComboBox<Fornecedor> cbFornecedor;

    private JComboBox<Produto> cbProduto;

    private JTable tabela;

    private DefaultTableModel modeloTabela;

    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private JButton btnLimpar;

    private final FornecedorProdutoController controller =
            new FornecedorProdutoController();

    private final FornecedorController fornecedorController =
            new FornecedorController();

    private final ProdutoController produtoController =
            new ProdutoController();

    public FornecedorProdutoView() {

        setTitle("Fornecedor x Produto");

        setSize(900,600);

        setResizable(false);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        carregarFornecedores();

        carregarProdutos();

        atualizarTabela();

        configurarEventos();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JPanel painelCampos =
                new JPanel(new GridLayout(3,2,5,5));

        painelCampos.add(new JLabel("Código"));

        txtId = new JTextField();

        txtId.setEditable(false);

        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Fornecedor"));

        cbFornecedor = new JComboBox<>();

        painelCampos.add(cbFornecedor);

        painelCampos.add(new JLabel("Produto"));

        cbProduto = new JComboBox<>();

        painelCampos.add(cbProduto);

        add(painelCampos, BorderLayout.NORTH);

        modeloTabela =
                new DefaultTableModel(

                        new Object[]{
                                "ID",
                                "Fornecedor",
                                "Produto"
                        },0){

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column){

                        return false;
                    }

                };

        tabela = new JTable(modeloTabela);

        tabela.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabela),
                BorderLayout.CENTER);

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

        add(painelBotoes,
                BorderLayout.SOUTH);
    }

        private void carregarFornecedores() {

        cbFornecedor.removeAllItems();

        List<Fornecedor> lista =
                fornecedorController.pesquisarTodos();

        if (lista != null) {

            for (Fornecedor fornecedor : lista) {

                cbFornecedor.addItem(fornecedor);
            }
        }
    }

    private void carregarProdutos() {

        cbProduto.removeAllItems();

        List<Produto> lista =
                produtoController.pesquisarTodos();

        if (lista != null) {

            for (Produto produto : lista) {

                cbProduto.addItem(produto);
            }
        }
    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        List<FornecedorProduto> lista =
                controller.pesquisarTodos();

        if (lista == null) {
            return;
        }

        for (FornecedorProduto fp : lista) {

            modeloTabela.addRow(new Object[]{

                    fp.getId(),

                    fp.getFornecedor(),

                    fp.getProduto()

            });

        }
    }

    private void configurarEventos() {

        btnSalvar.addActionListener(e -> salvar());

        btnAlterar.addActionListener(e -> alterar());

        btnExcluir.addActionListener(e -> excluir());

        btnPesquisar.addActionListener(e -> pesquisar());

        btnLimpar.addActionListener(e -> limparCampos());

        tabela.getSelectionModel()
                .addListSelectionListener(e -> {

            int linha = tabela.getSelectedRow();

            if (linha < 0) {
                return;
            }

            txtId.setText(
                    modeloTabela.getValueAt(linha, 0).toString());

            pesquisar();
        });

    }

    private void salvar() {

        Fornecedor fornecedor =
                (Fornecedor) cbFornecedor.getSelectedItem();

        Produto produto =
                (Produto) cbProduto.getSelectedItem();

        if (fornecedor == null || produto == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um fornecedor e um produto.");

            return;
        }

        FornecedorProduto fp =
                new FornecedorProduto();

        fp.setFornecedor(fornecedor);

        fp.setProduto(produto);

        if (controller.salvar(fp)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registro salvo.");

            atualizarTabela();

            limparCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao salvar.");
        }
    }

    private void alterar() {

        if (txtId.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pesquise um registro.");

            return;
        }

        FornecedorProduto fp =
                controller.pesquisar(
                        Integer.parseInt(txtId.getText()));

        if (fp == null) {
            return;
        }

        fp.setFornecedor(
                (Fornecedor) cbFornecedor.getSelectedItem());

        fp.setProduto(
                (Produto) cbProduto.getSelectedItem());

        controller.alterar(fp);

        atualizarTabela();

        limparCampos();

        JOptionPane.showMessageDialog(
                this,
                "Registro alterado.");
    }

    private void excluir() {

        if (txtId.getText().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pesquise um registro.");

            return;
        }

        controller.excluir(
                Integer.parseInt(txtId.getText()));

        atualizarTabela();

        limparCampos();

        JOptionPane.showMessageDialog(
                this,
                "Registro excluído.");
    }

    private void pesquisar() {

        if (txtId.getText().isBlank()) {
            return;
        }

        FornecedorProduto fp =
                controller.pesquisar(
                        Integer.parseInt(txtId.getText()));

        if (fp == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registro não encontrado.");

            return;
        }

        cbFornecedor.setSelectedItem(fp.getFornecedor());

        cbProduto.setSelectedItem(fp.getProduto());

    }

    private void limparCampos() {

        txtId.setText("");

        if (cbFornecedor.getItemCount() > 0) {
            cbFornecedor.setSelectedIndex(0);
        }

        if (cbProduto.getItemCount() > 0) {
            cbProduto.setSelectedIndex(0);
        }

        tabela.clearSelection();
    }

}