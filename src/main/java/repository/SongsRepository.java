package repository;

import model.Song;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class SongsRepository extends AbstractHibernateRepository<Song> implements ISongsRepository{
    @Override
    protected Query<Song> getFindQuery(Session session, Long id) {
        return null;
    }

    @Override
    protected Query<Song> getFindAllQuery(Session session) {
        return null;
    }
}
