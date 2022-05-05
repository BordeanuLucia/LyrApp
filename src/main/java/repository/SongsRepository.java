package repository;

import model.Song;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SongsRepository extends AbstractHibernateRepository<Song> implements ISongsRepository{
    @Override
    protected Query<Song> getFindQuery(Session session, Long id) {
        return session.createQuery("from Song where id=:id", Song.class)
                .setParameter("id", id);
    }

    @Override
    protected Query<Song> getFindAllQuery(Session session) {
        return session.createQuery("from Song", Song.class);
    }

    @Override
    public List<Song> getSongsByKeyWords(String keyWords) {
        Transaction transaction = null;
        List<Song> result = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("from Song where title like '%" + keyWords + "'", Song.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
        return result;
    }
}
