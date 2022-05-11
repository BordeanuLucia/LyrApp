package service;

import model.Song;
import java.util.List;

public interface ILyrAppService {
    List<Song> getFilteredSongs(String keyWords);
    List<Song> getAllSongs();
}
