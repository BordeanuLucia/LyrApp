package observer;

import model.Playlist;
import model.Song;

public interface SongPlaylistObserver {
    void songAdded(Song song);
    void songUpdated(Song song);
    void songDeleted(Song song);
    void playlistAdded(Playlist playlist);
    void playlistUpdated(Playlist playlist);
    void playlistDeleted(Playlist playlist);
}
