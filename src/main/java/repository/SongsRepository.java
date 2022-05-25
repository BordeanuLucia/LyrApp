package repository;

import model.Song;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

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
    public Set<Song> getSongsByKeyWords(String keyWords) {
        Transaction transaction = null;
        Set<Song> result = new HashSet<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            result = new HashSet<>(session.createQuery("from Song where title like '" + keyWords + "%'", Song.class).list());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
//        Set<Song> result2 = new HashSet<>();
//        try (Session session = sessionFactory.openSession()) {
//            transaction = session.beginTransaction();
//            result2 = new HashSet<>(session.createQuery("from Strophe where text like '" + keyWords + "%'", Strophe.class).list());
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null)
//                transaction.rollback();
//        }
//        result.addAll(result2);
        return result;
    }
}
