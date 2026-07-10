package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Categoria;
import venda.siscom.util.HibernateUtil;

public class CategoriaDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(CategoriaDAO.class);

    
    public boolean salvar(Categoria categoria) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(categoria);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao salvar categoria.", e);

            return false;
        }
    }

    
    public boolean alterar(Categoria categoria) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(categoria);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao alterar categoria.", e);

            return false;
        }
    }

    
    public boolean excluir(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Categoria categoria = session.get(Categoria.class, id);

            if (categoria != null) {

                session.remove(categoria);

                transaction.commit();

                return true;
            }

            return false;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao excluir categoria.", e);

            return false;
        }
    }

    
    public Categoria pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Categoria.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar categoria.", e);

            return null;
        }
    }

    
    public List<Categoria> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Categoria ORDER BY nome",
                    Categoria.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar categorias.", e);

            return null;
        }
    }

    
    public Categoria pesquisarPorNome(String nome) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Categoria WHERE lower(nome)=lower(:nome)",
                    Categoria.class)
                    .setParameter("nome", nome)
                    .uniqueResult();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar categoria por nome.", e);

            return null;
        }
    }

}