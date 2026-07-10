package venda.siscom.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Usuario;
import venda.siscom.util.HibernateUtil;

public class UsuarioDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioDAO.class);

    
    public Usuario efetuarLogin(String login, String senha) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Usuario WHERE login = :login AND senha = :senha",
                    Usuario.class)
                    .setParameter("login", login)
                    .setParameter("senha", senha)
                    .uniqueResult();

        } catch (Exception e) {

            logger.error("Erro ao efetuar login.", e);

            return null;
        }
    }

}