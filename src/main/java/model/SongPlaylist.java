package model;

import java.util.Objects;

public class SongPlaylist {
    private Long id;
    private Long songId;
    private Long playlistId;

    public SongPlaylist() {
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongPlaylist that = (SongPlaylist) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(songId, that.songId) &&
                Objects.equals(playlistId, that.playlistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, songId, playlistId);
    }

    @Override
    public String toString() {
        return "SongPlaylist{" +
                "id=" + id +
                ", songId=" + songId +
                ", playlistId=" + playlistId +
                '}';
    }
}
