package repository;

import model.Strophe;

public interface IStrophesRepository extends IRepository<Strophe> {
    void deleteStrophesForSong(long songId);
}
