package observer;

import model.Song;

public interface SongObservable {
    void addObserver(SongPlaylistObserver observer);
    void removeObserver(SongPlaylistObserver observer);
    void notifySongAdded(Song song);
    void notifySongUpdated(Song song);
    void notifySongDeleted(Song song);
}
