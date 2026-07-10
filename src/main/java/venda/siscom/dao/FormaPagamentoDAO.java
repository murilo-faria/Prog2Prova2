package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.FormaPagamento;
import venda.siscom.util.HibernateUtil;

public class FormaPagamentoDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(FormaPagamentoDAO.class);

    
    public boolean salvar(FormaPagamento formaPagamento) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(formaPagamento);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao salvar forma de pagamento.", e);
            return false;
        }
    }

    
    public boolean alterar(FormaPagamento formaPagamento) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(formaPagamento);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao alterar forma de pagamento.", e);
            return false;
        }
    }

    
    public boolean excluir(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            FormaPagamento formaPagamento = session.get(FormaPagamento.class, id);

            if (formaPagamento != null) {

                session.remove(formaPagamento);

                transaction.commit();

                return true;
            }

            return false;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao excluir forma de pagamento.", e);
            return false;
        }
    }

    
    public FormaPagamento pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(FormaPagamento.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar forma de pagamento.", e);
            return null;
        }
    }

    
    public List<FormaPagamento> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM FormaPagamento ORDER BY nome",
                    FormaPagamento.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar formas de pagamento.", e);
            return null;
        }
    }

    
    public FormaPagamento pesquisarPorNome(String nome) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM FormaPagamento WHERE lower(nome)=lower(:nome)",
                    FormaPagamento.class)
                    .setParameter("nome", nome)
                    .uniqueResult();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar forma de pagamento por nome.", e);
            return null;
        }
    }

    
    public FormaPagamento buscarOuCriarForma(FormaPagamento formaTela) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            FormaPagamento formaBanco = session.createQuery(
                    "FROM FormaPagamento WHERE lower(nome)=lower(:nome)",
                    FormaPagamento.class)
                    .setParameter("nome", formaTela.getNome().trim())
                    .uniqueResult();

            if (formaBanco != null) {

                transaction.commit();

                return formaBanco;
            }

            session.persist(formaTela);

            transaction.commit();

            return formaTela;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao buscar ou criar forma de pagamento.", e);
            return null;
        }
    }

}