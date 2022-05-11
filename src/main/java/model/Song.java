package model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class used to store each song's information
 */
public class Song {
    private Long id;
    private String title;
    private Set<Strophe> lyrics;

    public Song() {
    }

    public Song(String title, Set<Strophe> lyrics) {
        this.title = title;
        this.lyrics = lyrics;
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

    public Set<Strophe> getLyrics() {
        return lyrics;
    }

    public void setLyrics(Set<Strophe> lyrics) {
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

    public List<Strophe> getOrderedLyrics() {
        return lyrics.stream().sorted((o1, o2) -> {
            if (o1.getPosition() <= o2.getPosition())
                return 0;
            else
                return 1;
        }).collect(Collectors.toList());
    }
}