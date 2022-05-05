package service;

import model.Song;
import repository.IPlaylistsRepository;
import repository.ISongsRepository;

import java.util.List;

public class LyrAppService implements ILyrAppService{
    private final ISongsRepository songsRepository;
    private final IPlaylistsRepository playlistRepository;

    public LyrAppService(ISongsRepository songsRepository, IPlaylistsRepository playlistRepository) {
        this.songsRepository = songsRepository;
        this.playlistRepository = playlistRepository;
    }

    @Override
    public List<Song> getFilteredSongs(String keyWords) {
        return songsRepository.getSongsByKeyWords(keyWords);
    }
}
