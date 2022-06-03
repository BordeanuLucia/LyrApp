package model;

import java.util.Objects;
import java.util.Set;

/**
 * Class to create playlists of multiple songs
 */
public class Playlist {
    private Long id;
    private String title;
    private Set<Song> songs;

    public Playlist(String title, Set<Song> songs) {
        this.title = title;
        this.songs = songs;
    }

    public Playlist() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
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

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", songs=" + songs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id) &&
                Objects.equals(title, playlist.title) &&
                Objects.equals(songs, playlist.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, songs);
    }
}