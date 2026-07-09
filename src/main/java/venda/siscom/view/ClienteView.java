package venda.siscom.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import venda.siscom.controller.ClienteController;
import venda.siscom.model.Cliente;

public class ClienteView extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtRg;
    private JTextField txtEndereco;
    private JTextField txtTelefone;

    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private JButton btnLimpar;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final ClienteController clienteController =
            new ClienteController();

    public ClienteView() {

        setTitle("Cadastro de Clientes");

        setSize(900, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        configurarAcoes();

        atualizarTabela();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JPanel painelCampos =
                new JPanel(new GridLayout(6, 2, 5, 5));

        painelCampos.add(new JLabel("Código"));

        txtId = new JTextField();
        txtId.setEditable(false);
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Nome"));

        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("CPF"));

        txtCpf = new JTextField();
        painelCampos.add(txtCpf);

        painelCampos.add(new JLabel("RG"));

        txtRg = new JTextField();
        painelCampos.add(txtRg);

        painelCampos.add(new JLabel("Endereço"));

        txtEndereco = new JTextField();
        painelCampos.add(txtEndereco);

        painelCampos.add(new JLabel("Telefone"));

        txtTelefone = new JTextField();
        painelCampos.add(txtTelefone);

        add(painelCampos, BorderLayout.NORTH);

        modeloTabela =
                new DefaultTableModel(
                        new Object[]{
                                "Código",
                                "Nome",
                                "CPF",
                                "Telefone"
                        }, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        tabela = new JTable(modeloTabela);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

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

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void configurarAcoes() {

        btnSalvar.addActionListener(e -> salvar());

        btnAlterar.addActionListener(e -> alterar());

        btnExcluir.addActionListener(e -> excluir());

        btnPesquisar.addActionListener(e -> pesquisarPorNome());

        btnLimpar.addActionListener(e -> limparCampos());

        tabela.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int linha =
                        tabela.getSelectedRow();

                if (linha >= 0) {

                    Integer id =
                            (Integer) modeloTabela.getValueAt(linha, 0);

                    Cliente cliente =
                            clienteController.pesquisar(id);

                    if (cliente != null) {

                        preencherCampos(cliente);
                    }
                }
            }
        });
    }

    private void salvar() {

        if (txtNome.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome do cliente.");

            return;
        }

        Cliente cliente =
                new Cliente();

        cliente.setNome(
                txtNome.getText().trim());

        cliente.setCpf(
                txtCpf.getText().trim());

        cliente.setRg(
                txtRg.getText().trim());

        cliente.setEndereco(
                txtEndereco.getText().trim());

        cliente.setTelefone(
                txtTelefone.getText().trim());

        if (clienteController.salvar(cliente)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente cadastrado com sucesso.");

            limparCampos();

            atualizarTabela();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao salvar cliente.");
        }
    }

    private void alterar() {

        if (txtId.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um cliente.");

            return;
        }

        if (txtNome.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome do cliente.");

            return;
        }

        Cliente cliente =
                new Cliente();

        cliente.setId(
                Integer.parseInt(txtId.getText()));

        cliente.setNome(
                txtNome.getText().trim());

        cliente.setCpf(
                txtCpf.getText().trim());

        cliente.setRg(
                txtRg.getText().trim());

        cliente.setEndereco(
                txtEndereco.getText().trim());

        cliente.setTelefone(
                txtTelefone.getText().trim());

        if (clienteController.alterar(cliente)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente alterado com sucesso.");

            limparCampos();

            atualizarTabela();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao alterar cliente.");
        }
    }

    private void excluir() {

        if (txtId.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um cliente.");

            return;
        }

        int opcao =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente excluir este cliente?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

        if (opcao != JOptionPane.YES_OPTION) {

            return;
        }

        if (clienteController.excluir(
                Integer.parseInt(txtId.getText()))) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente excluído com sucesso.");

            limparCampos();

            atualizarTabela();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir cliente.");
        }
    }

    private void pesquisarPorNome() {

        String nomePesquisa =
                JOptionPane.showInputDialog(
                        this,
                        "Informe o nome do cliente:");

        if (nomePesquisa == null || nomePesquisa.trim().isEmpty()) {

            return;
        }

        modeloTabela.setRowCount(0);

        List<Cliente> lista =
                clienteController.pesquisarTodos();

        if (lista == null) {

            return;
        }

        boolean encontrou =
                false;

        for (Cliente cliente : lista) {

            String nomeCliente =
                    cliente.getNome();

            if (nomeCliente != null &&
                    nomeCliente.toLowerCase()
                            .contains(nomePesquisa.trim().toLowerCase())) {

                modeloTabela.addRow(new Object[]{

                        cliente.getId(),

                        cliente.getNome(),

                        cliente.getCpf(),

                        cliente.getTelefone()
                });

                encontrou = true;
            }
        }

        if (!encontrou) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhum cliente encontrado com esse nome.");

            atualizarTabela();
        }
    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        List<Cliente> lista =
                clienteController.pesquisarTodos();

        if (lista != null) {

            for (Cliente cliente : lista) {

                modeloTabela.addRow(new Object[]{

                        cliente.getId(),

                        cliente.getNome(),

                        cliente.getCpf(),

                        cliente.getTelefone()
                });
            }
        }
    }

    private void preencherCampos(Cliente cliente) {

        txtId.setText(
                String.valueOf(cliente.getId()));

        txtNome.setText(
                cliente.getNome());

        txtCpf.setText(
                cliente.getCpf());

        txtRg.setText(
                cliente.getRg());

        txtEndereco.setText(
                cliente.getEndereco());

        txtTelefone.setText(
                cliente.getTelefone());
    }

    private void limparCampos() {

        txtId.setText("");

        txtNome.setText("");

        txtCpf.setText("");

        txtRg.setText("");

        txtEndereco.setText("");

        txtTelefone.setText("");

        tabela.clearSelection();

        atualizarTabela();

        txtNome.requestFocus();
    }
}