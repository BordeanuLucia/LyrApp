package service;

import model.Song;
import repository.IPlaylistsRepository;
import repository.ISongsRepository;

import java.util.List;
import java.util.Set;

public class LyrAppService implements ILyrAppService{
    private final ISongsRepository songsRepository;
    private final IPlaylistsRepository playlistRepository;

    public LyrAppService(ISongsRepository songsRepository, IPlaylistsRepository playlistRepository) {
        this.songsRepository = songsRepository;
        this.playlistRepository = playlistRepository;
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
    public void addSong(Song song) {
        songsRepository.save(song);
    }
}
