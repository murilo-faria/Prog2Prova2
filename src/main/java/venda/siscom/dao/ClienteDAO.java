package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Cliente;
import venda.siscom.util.HibernateUtil;

public class ClienteDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(ClienteDAO.class);

    
    public boolean salvar(Cliente cliente) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(cliente);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao salvar cliente.", e);

            return false;
        }
    }

    
    public boolean alterar(Cliente cliente) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(cliente);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao alterar cliente.", e);

            return false;
        }
    }

    
    public boolean excluir(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Cliente cliente = session.get(Cliente.class, id);

            if (cliente != null) {

                session.remove(cliente);

                transaction.commit();

                return true;
            }

            return false;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao excluir cliente.", e);

            return false;
        }
    }

    
    public Cliente pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Cliente.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar cliente.", e);

            return null;
        }
    }

    
    public List<Cliente> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Cliente ORDER BY nome",
                    Cliente.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar clientes.", e);

            return null;
        }
    }

    
    public Cliente pesquisarPorCpf(String cpf) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Cliente WHERE cpf = :cpf",
                    Cliente.class)
                    .setParameter("cpf", cpf)
                    .uniqueResult();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar cliente por CPF.", e);

            return null;
        }
    }

}