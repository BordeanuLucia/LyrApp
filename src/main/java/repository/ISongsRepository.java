package repository;

import model.Song;
import java.util.Set;

public interface ISongsRepository extends IRepository<Song> {
    Set<Song> getSongsByKeyWords(String keyWords);
}
