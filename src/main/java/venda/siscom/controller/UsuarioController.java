package venda.siscom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.dao.UsuarioDAO;
import venda.siscom.model.Usuario;

public class UsuarioController {

    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    
    public Usuario efetuarLogin(String login, String senha) {
        logger.info("Executando efetuar login. Usuario: {}", login);

        Usuario usuario = usuarioDAO.efetuarLogin(login, senha);

        logger.info("Resultado efetuar login. Usuario: {}, autenticado: {}",
                login,
                usuario != null);

        return usuario;
    }

    
}
