package repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public abstract class AbstractHibernateRepository<E> implements IRepository<E> {
    protected static SessionFactory sessionFactory;

    static {
        // connecting to the database and migrations
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Failed to create session factory");
        }
    }

    @Override
    public void save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        Transaction transaction = null;
//        Optional<E> result = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
//            result = Optional.of(entity);
        }
//        return result;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        Transaction transaction = null;
//        Optional<E> result = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            E entity = findOne(id);
            if (entity != null)
                session.delete(entity);
//            result = findOne(id);
//            result.ifPresent(session::delete);
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
        }
//        return result;
    }

    @Override
    public void update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        Transaction transaction = null;
//        Optional<E> result = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
//            result = Optional.of(entity);
        }
//        return result;
    }

    @Override
    public E findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        E result = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            E entity = getFindQuery(session, id)
                    .setMaxResults(1)
                    .uniqueResult();
            transaction.commit();
            result = entity != null ? entity : result;
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
        }
        return result;
    }

    @Override
    public Iterable<E> getAll() {
        Transaction transaction = null;
        List<E> result = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            result = getFindAllQuery(session).list();
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transaction != null)
                transaction.rollback();
        }

        return result;
    }

    /**
     * Method for obtaining the "find" query, according to the current entity
     *
     * @param session: Session, the current working session
     * @param id:      ID, the ID of the entity to find
     * @return a query for finding the entity with the given id
     */
    protected abstract Query<E> getFindQuery(Session session, Long id);

    /**
     * Method for obtaining the "find all" query, according to the current entity
     *
     * @param session: Session, the current working session
     * @return a query for finding all the entities
     */
    protected abstract Query<E> getFindAllQuery(Session session);
}