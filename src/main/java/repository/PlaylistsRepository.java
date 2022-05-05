package repository;

import model.Playlist;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class PlaylistRepository extends AbstractHibernateRepository<Playlist> implements IPlaylistRepository {
    @Override
    protected Query<Playlist> getFindQuery(Session session, Long id) {
        return null;
    }

    @Override
    protected Query<Playlist> getFindAllQuery(Session session) {
        return null;
    }
}
