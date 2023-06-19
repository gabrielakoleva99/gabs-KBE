package htwb.ai.controller;

import htwb.ai.exception.RessourceNotFoundException;
import htwb.ai.modell.Song;
import htwb.ai.repo.SongRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/songsWS-gabs-KBE/rest")
public class SongController {

    private final SongRepository songRepository;

    public SongController(SongRepository repo) {
        this.songRepository = repo;
    }

    @GetMapping(path = "/songs", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Iterable<Song> getAllSongs() {
        System.out.println(songRepository.findAll());
        return songRepository.findAll();
    }


    //write get handeling certain exceptions
    @GetMapping(path = "/songs/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getSongById(@PathVariable(value = "id") long id) {
        try {
            Song song = songRepository.findById(id).get();
            if (!song.equals(null)) {
                return new ResponseEntity<Song>(song, HttpStatus.OK);
            }else if(song.equals(null)) throw new NoSuchElementException();


        } catch (NullPointerException |NoSuchElementException e) {
            return new ResponseEntity<Song>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Song>(HttpStatus.NOT_FOUND);

    }


    @PostMapping(path = "/songs", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createSong(@RequestBody Song song) {

        System.out.println("RECEIVED: " + song.toString());
        try {
            Song savedSong = songRepository.save(song);
            if (song.getTitle().equals(null)) {
                return ResponseEntity.badRequest().build();
            }
            //maybe change to savedSong.getId()
            String path = "Location: /songsWS-gabs-KBE/rest/songs?songId=" + song.getId();
            HttpHeaders headers = new HttpHeaders();
            headers.set("location", path);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //fix
//    @PutMapping(path="/songs/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<Song> updateSong(@PathVariable(value = "id") long id,
//                           @RequestBody Song songToPut) {
//        Song changingSong = songRepository.findById(id).get();
//
//        if(changingSong !=null&&id== songToPut.getId()){
//            songToPut.setId((int) id);
//            songRepository.save(songToPut);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }

    //add status code 404 or 405 when unexisting id is chosen
    @PutMapping(path = "/songs/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Song> updateSong(@PathVariable(value = "id") Long id,
                                           @RequestBody Song songToPut) {
      try {
          Song song = songRepository.findById(id)
                  .orElseThrow(() -> new RessourceNotFoundException("Note", "id", id));

          //   song.setId(songToPut.getId());

          song.setTitle(songToPut.getTitle());
          song.setArtist(songToPut.getArtist());
          song.setLabel(songToPut.getLabel());
          song.setReleased(songToPut.getReleased());
          songRepository.save(song);
          return ResponseEntity.ok(song);
      } catch (RessourceNotFoundException e) {
        return ResponseEntity.notFound().build();

    }

    }


    @DeleteMapping(path = "/songs/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable(value = "id") long id) {
        try {
            System.out.println("hello i am mcpuhi");
            Song song = songRepository.findById(id)
                    .orElseThrow(() -> new RessourceNotFoundException("User", "id", id));

            if (song != null) {
                System.out.println("PIIINER MAIZEN PIIIENER \n");
                System.out.println(ResponseEntity.noContent().build());
                songRepository.delete(song);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();


        } catch (RessourceNotFoundException e) {
            return ResponseEntity.badRequest().build();

        }


    }
}



