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

import venda.siscom.controller.CategoriaController;
import venda.siscom.model.Categoria;

public class CategoriaView extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;

    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private JButton btnLimpar;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final CategoriaController controller =
            new CategoriaController();

    public CategoriaView() {

        setTitle("Cadastro de Categorias");

        setSize(500, 350);

        setLocationRelativeTo(null);

        setResizable(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        configurarEventos();

        atualizarTabela();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JPanel painelCampos =
                new JPanel(new GridLayout(2, 2, 5, 5));

        painelCampos.add(new JLabel("Código"));

        txtId = new JTextField();
        txtId.setEditable(false);
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Nome"));

        txtNome = new JTextField();
        painelCampos.add(txtNome);

        add(painelCampos, BorderLayout.NORTH);

        modeloTabela =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Nome"
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

                            txtId.setText(
                                    String.valueOf(
                                            tabela.getValueAt(linha, 0)));

                            txtNome.setText(
                                    String.valueOf(
                                            tabela.getValueAt(linha, 1)));
                        }
                    }
                });

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

    private void configurarEventos() {

        btnSalvar.addActionListener(e -> salvar());

        btnAlterar.addActionListener(e -> alterar());

        btnExcluir.addActionListener(e -> excluir());

        btnPesquisar.addActionListener(e -> pesquisar());

        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void salvar() {

        if (txtNome.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome da categoria.");

            return;
        }

        Categoria categoria =
                new Categoria();

        categoria.setNome(
                txtNome.getText().trim());

        if (controller.salvar(categoria)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoria salva com sucesso.");

            limparCampos();

            atualizarTabela();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao salvar categoria.");
        }
    }

    private void alterar() {

        if (txtId.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma categoria primeiro.");

            return;
        }

        if (txtNome.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome da categoria.");

            return;
        }

        Categoria categoria =
                new Categoria();

        categoria.setId(
                Integer.parseInt(txtId.getText()));

        categoria.setNome(
                txtNome.getText().trim());

        if (controller.alterar(categoria)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoria alterada com sucesso.");

            limparCampos();

            atualizarTabela();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao alterar categoria.");
        }
    }

    private void excluir() {

        if (txtId.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma categoria primeiro.");

            return;
        }

        int opcao =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja excluir esta categoria?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

        if (opcao != JOptionPane.YES_OPTION) {

            return;
        }

        if (controller.excluir(
                Integer.parseInt(txtId.getText()))) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoria excluída com sucesso.");

            limparCampos();

            atualizarTabela();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir categoria.");
        }
    }

    private void pesquisar() {

        String nomePesquisa =
                JOptionPane.showInputDialog(
                        this,
                        "Informe o nome da categoria:");

        if (nomePesquisa == null || nomePesquisa.trim().isEmpty()) {

            return;
        }

        modeloTabela.setRowCount(0);

        List<Categoria> lista =
                controller.pesquisarTodos();

        if (lista == null) {

            return;
        }

        boolean encontrou =
                false;

        for (Categoria categoria : lista) {

            String nomeCategoria =
                    categoria.getNome();

            if (nomeCategoria != null &&
                    nomeCategoria.toLowerCase()
                            .contains(nomePesquisa.trim().toLowerCase())) {

                modeloTabela.addRow(new Object[]{

                        categoria.getId(),

                        categoria.getNome()
                });

                encontrou = true;
            }
        }

        if (!encontrou) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhuma categoria encontrada com esse nome.");

            atualizarTabela();
        }
    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        List<Categoria> lista =
                controller.pesquisarTodos();

        if (lista == null) {

            return;
        }

        for (Categoria categoria : lista) {

            modeloTabela.addRow(new Object[]{

                    categoria.getId(),

                    categoria.getNome()
            });
        }
    }

    private void limparCampos() {

        txtId.setText("");

        txtNome.setText("");

        tabela.clearSelection();

        atualizarTabela();

        txtNome.requestFocus();
    }
}