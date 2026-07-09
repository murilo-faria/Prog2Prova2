package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import venda.siscom.model.TipoConta;
import venda.siscom.util.HibernateUtil;

public class TipoContaDAO {

    
    public boolean salvar(TipoConta tipoConta) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(tipoConta);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();

            return false;
        }
    }

    
    public boolean alterar(TipoConta tipoConta) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(tipoConta);

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

            TipoConta tipoConta = session.get(TipoConta.class, id);

            if (tipoConta != null) {

                session.remove(tipoConta);

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

    
    public TipoConta pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(TipoConta.class, id);

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    
    public List<TipoConta> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM TipoConta ORDER BY descricao",
                    TipoConta.class)
                    .list();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    
    public TipoConta pesquisarPorDescricao(String descricao) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM TipoConta WHERE lower(descricao)=lower(:descricao)",
                    TipoConta.class)
                    .setParameter("descricao", descricao)
                    .uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    
    public TipoConta buscarOuCriarConta(String nomeConta) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            TipoConta tipoConta = session.createQuery(
                    "FROM TipoConta WHERE lower(descricao)=lower(:nome)",
                    TipoConta.class)
                    .setParameter("nome", nomeConta)
                    .uniqueResult();

            if (tipoConta == null) {

                tipoConta = new TipoConta();

                tipoConta.setDescricao(nomeConta);

                session.persist(tipoConta);
            }

            transaction.commit();

            return tipoConta;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();

            return null;
        }
    }

}