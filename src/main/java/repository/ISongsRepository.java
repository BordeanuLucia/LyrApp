package repository;

import model.Song;
import java.util.List;

public interface ISongsRepository extends IRepository<Song> {
    List<Song> getSongsByKeyWords(String keyWords);
}
