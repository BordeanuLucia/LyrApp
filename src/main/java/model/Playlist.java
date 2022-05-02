package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class to create playlists of multiple songs
 */
public class Playlist {
    private Long id;
    private List<Song> songs;

    public Playlist(Long id) {
        this.id = id;
        this.songs = new ArrayList<>();
    }

    public Playlist() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public void removeSong(long idSong) {
        int i = 0;
        for (Song song : this.songs) {
            if (song.getId() == idSong)
                this.songs.remove(i);
            i++;
        }
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", songs=" + songs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id) &&
                Objects.equals(songs, playlist.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, songs);
    }
}