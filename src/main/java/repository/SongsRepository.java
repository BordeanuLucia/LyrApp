package repository;

import model.Song;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class SongsRepository extends AbstractHibernateRepository<Song> implements ISongsRepository{
    private String newline = "\n";

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
            result = new HashSet<>(session.createQuery("SELECT p FROM Song p, IN (p.lyrics) t where t.text like '%" + keyWords + "%'", Song.class).list()); //or t.text like '%" + newline +  keyWords + "%'", Song.class).list());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Song entity = findOne(id);
            if (entity != null)
                session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }

        transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete from SongPlaylist where songId = " + id).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
        }
    }
}
