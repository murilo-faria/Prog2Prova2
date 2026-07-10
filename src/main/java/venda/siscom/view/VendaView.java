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

import venda.siscom.controller.ClienteController;
import venda.siscom.controller.ProdutoController;
import venda.siscom.controller.TipoContaController;
import venda.siscom.controller.VendaController;
import venda.siscom.model.Cliente;
import venda.siscom.model.Produto;
import venda.siscom.model.TipoConta;
import venda.siscom.model.Venda;
import venda.siscom.model.VendaProduto;
import venda.siscom.util.FormatadorMoeda;

public class VendaView extends JFrame {

    private static final Logger logger =
            LoggerFactory.getLogger(VendaView.class);

    private JTextField txtId;
    private JTextField txtData;
    private JTextField txtValorTotal;

    private JComboBox<Cliente> cbCliente;
    private JComboBox<Produto> cbProduto;
    private JComboBox<TipoConta> cbTipoConta;

    private JTextField txtQuantidade;
    private JTextField txtValorUnitario;

    private JButton btnAdicionarProduto;
    private JButton btnRemoverProduto;

    private JButton btnFinalizarVenda;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private JButton btnLimpar;

    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    private final ClienteController clienteController =
            new ClienteController();

    private final ProdutoController produtoController =
            new ProdutoController();

    private final TipoContaController tipoContaController =
            new TipoContaController();

    private final VendaController vendaController =
            new VendaController();

    private Venda vendaAtual = new Venda();

    private final List<VendaProduto> itensVenda =
            new ArrayList<>();

    public VendaView() {

        setTitle("Cadastro de Vendas");

        setSize(1024, 728);

        setResizable(false);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        carregarClientes();

        carregarProdutos();

        carregarTiposConta();

        selecionarTipoContaVenda();

        cbTipoConta.setEnabled(false);

    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JPanel painelCampos =
                new JPanel(new GridLayout(8, 2, 5, 5));

        painelCampos.add(new JLabel("Código"));

        txtId = new JTextField();
        txtId.setEditable(false);
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Cliente"));

        cbCliente = new JComboBox<>();
        painelCampos.add(cbCliente);

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

        painelCampos.add(new JLabel("Valor Unitário"));

        txtValorUnitario = new JTextField();
        painelCampos.add(txtValorUnitario);

        painelCampos.add(new JLabel("Tipo Conta"));

        cbTipoConta = new JComboBox<>();
        painelCampos.add(cbTipoConta);

        painelCampos.add(new JLabel("Valor Total"));

        txtValorTotal = new JTextField(FormatadorMoeda.formatar(0.0));
        txtValorTotal.setEditable(false);
        painelCampos.add(txtValorTotal);

        add(painelCampos, BorderLayout.NORTH);

        JPanel painelItens =
                new JPanel(new FlowLayout());

        btnAdicionarProduto =
                new JButton("Adicionar Produto");

        btnRemoverProduto =
                new JButton("Remover Produto");

        painelItens.add(btnAdicionarProduto);
        painelItens.add(btnRemoverProduto);

        add(painelItens, BorderLayout.WEST);

        modeloTabela =
                new DefaultTableModel(
                        new Object[]{
                                "Produto",
                                "Quantidade",
                                "Valor Unitário",
                                "Total"
                        }, 0);

        tabelaProdutos =
                new JTable(modeloTabela);

        add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        JPanel painelBotoes =
                new JPanel(new FlowLayout());

        btnFinalizarVenda =
                new JButton("Finalizar Venda");

        btnAlterar =
                new JButton("Alterar");

        btnExcluir =
                new JButton("Excluir");

        btnPesquisar =
                new JButton("Pesquisar");

        btnLimpar =
                new JButton("Limpar");

        painelBotoes.add(btnFinalizarVenda);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnPesquisar);
        painelBotoes.add(btnLimpar);

        add(painelBotoes, BorderLayout.SOUTH);

        cbProduto.addActionListener(e -> {

            Produto produto =
                    (Produto) cbProduto.getSelectedItem();

            if (produto != null) {

                txtValorUnitario.setText(
                        FormatadorMoeda.formatar(produto.getPreco()));
            }
        });

        btnAdicionarProduto.addActionListener(
                e -> adicionarProduto());

        btnRemoverProduto.addActionListener(
                e -> removerProduto());

        btnFinalizarVenda.addActionListener(
                e -> finalizarVenda());

        btnLimpar.addActionListener(
                e -> limparCampos());
    }

    private void carregarClientes() {

        cbCliente.removeAllItems();

        List<Cliente> clientes =
                clienteController.pesquisarTodos();

        if (clientes != null) {

            for (Cliente cliente : clientes) {

                cbCliente.addItem(cliente);
            }
        }

        cbCliente.setSelectedIndex(-1);
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

        if (cbProduto.getItemCount() > 0) {

            cbProduto.setSelectedIndex(0);
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

    private void selecionarTipoContaVenda() {

        for (int i = 0; i < cbTipoConta.getItemCount(); i++) {

            TipoConta tipo =
                    cbTipoConta.getItemAt(i);

            if (tipo != null
                    && tipo.getDescricao() != null
                    && tipo.getDescricao().equalsIgnoreCase("Venda")) {

                cbTipoConta.setSelectedIndex(i);

                break;
            }
        }
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

            if (quantidade <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantidade inválida.");

                return;
            }

            if (!produtoController.verificarEstoque(
                    produto,
                    quantidade)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Estoque insuficiente.");

                return;
            }

            double valorUnitario =
                    FormatadorMoeda.converter(
                            txtValorUnitario.getText());

            VendaProduto item =
                    new VendaProduto();

            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setValorUnitario(valorUnitario);

            itensVenda.add(item);

            modeloTabela.addRow(new Object[]{

                    produto.getNome(),

                    quantidade,

                    FormatadorMoeda.formatar(valorUnitario),

                    FormatadorMoeda.formatar(
                            quantidade * valorUnitario)
            });

            calcularTotal();

        } catch (Exception ex) {

            logger.warn(
                    "Dados invalidos ao adicionar produto na venda.",
                    ex);

            JOptionPane.showMessageDialog(
                    this,
                    "Dados inválidos.");
        }
    }

    private void removerProduto() {

        int linha =
                tabelaProdutos.getSelectedRow();

        if (linha < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um produto para remover.");

            return;
        }

        itensVenda.remove(linha);

        modeloTabela.removeRow(linha);

        calcularTotal();
    }

    private void calcularTotal() {

        double total = 0;

        for (VendaProduto item : itensVenda) {

            total += item.getQuantidade()
                    * item.getValorUnitario();
        }

        txtValorTotal.setText(
                FormatadorMoeda.formatar(total));
    }

    private void limparCampos() {

        vendaAtual =
                new Venda();

        itensVenda.clear();

        modeloTabela.setRowCount(0);

        txtId.setText("");

        txtData.setText(LocalDate.now().toString());

        txtQuantidade.setText("1");

        txtValorUnitario.setText("");

        txtValorTotal.setText(
                FormatadorMoeda.formatar(0.0));

        cbCliente.setSelectedIndex(-1);

        if (cbProduto.getItemCount() > 0) {

            cbProduto.setSelectedIndex(0);
        }

        selecionarTipoContaVenda();

        cbTipoConta.setEnabled(false);
    }

    private void finalizarVenda() {

        if (itensVenda.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Adicione pelo menos um produto.");

            return;
        }

        Cliente cliente =
                (Cliente) cbCliente.getSelectedItem();

        if (cliente == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um cliente.");

            return;
        }

        if (!vendaController.verificarLimiteVendasCliente(cliente)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Limite de vendas do cliente atingido no mês.");

            return;
        }

        vendaAtual =
                new Venda();

        vendaAtual.setCliente(cliente);

        vendaAtual.setDataVenda(LocalDate.now());

        vendaAtual.setValorTotal(
                FormatadorMoeda.converter(
                        txtValorTotal.getText()));

        PagamentoVendaView pagamento =
                new PagamentoVendaView(
                        this,
                        vendaAtual,
                        itensVenda);

        pagamento.setVisible(true);

        setVisible(false);
    }

    public JTextField getTxtId() {
        return txtId;
    }

    public JTextField getTxtData() {
        return txtData;
    }

    public JTextField getTxtValorTotal() {
        return txtValorTotal;
    }

    public JTable getTabelaProdutos() {
        return tabelaProdutos;
    }

    public List<VendaProduto> getItensVenda() {
        return itensVenda;
    }

    public Venda getVendaAtual() {
        return vendaAtual;
    }

    public TipoConta getTipoContaSelecionado() {
        return (TipoConta) cbTipoConta.getSelectedItem();
    }

    public JComboBox<TipoConta> getCbTipoConta() {
        return cbTipoConta;
    }

    public JButton getBtnAdicionarProduto() {
        return btnAdicionarProduto;
    }

    public JButton getBtnRemoverProduto() {
        return btnRemoverProduto;
    }

    public JButton getBtnFinalizarVenda() {
        return btnFinalizarVenda;
    }

    public JButton getBtnPesquisar() {
        return btnPesquisar;
    }

    public JButton getBtnAlterar() {
        return btnAlterar;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public JButton getBtnLimpar() {
        return btnLimpar;
    }
}