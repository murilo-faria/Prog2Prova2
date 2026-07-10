package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Compra;
import venda.siscom.model.CompraProduto;
import venda.siscom.util.HibernateUtil;

public class CompraProdutoDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(CompraProdutoDAO.class);

    
    public boolean salvar(CompraProduto compraProduto) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(compraProduto);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao salvar item da compra.", e);

            return false;
        }
    }

    
    public boolean alterar(CompraProduto compraProduto) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(compraProduto);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao alterar item da compra.", e);

            return false;
        }
    }

    
    public boolean excluir(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            CompraProduto compraProduto = session.get(CompraProduto.class, id);

            if (compraProduto != null) {

                session.remove(compraProduto);

                transaction.commit();

                return true;
            }

            return false;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao excluir item da compra.", e);

            return false;
        }
    }

    
    public CompraProduto pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(CompraProduto.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar item da compra.", e);

            return null;
        }
    }

    
    public List<CompraProduto> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM CompraProduto",
                    CompraProduto.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar itens da compra.", e);

            return null;
        }
    }

    
    public List<CompraProduto> pesquisarPorCompra(Compra compra) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM CompraProduto WHERE compra = :compra",
                    CompraProduto.class)
                    .setParameter("compra", compra)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar itens por compra.", e);

            return null;
        }
    }

}