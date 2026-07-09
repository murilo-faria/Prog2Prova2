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

import venda.siscom.controller.TipoContaController;
import venda.siscom.model.TipoConta;

public class TipoContaView extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;

    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private JButton btnLimpar;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final TipoContaController controller =
            new TipoContaController();

    public TipoContaView() {

        setTitle("Cadastro de Tipos de Conta");

        setSize(700, 500);

        setLocationRelativeTo(null);

        setResizable(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        configurarAcoes();

        atualizarTabela();

    }

    private void inicializarComponentes() {

        JPanel painelCampos =
                new JPanel(new GridLayout(2,2,5,5));

        painelCampos.add(new JLabel("Código"));

        txtId = new JTextField();
        txtId.setEditable(false);

        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Descrição"));

        txtNome = new JTextField();

        painelCampos.add(txtNome);

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
                                "Descrição"
                        },0);

        tabela = new JTable(modeloTabela);

        add(painelCampos, BorderLayout.NORTH);

        add(new JScrollPane(tabela),
                BorderLayout.CENTER);

        add(painelBotoes,
                BorderLayout.SOUTH);
    }

        private void configurarAcoes() {

        tabela.getSelectionModel().addListSelectionListener(e -> {

            int linha = tabela.getSelectedRow();

            if (linha >= 0) {

                Integer id = (Integer) tabela.getValueAt(linha, 0);

                TipoConta tipo = controller.pesquisar(id);

                if (tipo != null) {

                    txtId.setText(String.valueOf(tipo.getId()));
                    txtNome.setText(tipo.getDescricao());

                }

            }

        });

        btnSalvar.addActionListener(e -> {

            TipoConta tipo = new TipoConta();

            tipo.setDescricao(txtNome.getText());

            if (controller.salvar(tipo)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Tipo de conta salvo com sucesso!");

                limparCampos();

                atualizarTabela();

            }

        });

        btnAlterar.addActionListener(e -> {

            if (txtId.getText().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um registro.");

                return;

            }

            TipoConta tipo = new TipoConta();

            tipo.setId(Integer.parseInt(txtId.getText()));

            tipo.setDescricao(txtNome.getText());

            if (controller.alterar(tipo)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Registro alterado.");

                limparCampos();

                atualizarTabela();

            }

        });

        btnExcluir.addActionListener(e -> {

            if (txtId.getText().isEmpty()) {

                return;

            }

            int op = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja excluir?");

            if (op == JOptionPane.YES_OPTION) {

                controller.excluir(
                        Integer.parseInt(txtId.getText()));

                limparCampos();

                atualizarTabela();

            }

        });

        btnPesquisar.addActionListener(e -> {

            String codigo =
                    JOptionPane.showInputDialog(
                            this,
                            "Informe o código:");

            if (codigo == null) {

                return;

            }

            TipoConta tipo =
                    controller.pesquisar(
                            Integer.parseInt(codigo));

            if (tipo != null) {

                txtId.setText(
                        String.valueOf(tipo.getId()));

                txtNome.setText(
                        tipo.getDescricao());

            }

        });

        btnLimpar.addActionListener(
                e -> limparCampos());

    }

    private void limparCampos() {

        txtId.setText("");

        txtNome.setText("");

        tabela.clearSelection();

    }

    private void atualizarTabela() {

        modeloTabela.setRowCount(0);

        List<TipoConta> lista =
                controller.pesquisarTodos();

        if (lista == null) {

            return;

        }

        for (TipoConta tipo : lista) {

            modeloTabela.addRow(new Object[]{

                    tipo.getId(),

                    tipo.getDescricao()

            });

        }

    }

}