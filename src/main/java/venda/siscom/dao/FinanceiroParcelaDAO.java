package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Financeiro;
import venda.siscom.model.FinanceiroParcela;
import venda.siscom.util.HibernateUtil;

public class FinanceiroParcelaDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(FinanceiroParcelaDAO.class);

    
    public boolean salvar(FinanceiroParcela financeiroParcela) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(financeiroParcela);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Erro ao salvar parcela financeira.", e);
            return false;
        }
    }

    
    public boolean alterar(FinanceiroParcela financeiroParcela) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(financeiroParcela);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Erro ao alterar parcela financeira.", e);
            return false;
        }
    }

    
    public boolean excluir(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            FinanceiroParcela financeiroParcela = session.get(FinanceiroParcela.class, id);

            if (financeiroParcela != null) {

                session.remove(financeiroParcela);

                transaction.commit();

                return true;
            }

            return false;

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Erro ao excluir parcela financeira.", e);
            return false;
        }
    }

    
    public FinanceiroParcela pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(FinanceiroParcela.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar parcela financeira.", e);
            return null;
        }
    }

    
    public List<FinanceiroParcela> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM FinanceiroParcela ORDER BY dataVencimento",
                    FinanceiroParcela.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar parcelas financeiras.", e);
            return null;
        }
    }

    
    public List<FinanceiroParcela> pesquisarPorFinanceiro(Financeiro financeiro) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM FinanceiroParcela WHERE financeiro = :financeiro ORDER BY numeroParcela",
                    FinanceiroParcela.class)
                    .setParameter("financeiro", financeiro)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar parcelas por lancamento financeiro.", e);
            return null;
        }
    }

    
    public List<FinanceiroParcela> pesquisarPendentes() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM FinanceiroParcela WHERE status = 0 ORDER BY dataVencimento",
                    FinanceiroParcela.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar parcelas pendentes.", e);
            return null;
        }
    }

    
    public List<FinanceiroParcela> pesquisarPagas() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM FinanceiroParcela WHERE status = 1 ORDER BY dataPagamento",
                    FinanceiroParcela.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar parcelas pagas.", e);
            return null;
        }
    }

}