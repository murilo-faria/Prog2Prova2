package venda.siscom.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import venda.siscom.controller.UsuarioController;
import venda.siscom.model.Usuario;

public class LoginView extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;

    private JButton btnEntrar;
    private JButton btnSair;

    private final UsuarioController controller = new UsuarioController();

    public LoginView() {

        setTitle("SisCom - Login");
        setSize(350, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelCampos = new JPanel(new GridLayout(2, 2, 5, 5));

        painelCampos.add(new JLabel("Login:"));
        txtLogin = new JTextField();
        painelCampos.add(txtLogin);

        painelCampos.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        painelCampos.add(txtSenha);

        add(painelCampos, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        btnEntrar = new JButton("Entrar");
        btnSair = new JButton("Sair");

        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnSair);

        add(painelBotoes, BorderLayout.SOUTH);

        btnEntrar.addActionListener(e -> efetuarLogin());

        btnSair.addActionListener(e -> System.exit(0));

        getRootPane().setDefaultButton(btnEntrar);

        setVisible(true);
    }

    private void efetuarLogin() {

        String login = txtLogin.getText().trim();
        String senha = String.valueOf(txtSenha.getPassword());

        Usuario usuario = controller.efetuarLogin(login, senha);

        if (usuario != null) {

            JOptionPane.showMessageDialog(this,
                    "Bem-vindo, " + usuario.getNome() + "!");

            dispose();

            new PrincipalView().setVisible(true);

        } else {

            JOptionPane.showMessageDialog(this,
                    "Login ou senha inválidos!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

            txtSenha.setText("");
            txtLogin.requestFocus();
        }
    }

    public static void main(String[] args) {
        new LoginView();
    }
}