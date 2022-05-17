package service;

import model.Song;
import java.util.List;
import java.util.Set;

public interface ILyrAppService {
    Set<Song> getFilteredSongs(String keyWords);
    List<Song> getAllSongs();
    void deleteSong(Song song);
    void addSong(Song song);
}
