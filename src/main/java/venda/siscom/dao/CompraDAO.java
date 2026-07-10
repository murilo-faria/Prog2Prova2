package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Compra;
import venda.siscom.model.Fornecedor;
import venda.siscom.util.HibernateUtil;

public class CompraDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(CompraDAO.class);

    
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

            logger.error("Erro ao salvar compra.", e);
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

            logger.error("Erro ao alterar compra.", e);
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

            logger.error("Erro ao excluir compra.", e);
            return false;
        }
    }

    
    public Compra pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Compra.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar compra.", e);
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

            logger.error("Erro ao listar compras.", e);
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

            logger.error("Erro ao pesquisar compras por fornecedor.", e);
            return null;
        }
    }

    
    

}