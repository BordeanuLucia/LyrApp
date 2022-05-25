package repository;

import model.Playlist;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class PlaylistsRepository extends AbstractHibernateRepository<Playlist> implements IPlaylistsRepository {
    @Override
    protected Query<Playlist> getFindQuery(Session session, Long id) {
        return session.createQuery("from Playlist where id=:id", Playlist.class)
                .setParameter("id", id);
    }

    @Override
    protected Query<Playlist> getFindAllQuery(Session session) {
        return session.createQuery("from Playlist ", Playlist.class);
    }

    @Override
    public Set<Playlist> getPlaylistsByKeyWords(String keyWords) {
        Transaction transaction = null;
        Set<Playlist> result = new HashSet<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            result = new HashSet<>(session.createQuery("from Playlist where title like '" + keyWords + "%'", Playlist.class).list());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
        return result;
    }
}
