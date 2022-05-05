package service;

import model.Song;
import java.util.List;

public interface ILyrAppService {
    public List<Song> getFilteredSongs(String keyWords);
}
