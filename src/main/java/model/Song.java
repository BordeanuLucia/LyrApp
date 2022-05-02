package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class used to store each song's information
 */
public class Song {
    private Long id;
    private String title;
    private List<Strophe> lyrics;

    public Song() {
    }

    public Song(Long id, String title) {
        this.id = id;
        this.title = title;
        this.lyrics = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Strophe> getLyrics() {
        return lyrics;
    }

    public void setLyrics(List<Strophe> lyrics) {
        this.lyrics = lyrics;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", lyrics=" + lyrics +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id) &&
                Objects.equals(title, song.title) &&
                Objects.equals(lyrics, song.lyrics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, lyrics);
    }
}