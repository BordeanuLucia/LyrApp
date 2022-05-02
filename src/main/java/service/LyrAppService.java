package service;

import repository.IPlaylistRepository;
import repository.ISongsRepository;

public class LyrAppService implements ILyrAppService{
    private ISongsRepository songsRepository;
    private IPlaylistRepository playlistRepository;

    public LyrAppService(ISongsRepository songsRepository, IPlaylistRepository playlistRepository) {
        this.songsRepository = songsRepository;
        this.playlistRepository = playlistRepository;
    }
}
