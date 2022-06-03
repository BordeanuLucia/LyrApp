package repository;

import model.Song;
import model.Strophe;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class StrophesRepository extends AbstractHibernateRepository<Strophe> implements IStrophesRepository {
    @Override
    protected Query<Strophe> getFindQuery(Session session, Long id) {
        return null;
    }

    @Override
    protected Query<Strophe> getFindAllQuery(Session session) {
        return null;
    }

    @Override
    public void deleteStrophesForSong(long songId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete from Strophe where songId = " + songId).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
        }
    }
}