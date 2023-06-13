package htwb.ai.controller;

import com.fasterxml.jackson.annotation.JsonValue;
import htwb.ai.modell.Song;
import htwb.ai.repo.SongRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/songsWS-gabs-KBE/rest")
public class SongController {

    private final SongRepository songRepository;

    public SongController(SongRepository repo) {
        this.songRepository = repo;
    }

    @GetMapping(value = "/songs")
    public Iterable<Song> getAllSongs() {
        return songRepository.findAll();
    }


    @GetMapping("/songs/{id}")
    public Song getSongById(@PathVariable(value = "id") long id) {
        return songRepository.findById(id).get();}

    @PostMapping("/songs")
    public ResponseEntity<?> createSong(@RequestBody Song song) {

        System.out.println("RECEIVED: " + song.toString());

        Song savedSong = songRepository.save(song);
        String path ="/rest/songs" + savedSong.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.set("location", path);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/songs/{id}")
    public Song updateSong(@PathVariable(value = "id") long id,
                           @RequestBody Song songToPut) {
        Song song = songRepository.findById(id).get();

        song.setTitle(songToPut.getTitle());
        song.setArtist(songToPut.getArtist());
        song.setLabel(songToPut.getLabel());
        song.setReleased(songToPut.getReleased());
      Song updatedSong = songRepository.save(song);
        return updatedSong;


    }

    //fix to delete song from db
    @DeleteMapping("/songs/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable(value = "id") long id) {
        Song song= songRepository.findById(id).get();
        songRepository.delete(song);
        return ResponseEntity.noContent().build();
    }

}