package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import venda.siscom.model.Venda;
import venda.siscom.model.VendaProduto;
import venda.siscom.util.HibernateUtil;

public class VendaProdutoDAO {

    // SALVAR
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

            e.printStackTrace();

            return false;
        }
    }

    // ALTERAR
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

            e.printStackTrace();

            return false;
        }
    }

    // EXCLUIR
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

            e.printStackTrace();

            return false;
        }
    }

    // PESQUISAR POR ID
    public VendaProduto pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(VendaProduto.class, id);

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // LISTAR TODOS
    public List<VendaProduto> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM VendaProduto",
                    VendaProduto.class)
                    .list();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // LISTAR ITENS DE UMA VENDA
    public List<VendaProduto> pesquisarPorVenda(Venda venda) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM VendaProduto WHERE venda = :venda",
                    VendaProduto.class)
                    .setParameter("venda", venda)
                    .list();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

}