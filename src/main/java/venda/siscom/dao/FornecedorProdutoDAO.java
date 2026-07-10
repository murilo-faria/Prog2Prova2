package venda.siscom.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import venda.siscom.model.Fornecedor;
import venda.siscom.model.FornecedorProduto;
import venda.siscom.model.Produto;
import venda.siscom.util.HibernateUtil;

public class FornecedorProdutoDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(FornecedorProdutoDAO.class);

    
    public boolean salvar(FornecedorProduto fornecedorProduto) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(fornecedorProduto);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao salvar vinculo de fornecedor/produto.", e);

            return false;
        }
    }

    
    public boolean alterar(FornecedorProduto fornecedorProduto) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(fornecedorProduto);

            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao alterar vinculo de fornecedor/produto.", e);

            return false;
        }
    }

    
    public boolean excluir(Integer id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            FornecedorProduto fornecedorProduto = session.get(FornecedorProduto.class, id);

            if (fornecedorProduto != null) {

                session.remove(fornecedorProduto);

                transaction.commit();

                return true;
            }

            return false;

        } catch (Exception e) {

            if (transaction != null)
                transaction.rollback();

            logger.error("Erro ao excluir vinculo de fornecedor/produto.", e);

            return false;
        }
    }

    
    public FornecedorProduto pesquisar(Integer id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(FornecedorProduto.class, id);

        } catch (Exception e) {

            logger.error("Erro ao pesquisar vinculo de fornecedor/produto.", e);

            return null;
        }
    }

    
    public List<FornecedorProduto> pesquisarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM FornecedorProduto",
                    FornecedorProduto.class)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao listar vinculos de fornecedor/produto.", e);

            return null;
        }
    }

    
    public List<FornecedorProduto> pesquisarPorProduto(Produto produto) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM FornecedorProduto WHERE produto = :produto",
                    FornecedorProduto.class)
                    .setParameter("produto", produto)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar vinculos por produto.", e);

            return null;
        }
    }

    
    public List<FornecedorProduto> pesquisarPorFornecedor(Fornecedor fornecedor) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM FornecedorProduto WHERE fornecedor = :fornecedor",
                    FornecedorProduto.class)
                    .setParameter("fornecedor", fornecedor)
                    .list();

        } catch (Exception e) {

            logger.error("Erro ao pesquisar vinculos por fornecedor.", e);

            return null;
        }
    }

}