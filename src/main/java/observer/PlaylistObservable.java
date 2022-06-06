package observer;

import model.Playlist;

import java.util.ArrayList;
import java.util.List;

public interface PlaylistObservable {
    List<SongPlaylistObserver> observersList = new ArrayList<>();

    void addObserver(SongPlaylistObserver observer);
    void removeObserver(SongPlaylistObserver observer);
    void notifyPlaylistAdded(Playlist playlist);
    void notifyPlaylistUpdated(Playlist playlist);
}
