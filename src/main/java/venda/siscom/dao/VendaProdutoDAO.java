package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Venda;
import venda.siscom.model.VendaProduto;
import venda.siscom.util.HibernateUtil;

public class VendaProdutoDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(VendaProdutoDAO.class);

    
    public boolean salvar(VendaProduto vendaProduto) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(vendaProduto);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao salvar item da venda.", e);

            return false;
        }
    }

    
    public boolean alterar(VendaProduto vendaProduto) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(vendaProduto);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao alterar item da venda.", e);

            return false;
        }
    }

    
    public boolean excluir(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            VendaProduto vendaProduto = session.get(VendaProduto.class, id);

            if (vendaProduto != null) {

                session.remove(vendaProduto);

                transaction.commit();

                return true;
            }

            return false;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao excluir item da venda.", e);

            return false;
        }
    }

    
    public VendaProduto pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(VendaProduto.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar item da venda.", e);

            return null;
        }
    }

    
    public List<VendaProduto> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM VendaProduto",
                    VendaProduto.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar itens da venda.", e);

            return null;
        }
    }

    
    public List<VendaProduto> pesquisarPorVenda(Venda venda) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM VendaProduto WHERE venda = :venda",
                    VendaProduto.class)
                    .setParameter("venda", venda)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar itens por venda.", e);

            return null;
        }
    }

}