package repository;

import model.Playlist;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class PlaylistsRepository extends AbstractHibernateRepository<Playlist> implements IPlaylistsRepository {
    @Override
    protected Query<Playlist> getFindQuery(Session session, Long id) {
        return null;
    }

    @Override
    protected Query<Playlist> getFindAllQuery(Session session) {
        return null;
    }
}
