package venda.siscom.dao;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Compra;
import venda.siscom.model.Financeiro;
import venda.siscom.model.FormaPagamento;
import venda.siscom.model.TipoConta;
import venda.siscom.model.Venda;
import venda.siscom.util.HibernateUtil;

public class FinanceiroDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(FinanceiroDAO.class);

    
    public boolean salvar(Financeiro financeiro) {

        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();

            session.persist(financeiro);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Erro ao salvar lancamento financeiro.", e);

            return false;
        }
    }

    
    public boolean alterar(Financeiro financeiro) {

        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();

            session.merge(financeiro);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Erro ao alterar lancamento financeiro.", e);

            return false;
        }
    }

    
    public boolean excluir(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();

            Financeiro financeiro =
                    session.get(Financeiro.class, id);

            if (financeiro == null) {
                return false;
            }

            
            
            if (financeiro.getVenda() != null
                    || financeiro.getCompra() != null) {

                logger.warn(
                        "Conta gerada por Compra/Venda nao pode ser excluida.");

                return false;
            }

            session.remove(financeiro);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Erro ao excluir lancamento financeiro.", e);

            return false;
        }
    }

    
    public Financeiro pesquisar(Integer id) {

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.get(Financeiro.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar lancamento financeiro.", e);

            return null;
        }
    }


public List<Financeiro> pesquisarTodos() {

    try (Session session = HibernateUtil
            .getSessionFactory()
            .openSession()) {

        return session.createQuery(
                "FROM Financeiro ORDER BY dataLancamento",
                Financeiro.class)
                .list();

    } catch (Exception e) {

        logger.error("Erro ao listar lancamentos financeiros.", e);

        return null;
    }
}

        
        public List<Financeiro> pesquisarPagar() {

        try (Session session = HibernateUtil
            .getSessionFactory()
            .openSession()) {

        return session.createQuery(
                "FROM Financeiro WHERE pagarOuReceber = 0 ORDER BY dataLancamento",
                Financeiro.class)
                .list();

        } catch (Exception e) {

        logger.error("Erro ao listar contas a pagar.", e);

        return null;
        }
    }

        
        public List<Financeiro> pesquisarReceber() {

        try (Session session = HibernateUtil
            .getSessionFactory()
            .openSession()) {

        return session.createQuery(
                "FROM Financeiro WHERE pagarOuReceber = 1 ORDER BY dataLancamento",
                Financeiro.class)
                .list();

         } catch (Exception e) {

        logger.error("Erro ao listar contas a receber.", e);

        return null;
        }
    }

        
        public List<Financeiro> pesquisarPorTipoConta(TipoConta tipoConta) {

        try (Session session = HibernateUtil
            .getSessionFactory()
            .openSession()) {

        return session.createQuery(
                "FROM Financeiro WHERE tipoConta = :tipo ORDER BY dataLancamento",
                Financeiro.class)
                .setParameter("tipo", tipoConta)
                .list();

     }  catch (Exception e) {

        logger.error("Erro ao pesquisar lancamentos por tipo de conta.", e);

        return null;
        }
    }

        
        public List<Financeiro> pesquisarPeriodo(
        LocalDate inicio,
        LocalDate fim) {

        try (Session session = HibernateUtil
            .getSessionFactory()
            .openSession()) {

            return session.createQuery(
                "FROM Financeiro WHERE dataLancamento BETWEEN :inicio AND :fim ORDER BY dataLancamento",
                Financeiro.class)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .list();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar lancamentos por periodo.", e);

            return null;
        }
    }

    public List<Financeiro> pesquisarFiltros(
            TipoConta tipoConta,
            FormaPagamento formaPagamento,
            Integer pagarOuReceber) {

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            StringBuilder hql =
                    new StringBuilder("FROM Financeiro WHERE 1 = 1");

            if (tipoConta != null) {
                hql.append(" AND tipoConta = :tipoConta");
            }

            if (formaPagamento != null) {
                hql.append(" AND formaPagamento = :formaPagamento");
            }

            if (pagarOuReceber != null) {
                hql.append(" AND pagarOuReceber = :pagarOuReceber");
            }

            hql.append(" ORDER BY dataLancamento");

            Query<Financeiro> query =
                    session.createQuery(hql.toString(), Financeiro.class);

            if (tipoConta != null) {
                query.setParameter("tipoConta", tipoConta);
            }

            if (formaPagamento != null) {
                query.setParameter("formaPagamento", formaPagamento);
            }

            if (pagarOuReceber != null) {
                query.setParameter("pagarOuReceber", pagarOuReceber);
            }

            return query.list();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar lancamentos financeiros.", e);

            return null;
        }
    }

    
    public Financeiro pesquisarPorVenda(Venda venda) {

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(

                    "FROM Financeiro WHERE venda = :venda",

                    Financeiro.class)

                    .setParameter("venda", venda)

                    .uniqueResult();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar lancamento por venda.", e);

            return null;
        }
    }

    
    public Financeiro pesquisarPorCompra(Compra compra) {

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(

                    "FROM Financeiro WHERE compra = :compra",

                    Financeiro.class)

                    .setParameter("compra", compra)

                    .uniqueResult();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar lancamento por compra.", e);

            return null;
        }
    }

}
