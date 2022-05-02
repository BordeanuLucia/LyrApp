package model;

import java.util.Objects;

/**
 * Class to store a verse of a song
 */
public class Strophe {
    private Long position;
    private String text;
    private Long songId;

    public Strophe() {
    }

    public Strophe(Long position, String text, Long songId) {
        this.position = position;
        this.text = text;
        this.songId = songId;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    @Override
    public String toString() {
        return "Verse{" +
                "position=" + position +
                ", text='" + text + '\'' +
                ", songId=" + songId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Strophe strophe = (Strophe) o;
        return Objects.equals(position, strophe.position) &&
                Objects.equals(text, strophe.text) &&
                Objects.equals(songId, strophe.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, text, songId);
    }
}
