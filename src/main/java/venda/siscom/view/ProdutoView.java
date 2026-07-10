package venda.siscom.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.controller.CategoriaController;
import venda.siscom.controller.ProdutoController;
import venda.siscom.model.Categoria;
import venda.siscom.model.Produto;
import venda.siscom.util.FormatadorMoeda;

public class ProdutoView extends JFrame {

    private static final Logger logger =
            LoggerFactory.getLogger(ProdutoView.class);

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtPrecoVenda;
    private JTextField txtEstoque;

    private JComboBox<Categoria> cbCategoria;

    private JTextField txtPrecoUltimaCompra;
    private JTextField txtPrecoUltimaVenda;
    private JTextField txtPrecoMedio;

    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private JButton btnLimpar;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final ProdutoController produtoController =
            new ProdutoController();

    private final CategoriaController categoriaController =
            new CategoriaController();

    public ProdutoView() {

        setTitle("Cadastro de Produtos");

        setSize(950, 680);

        setResizable(false);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        configurarAcoes();

        atualizarTabela();

    }

    private void inicializarComponentes() {

        JPanel painelCampos =
                new JPanel(new GridLayout(8, 2, 5, 5));

        painelCampos.add(new JLabel("Código"));

        txtId = new JTextField();
        txtId.setEditable(false);
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Nome do Produto"));

        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Categoria"));

        cbCategoria = new JComboBox<>();

        cbCategoria.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus);

                if (value instanceof Categoria) {

                    Categoria categoria = (Categoria) value;

                    setText(categoria.getNome());

                }

                return this;
            }
        });

        carregarCategorias();

        painelCampos.add(cbCategoria);

        painelCampos.add(new JLabel("Preço de Venda"));

        txtPrecoVenda = new JTextField();
        painelCampos.add(txtPrecoVenda);

        painelCampos.add(new JLabel("Quantidade em Estoque"));

        txtEstoque = new JTextField("0");
        txtEstoque.setEditable(false);
        painelCampos.add(txtEstoque);

        painelCampos.add(new JLabel("Última Compra"));

        txtPrecoUltimaCompra = new JTextField(FormatadorMoeda.formatar(0.0));
        txtPrecoUltimaCompra.setEditable(false);
        painelCampos.add(txtPrecoUltimaCompra);

        painelCampos.add(new JLabel("Última Venda"));

        txtPrecoUltimaVenda = new JTextField(FormatadorMoeda.formatar(0.0));
        txtPrecoUltimaVenda.setEditable(false);
        painelCampos.add(txtPrecoUltimaVenda);

        painelCampos.add(new JLabel("Preço Médio"));

        txtPrecoMedio = new JTextField(FormatadorMoeda.formatar(0.0));
        txtPrecoMedio.setEditable(false);
        painelCampos.add(txtPrecoMedio);

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
                                "Nome",
                                "Preço",
                                "Estoque",
                                "Categoria"
                        }, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;

                    }

                };

        tabela = new JTable(modeloTabela);

        tabela.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int linha = tabela.getSelectedRow();

                        if (linha >= 0) {

                            Integer id =
                                    (Integer) tabela.getValueAt(linha, 0);

                            Produto p =
                                    produtoController.pesquisar(id);

                            if (p != null) {

                                preencherCampos(p);

                            }

                        }

                    }

                });

        add(painelCampos, BorderLayout.NORTH);

        add(new JScrollPane(tabela),
                BorderLayout.CENTER);

        add(painelBotoes,
                BorderLayout.SOUTH);

    }

    private void configurarAcoes() {

        btnSalvar.addActionListener(e -> {

            try {

                Produto produto = new Produto();

                produto.setNome(txtNome.getText());

                produto.setPreco(
                        FormatadorMoeda.converter(
                                txtPrecoVenda.getText()));

                produto.setQtdEstoque(0);

                produto.setPrecoMedio(0.0);

                produto.setValorUltimaCompra(0.0);

                produto.setValorUltimaVenda(0.0);

                Categoria categoria =
                        (Categoria) cbCategoria.getSelectedItem();


                if (txtNome.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Informe o nome do produto.");

                    return;

                }

                if (txtPrecoVenda.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Informe o preço de venda.");

                    return;

                }

                if (categoria == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Selecione uma categoria.");

                    return;

                }


                produto.setCategoria(categoria);

                if (produtoController.salvar(produto)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Produto salvo com sucesso!");

                    limparCampos();

                    atualizarTabela();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Erro ao salvar produto.");

                }

            } catch (Exception ex) {

                logger.warn("Dados invalidos ao salvar produto.", ex);

                JOptionPane.showMessageDialog(
                        this,
                        "Dados inválidos.");

            }

        });

        btnAlterar.addActionListener(e -> {

            if (txtId.getText().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um produto.");

                return;

            }

            try {

                Produto produto =
                        produtoController.pesquisar(
                                Integer.parseInt(txtId.getText()));

                if (produto != null) {

                    produto.setNome(txtNome.getText());

                    produto.setPreco(
                            FormatadorMoeda.converter(
                                    txtPrecoVenda.getText()));

                    Categoria categoria =
                            (Categoria) cbCategoria.getSelectedItem();

                    if (categoria == null) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Selecione uma categoria.");

                        return;

                    }

                    produto.setCategoria(categoria);

                    if (produtoController.alterar(produto)) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Produto alterado com sucesso!");

                        limparCampos();

                        atualizarTabela();

                    }

                }

            } catch (Exception ex) {

                logger.warn("Erro ao alterar produto.", ex);

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao alterar produto.");

            }

        });

        btnExcluir.addActionListener(e -> {

            if (txtId.getText().isEmpty()) {

                return;

            }

            int op =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Deseja excluir o produto?");

            if (op == JOptionPane.YES_OPTION) {

                if (produtoController.excluir(
                        Integer.parseInt(txtId.getText()))) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Produto excluído.");

                    limparCampos();

                    atualizarTabela();

                }

            }

        });

        btnPesquisar.addActionListener(e -> {

            String codigo =
                    JOptionPane.showInputDialog(
                            "Informe o código:");

            if (codigo == null) {

                return;

            }

            Produto produto =
                    produtoController.pesquisar(
                            Integer.parseInt(codigo));

            if (produto != null) {

                preencherCampos(produto);

            }

        });

        btnLimpar.addActionListener(
                e -> limparCampos());

    }

    private void carregarCategorias() {

        cbCategoria.removeAllItems();

        List<Categoria> categorias =
                categoriaController.pesquisarTodos();

        if (categorias == null) {

            return;

        }

        for (Categoria categoria : categorias) {

            cbCategoria.addItem(categoria);

        }

        cbCategoria.setSelectedIndex(-1);

    }

    private void preencherCampos(Produto produto) {

        txtId.setText(
                String.valueOf(produto.getId()));

        txtNome.setText(
                produto.getNome());

        txtPrecoVenda.setText(
                FormatadorMoeda.formatar(produto.getPreco()));

        txtEstoque.setText(
                String.valueOf(produto.getQtdEstoque()));

        txtPrecoMedio.setText(
                FormatadorMoeda.formatar(produto.getPrecoMedio()));

        txtPrecoUltimaCompra.setText(
                FormatadorMoeda.formatar(
                        produto.getValorUltimaCompra()));

        txtPrecoUltimaVenda.setText(
                FormatadorMoeda.formatar(
                        produto.getValorUltimaVenda()));

        selecionarCategoria(produto.getCategoria());

    }

    private void selecionarCategoria(Categoria categoriaProduto) {

        if (categoriaProduto == null) {

            cbCategoria.setSelectedIndex(-1);

            return;

        }

        for (int i = 0; i < cbCategoria.getItemCount(); i++) {

            Categoria categoriaCombo =
                    cbCategoria.getItemAt(i);

            if (categoriaCombo.getId().equals(categoriaProduto.getId())) {

                cbCategoria.setSelectedIndex(i);

                return;

            }

        }

        cbCategoria.setSelectedIndex(-1);

    }

    private void limparCampos() {

        txtId.setText("");

        txtNome.setText("");

        cbCategoria.setSelectedIndex(-1);

        txtPrecoVenda.setText("");

        txtEstoque.setText("0");

        txtPrecoMedio.setText(FormatadorMoeda.formatar(0.0));

        txtPrecoUltimaCompra.setText(FormatadorMoeda.formatar(0.0));

        txtPrecoUltimaVenda.setText(FormatadorMoeda.formatar(0.0));

        tabela.clearSelection();

    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        List<Produto> lista =
                produtoController.pesquisarTodos();

        if (lista == null) {

            return;

        }

        for (Produto produto : lista) {

            modeloTabela.addRow(new Object[]{

                    produto.getId(),

                    produto.getNome(),

                    FormatadorMoeda.formatar(produto.getPreco()),

                    produto.getQtdEstoque(),

                    produto.getCategoria() == null
                            ? ""
                            : produto.getCategoria().getNome()

            });

        }

    }

}