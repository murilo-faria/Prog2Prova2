package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Fornecedor;
import venda.siscom.util.HibernateUtil;

public class FornecedorDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(FornecedorDAO.class);

    
    public boolean salvar(Fornecedor fornecedor) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(fornecedor);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao salvar fornecedor.", e);

            return false;
        }
    }

    
    public boolean alterar(Fornecedor fornecedor) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(fornecedor);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao alterar fornecedor.", e);

            return false;
        }
    }

    
    public boolean excluir(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Fornecedor fornecedor = session.get(Fornecedor.class, id);

            if (fornecedor != null) {

                session.remove(fornecedor);

                transaction.commit();

                return true;
            }

            return false;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao excluir fornecedor.", e);

            return false;
        }
    }

    
    public Fornecedor pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Fornecedor.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar fornecedor.", e);

            return null;
        }
    }

    
    public List<Fornecedor> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Fornecedor ORDER BY nomeFantasia",
                    Fornecedor.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar fornecedores.", e);

            return null;
        }
    }

    
    public Fornecedor pesquisarPorCnpj(String cnpj) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Fornecedor WHERE cnpj = :cnpj",
                    Fornecedor.class)
                    .setParameter("cnpj", cnpj)
                    .uniqueResult();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar fornecedor por CNPJ.", e);

            return null;
        }
    }

}