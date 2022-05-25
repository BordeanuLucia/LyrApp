package repository;

import model.Playlist;

import java.util.Set;

public interface IPlaylistsRepository extends IRepository<Playlist> {
    Set<Playlist> getPlaylistsByKeyWords(String keyWords);
}
