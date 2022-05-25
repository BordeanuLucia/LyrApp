package service;

import model.Playlist;
import model.Song;
import model.Strophe;
import repository.IPlaylistsRepository;
import repository.ISongsRepository;
import repository.IStrophesRepository;

import java.util.List;
import java.util.Set;

public class LyrAppService implements ILyrAppService{
    private final ISongsRepository songsRepository;
    private final IStrophesRepository strophesRepository;
    private final IPlaylistsRepository playlistRepository;

    public LyrAppService(ISongsRepository songsRepository, IPlaylistsRepository playlistRepository, IStrophesRepository strophesRepository) {
        this.songsRepository = songsRepository;
        this.playlistRepository = playlistRepository;
        this.strophesRepository = strophesRepository;
    }

    @Override
    public Set<Song> getFilteredSongs(String keyWords) {
        return songsRepository.getSongsByKeyWords(keyWords);
    }

    @Override
    public List<Song> getAllSongs() { return (List<Song>) songsRepository.getAll(); }

    @Override
    public void deleteSong(Song song) { songsRepository.delete(song.getId()); }

    @Override
    public long addSong(Song song) { return songsRepository.save(song); }

    @Override
    public Set<Playlist> getFilteredPlaylists(String keyWords) { return playlistRepository.getPlaylistsByKeyWords(keyWords); }

    @Override
    public List<Playlist> getAllPlaylists() { return (List<Playlist>) playlistRepository.getAll(); }

    @Override
    public void addStrophe(Strophe strophe) { strophesRepository.save(strophe); }

    @Override
    public void updateSong(Song song) {
        songsRepository.update(song);
    }

    @Override
    public void deleteStrophesForSong(long songId) {
        strophesRepository.deleteStrophesForSong(songId);
    }
}
