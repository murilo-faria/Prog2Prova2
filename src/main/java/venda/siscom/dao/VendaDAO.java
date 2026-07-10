package venda.siscom.dao;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Cliente;
import venda.siscom.model.Venda;
import venda.siscom.util.HibernateUtil;

public class VendaDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(VendaDAO.class);

    
    public boolean salvar(Venda venda) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(venda);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao salvar venda.", e);
            return false;
        }
    }

    
    public boolean alterar(Venda venda) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(venda);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao alterar venda.", e);
            return false;
        }
    }

    
    public boolean excluir(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Venda venda = session.get(Venda.class, id);

            if (venda != null) {

                session.remove(venda);

                transaction.commit();

                return true;
            }

            return false;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao excluir venda.", e);
            return false;
        }
    }

    
    public Venda pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Venda.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar venda.", e);
            return null;
        }
    }

    
    public List<Venda> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Venda ORDER BY dataVenda DESC",
                    Venda.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar vendas.", e);
            return null;
        }
    }

    
    public List<Venda> pesquisarPorCliente(Cliente cliente) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Venda WHERE cliente = :cliente ORDER BY dataVenda DESC",
                    Venda.class)
                    .setParameter("cliente", cliente)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar vendas por cliente.", e);
            return null;
        }
    }

    public List<Venda> pesquisarPeriodo(LocalDate inicio, LocalDate fim) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Venda WHERE dataVenda BETWEEN :inicio AND :fim ORDER BY dataVenda",
                    Venda.class)
                    .setParameter("inicio", inicio)
                    .setParameter("fim", fim)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar vendas por periodo.", e);
            return null;
        }
    }

    public int contarVendasPorCpfNoMes(String cpf) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
            LocalDate fimMes = inicioMes.plusMonths(1);

            Long quantidade = session.createQuery(
                    "SELECT COUNT(v) " +
                    "FROM Venda v " +
                    "WHERE v.cliente.cpf = :cpf " +
                    "AND v.dataVenda >= :inicioMes " +
                    "AND v.dataVenda < :fimMes",
                    Long.class)
                    .setParameter("cpf", cpf)
                    .setParameter("inicioMes", inicioMes)
                    .setParameter("fimMes", fimMes)
                    .uniqueResult();

            return quantidade == null ? 0 : quantidade.intValue();

        } catch (Exception e) {

            logger.error("Erro ao contar vendas por CPF no mes.", e);
            return 0;
        }
    }

}