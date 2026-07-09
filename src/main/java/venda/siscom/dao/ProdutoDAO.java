package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import venda.siscom.model.Categoria;
import venda.siscom.model.Produto;
import venda.siscom.util.HibernateUtil;

public class ProdutoDAO {

    
    public boolean salvar(Produto produto) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(produto);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean alterar(Produto produto) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(produto);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean excluir(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Produto produtoParaExcluir = session.get(Produto.class, id);
            
            if (produtoParaExcluir != null) {
                session.remove(produtoParaExcluir);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    
    public Produto pesquisar(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Produto.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public List<Produto> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Produto", Produto.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public Categoria buscarOuCriarCategoria(String nomeCategoria) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            
            Categoria categoria = session.createQuery("from Categoria where lower(nome) = lower(:nome)", Categoria.class)
                                         .setParameter("nome", nomeCategoria)
                                         .uniqueResult();

            
            if (categoria == null) {
                categoria = new Categoria();
                categoria.setNome(nomeCategoria);
                session.persist(categoria); 
            }

            transaction.commit();
            return categoria; 
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }
}