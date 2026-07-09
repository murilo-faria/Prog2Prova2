package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import venda.siscom.model.Fornecedor;
import venda.siscom.util.HibernateUtil;

public class FornecedorDAO {

    
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

            e.printStackTrace();

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

            e.printStackTrace();

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

            e.printStackTrace();

            return false;
        }
    }

    
    public Fornecedor pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Fornecedor.class, id);

        } catch (Exception e) {

            e.printStackTrace();

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

            e.printStackTrace();

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

            e.printStackTrace();

            return null;
        }
    }

}