package service;

import model.Playlist;
import model.Song;
import model.Strophe;

import java.util.List;
import java.util.Set;

public interface ILyrAppService {
    Set<Song> getFilteredSongs(String keyWords);
    List<Song> getAllSongs();
    Set<Playlist> getFilteredPlaylists(String keyWords);
    List<Playlist> getAllPlaylists();
    void deleteSong(Song song);
    long addSong(Song song);
    long addPlaylist(Playlist playlist);
    void addStrophe(Strophe strophe);
    void updateSong(Song song);
    void deleteStrophesForSong(long songId);
    void deletePlaylist(Playlist playlist);
}
