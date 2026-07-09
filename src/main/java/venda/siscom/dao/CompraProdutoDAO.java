package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import venda.siscom.model.Compra;
import venda.siscom.model.CompraProduto;
import venda.siscom.util.HibernateUtil;

public class CompraProdutoDAO {

    // SALVAR
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

            e.printStackTrace();

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

            e.printStackTrace();

            return false;
        }
    }

    // EXCLUIR
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

            e.printStackTrace();

            return false;
        }
    }

    // PESQUISAR POR ID
    public CompraProduto pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(CompraProduto.class, id);

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // LISTAR TODOS
    public List<CompraProduto> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM CompraProduto",
                    CompraProduto.class)
                    .list();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // LISTAR ITENS DE UMA COMPRA
    public List<CompraProduto> pesquisarPorCompra(Compra compra) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM CompraProduto WHERE compra = :compra",
                    CompraProduto.class)
                    .setParameter("compra", compra)
                    .list();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

}