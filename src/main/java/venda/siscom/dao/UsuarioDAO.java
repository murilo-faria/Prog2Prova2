package venda.siscom.dao;

import org.hibernate.Session;

import venda.siscom.model.Usuario;
import venda.siscom.util.HibernateUtil;

public class UsuarioDAO {

    
    public Usuario efetuarLogin(String login, String senha) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Usuario WHERE login = :login AND senha = :senha",
                    Usuario.class)
                    .setParameter("login", login)
                    .setParameter("senha", senha)
                    .uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

}