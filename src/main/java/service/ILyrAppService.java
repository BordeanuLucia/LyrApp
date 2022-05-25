package service;

import model.Playlist;
import model.Song;
import java.util.List;
import java.util.Set;

public interface ILyrAppService {
    Set<Song> getFilteredSongs(String keyWords);
    List<Song> getAllSongs();
    Set<Playlist> getFilteredPlaylists(String keyWords);
    List<Playlist> getAllPlaylists();
    void deleteSong(Song song);
    void addSong(Song song);
}
