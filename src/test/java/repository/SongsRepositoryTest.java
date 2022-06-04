package repository;

import junit.framework.TestCase;
import model.Song;

import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class SongsRepositoryTest extends TestCase {
    private final SongsRepository songsRepository = new SongsRepository();
    long addedSongId = -1;
    Song addedSong = new Song("Test");

    public void testCRUD(){
        addSongTest();
        updateSongTest();
        deleteSongTest();
    }

    public void addSongTest() {
        int noSongsBeforeAdding = ((List<Song>) songsRepository.getAll()).size();
        addedSongId = songsRepository.save(addedSong);
        addedSong = songsRepository.findOne(addedSongId);
        assertEquals(addedSong.getTitle(), "Test");
        int noSongsAfterAdding = ((List<Song>) songsRepository.getAll()).size();
        assertEquals(noSongsBeforeAdding + 1, noSongsAfterAdding);
        assertNotEquals(addedSongId, -1);
    }

    public void updateSongTest(){
        int noSongsBeforeUpdating = ((List<Song>) songsRepository.getAll()).size();
        addedSong.setTitle("new test");
        addedSong.setId(addedSongId);
        songsRepository.update(addedSong);
        Song updatedSong = songsRepository.findOne(addedSongId);
        assertEquals(updatedSong.getTitle(), "new test");
        int noSongsAfterUpdating = ((List<Song>) songsRepository.getAll()).size();
        assertEquals(noSongsBeforeUpdating, noSongsAfterUpdating);
    }

    public void deleteSongTest() {
        int noSongsBeforeDeleting = ((List<Song>) songsRepository.getAll()).size();
        songsRepository.delete(addedSongId);
        int noSongsAfterDeleting = ((List<Song>) songsRepository.getAll()).size();
        assertEquals(noSongsBeforeDeleting - 1, noSongsAfterDeleting);
    }
}