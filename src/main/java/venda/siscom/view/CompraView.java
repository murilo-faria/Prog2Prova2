package venda.siscom.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
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

import venda.siscom.controller.CompraController;
import venda.siscom.controller.FormaPagamentoController;
import venda.siscom.controller.FornecedorController;
import venda.siscom.controller.FornecedorProdutoController;
import venda.siscom.controller.ProdutoController;
import venda.siscom.controller.TipoContaController;
import venda.siscom.model.Compra;
import venda.siscom.model.CompraProduto;
import venda.siscom.model.FormaPagamento;
import venda.siscom.model.Fornecedor;
import venda.siscom.model.FornecedorProduto;
import venda.siscom.model.Produto;
import venda.siscom.model.TipoConta;
import venda.siscom.util.FormatadorMoeda;


public class CompraView extends JFrame {

    private static final Logger logger =
            LoggerFactory.getLogger(CompraView.class);

    private JTextField txtId;
    private JTextField txtData;
    private JTextField txtValorTotal;

    private JComboBox<Fornecedor> cbFornecedor;
    private JComboBox<Produto> cbProduto;
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private JComboBox<TipoConta> cbTipoConta;

    private JTextField txtQuantidade;
    private JTextField txtValorCusto;
    private JTextField txtParcelas;
    private JTextField txtPrazo;
    private JTextField txtDesconto;

    private JButton btnAdicionarProduto;
    private JButton btnRemoverProduto;
    private JButton btnFinalizarCompra;
    private JButton btnLimpar;

    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    private final CompraController compraController =
            new CompraController();

    private final FornecedorController fornecedorController =
            new FornecedorController();

    private final ProdutoController produtoController =
            new ProdutoController();

       private final FornecedorProdutoController fornecedorProdutoController =
        new FornecedorProdutoController();

    private final FormaPagamentoController formaPagamentoController =
            new FormaPagamentoController();

    private final TipoContaController tipoContaController =
            new TipoContaController();

    private List<ItemCarrinho> itensCompra =
            new ArrayList<>();

    public CompraView() {

        setTitle("Cadastro de Compras");

        setSize(1024, 728);

        setResizable(false);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        carregarFornecedores();

        carregarProdutosFornecedor();

        carregarFormasPagamento();

        carregarTiposConta();

        // Seleciona automaticamente "Compra"
        for (int i = 0; i < cbTipoConta.getItemCount(); i++) {

        TipoConta tipo = cbTipoConta.getItemAt(i);

        if (tipo.getDescricao().equalsIgnoreCase("Compra")) {

        cbTipoConta.setSelectedIndex(i);
        break;
                }
        }

        // Não permite alterar
        cbTipoConta.setEnabled(false);                

        configurarEventos();
    }

    private void inicializarComponentes() {

        JPanel painelCampos =
                new JPanel(new GridLayout(10,2,5,5));

        painelCampos.add(new JLabel("Código"));
        txtId = new JTextField();
        txtId.setEditable(false);
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Fornecedor"));
        cbFornecedor = new JComboBox<>();
        painelCampos.add(cbFornecedor);

        painelCampos.add(new JLabel("Data"));
        txtData = new JTextField(LocalDate.now().toString());
        txtData.setEditable(false);
        painelCampos.add(txtData);

        painelCampos.add(new JLabel("Produto"));
        cbProduto = new JComboBox<>();
        painelCampos.add(cbProduto);

        painelCampos.add(new JLabel("Quantidade"));
        txtQuantidade = new JTextField("1");
        painelCampos.add(txtQuantidade);

        painelCampos.add(new JLabel("Valor Custo"));
        txtValorCusto = new JTextField();
        painelCampos.add(txtValorCusto);

        painelCampos.add(new JLabel("Forma Pagamento"));
        cbFormaPagamento = new JComboBox<>();
        painelCampos.add(cbFormaPagamento);

        painelCampos.add(new JLabel("Tipo Conta"));
        cbTipoConta = new JComboBox<>();
        painelCampos.add(cbTipoConta);

        painelCampos.add(new JLabel("Parcelas / Prazo / Desconto"));
        JPanel painelPagamento = new JPanel(new GridLayout(1,3));

        txtParcelas = new JTextField("1");
        txtPrazo = new JTextField("30");
        txtDesconto = new JTextField("0");

        painelPagamento.add(txtParcelas);
        painelPagamento.add(txtPrazo);
        painelPagamento.add(txtDesconto);

        painelCampos.add(painelPagamento);

        painelCampos.add(new JLabel("Valor Total"));
        txtValorTotal = new JTextField(FormatadorMoeda.formatar(0.0));
        txtValorTotal.setEditable(false);
        painelCampos.add(txtValorTotal);

        add(painelCampos, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(
                new Object[]{
                        "Produto",
                        "Quantidade",
                        "Valor",
                        "Total"
                },0);

        tabelaProdutos = new JTable(modeloTabela);

        add(new JScrollPane(tabelaProdutos),
                BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        btnAdicionarProduto =
                new JButton("Adicionar");

        btnRemoverProduto =
                new JButton("Remover");

        btnFinalizarCompra =
                new JButton("Finalizar Compra");

        btnLimpar =
                new JButton("Limpar");

        painelBotoes.add(btnAdicionarProduto);
        painelBotoes.add(btnRemoverProduto);
        painelBotoes.add(btnFinalizarCompra);
        painelBotoes.add(btnLimpar);

        add(painelBotoes,
                BorderLayout.SOUTH);
    }

        private void carregarFornecedores() {

        cbFornecedor.removeAllItems();

        List<Fornecedor> fornecedores =
                fornecedorController.pesquisarTodos();

        if (fornecedores != null) {

            for (Fornecedor fornecedor : fornecedores) {
                cbFornecedor.addItem(fornecedor);
            }
        }
    }

    private void carregarProdutos() {

        cbProduto.removeAllItems();

        List<Produto> produtos =
                produtoController.pesquisarTodos();

        if (produtos != null) {

            for (Produto produto : produtos) {
                cbProduto.addItem(produto);
            }
        }
    }

    private void carregarProdutosFornecedor() {

    cbProduto.removeAllItems();

    Fornecedor fornecedor =
            (Fornecedor) cbFornecedor.getSelectedItem();

    if (fornecedor == null) {
        return;
        }

    List<FornecedorProduto> lista =
            fornecedorProdutoController
                    .pesquisarPorFornecedor(fornecedor);

    if (lista == null) {
        return;
         }

    for (FornecedorProduto fp : lista) {

        cbProduto.addItem(fp.getProduto());

         }

}

    private void carregarFormasPagamento() {

        cbFormaPagamento.removeAllItems();

        List<FormaPagamento> formas =
                formaPagamentoController.pesquisarTodos();

        if (formas != null) {

            for (FormaPagamento forma : formas) {
                cbFormaPagamento.addItem(forma);
            }
        }

        if (cbFormaPagamento.getItemCount() > 0) {

            FormaPagamento forma =
                    (FormaPagamento) cbFormaPagamento.getSelectedItem();

            if (forma != null) {

                txtParcelas.setText(
                        String.valueOf(forma.getQtdeParcela()));

                txtPrazo.setText(
                        String.valueOf(forma.getPrazo()));
            }
        }
    }

    private void carregarTiposConta() {

        cbTipoConta.removeAllItems();

        List<TipoConta> tipos =
                tipoContaController.pesquisarTodos();

        if (tipos != null) {

            for (TipoConta tipo : tipos) {
                cbTipoConta.addItem(tipo);
            }
        }
    }

    private void configurarEventos() {

        cbFornecedor.addActionListener(e -> {

        carregarProdutosFornecedor();

        Produto produto =
            (Produto) cbProduto.getSelectedItem();

        if (produto != null) {

        txtValorCusto.setText(
                FormatadorMoeda.formatar(produto.getPreco()));
                }

        });

        cbProduto.addActionListener(e -> {

            Produto produto =
                    (Produto) cbProduto.getSelectedItem();

            if (produto != null) {

                txtValorCusto.setText(
                        FormatadorMoeda.formatar(produto.getPreco()));
            }
        });

        cbFormaPagamento.addActionListener(e -> {

            FormaPagamento forma =
                    (FormaPagamento) cbFormaPagamento.getSelectedItem();

            if (forma != null) {

                txtParcelas.setText(
                        String.valueOf(forma.getQtdeParcela()));

                txtPrazo.setText(
                        String.valueOf(forma.getPrazo()));
            }
        });

        btnAdicionarProduto.addActionListener(
                e -> adicionarProduto());

        btnRemoverProduto.addActionListener(
                e -> removerProduto());

        btnFinalizarCompra.addActionListener(
                e -> finalizarCompra());

        btnLimpar.addActionListener(
                e -> limparCampos());
    }

        private void adicionarProduto() {

        try {

            Produto produto =
                    (Produto) cbProduto.getSelectedItem();

            if (produto == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um produto.");

                return;
            }

            int quantidade =
                    Integer.parseInt(txtQuantidade.getText());

            double valorCusto =
                    FormatadorMoeda.converter(txtValorCusto.getText());

            double total =
                    quantidade * valorCusto;

            modeloTabela.addRow(new Object[]{

                    produto.getNome(),

                    quantidade,

                    FormatadorMoeda.formatar(valorCusto),

                    FormatadorMoeda.formatar(total)

            });

            itensCompra.add(

                    new ItemCarrinho(

                            produto,

                            quantidade,

                            valorCusto

                    )

            );

            calcularTotal();

        } catch (Exception e) {

            logger.warn("Valores invalidos ao adicionar produto na compra.", e);

            JOptionPane.showMessageDialog(
                    this,
                    "Valores inválidos.");
        }
    }

    private void removerProduto() {

        int linha =
                tabelaProdutos.getSelectedRow();

        if (linha < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um item.");

            return;
        }

        modeloTabela.removeRow(linha);

        itensCompra.remove(linha);

        calcularTotal();
    }

    private void calcularTotal() {

        double total = 0;

        for (ItemCarrinho item : itensCompra) {

            total +=
                    item.getQuantidade()
                    * item.getValorCusto();
        }

        txtValorTotal.setText(
                FormatadorMoeda.formatar(total));
    }

    private void limparCampos() {

        itensCompra.clear();

        modeloTabela.setRowCount(0);

        txtQuantidade.setText("1");

        txtValorCusto.setText("");

        txtDesconto.setText("0");

        txtValorTotal.setText(FormatadorMoeda.formatar(0.0));
    }

        private void finalizarCompra() {

        if (itensCompra.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Adicione pelo menos um produto.");

            return;
        }

        Fornecedor fornecedor =
                (Fornecedor) cbFornecedor.getSelectedItem();

        FormaPagamento forma =
                (FormaPagamento) cbFormaPagamento.getSelectedItem();

        TipoConta tipo =
                (TipoConta) cbTipoConta.getSelectedItem();

        if (fornecedor == null || forma == null || tipo == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha todos os dados da compra.");

            return;
        }

        Compra compra = new Compra();

        compra.setFornecedor(fornecedor);
        compra.setDataCompra(LocalDate.now());

        List<CompraProduto> itens = new ArrayList<>();

        for (ItemCarrinho item : itensCompra) {

            CompraProduto cp = new CompraProduto();

            cp.setProduto(item.getProduto());
            cp.setQuantidade(item.getQuantidade());
            cp.setValorUnitario(item.getValorCusto());

            itens.add(cp);
        }

        boolean sucesso = compraController.finalizarCompra(

                compra,

                itens,

                forma,

                tipo,

                LocalDate.now(),

                Integer.parseInt(txtParcelas.getText()),

                Integer.parseInt(txtPrazo.getText()),

                FormatadorMoeda.converter(txtDesconto.getText())

        );

        if (sucesso) {

            JOptionPane.showMessageDialog(
                    this,
                    "Compra realizada com sucesso!");

            limparCampos();

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao realizar a compra.");
        }
    }

    private static class ItemCarrinho {

        private final Produto produto;
        private final int quantidade;
        private final double valorCusto;

        public ItemCarrinho(
                Produto produto,
                int quantidade,
                double valorCusto) {

            this.produto = produto;
            this.quantidade = quantidade;
            this.valorCusto = valorCusto;
        }

        public Produto getProduto() {
            return produto;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public double getValorCusto() {
            return valorCusto;
        }
    }

}
