package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import venda.siscom.model.Compra;
import venda.siscom.model.Fornecedor;
import venda.siscom.util.HibernateUtil;

public class CompraDAO {

    
    public boolean salvar(Compra compra) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(compra);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
            return false;
        }
    }

    
    public boolean alterar(Compra compra) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(compra);

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

            Compra compra = session.get(Compra.class, id);

            if (compra != null) {

                session.remove(compra);

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

    
    public Compra pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Compra.class, id);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    
    public List<Compra> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Compra ORDER BY dataCompra DESC",
                    Compra.class)
                    .list();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    
    public List<Compra> pesquisarPorFornecedor(Fornecedor fornecedor) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Compra WHERE fornecedor = :fornecedor ORDER BY dataCompra DESC",
                    Compra.class)
                    .setParameter("fornecedor", fornecedor)
                    .list();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    
    

}