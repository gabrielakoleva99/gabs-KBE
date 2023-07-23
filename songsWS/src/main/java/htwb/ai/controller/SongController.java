package htwb.ai.controller;

import htwb.ai.exception.RessourceNotFoundException;
import htwb.ai.exception.UnsuccessfulAuthorizationException;
import htwb.ai.modell.Song;
import htwb.ai.repo.SongRepository;
import htwb.ai.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public SongController(SongRepository repo, UserRepository urepo) {
        this.songRepository = repo;
        this.ur = urepo;
        us = new UserController(ur);
    }


    UserRepository ur;
    UserController us;

    public SongController(SongRepository repo) {
        this.songRepository = repo;
    }

    @GetMapping(path = "/songs", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Iterable<Song> getAllSongs(@RequestHeader(value = "Authorization") String authHeader) {
        try {
            if (us.getByToken(authHeader).getToken().equals(authHeader)) {
                return songRepository.findAll();
            } else {
                throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
            }
        } catch (NullPointerException e) {
        }
        throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
    }



    //write get handeling certain exceptions
    @GetMapping(path = "/songs/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getSongById(@PathVariable(value = "id") long id, @RequestHeader(value = "Authorization") String authHeader) {
        try {
            if (us.getByToken(authHeader).getToken().equals(authHeader)) {
                Song song = songRepository.findById(id)
                        .orElseThrow(() -> new RessourceNotFoundException("Song", "id", id));
                System.out.println("mql");
                return new ResponseEntity<Song>(song, HttpStatus.OK);
            } else if(!us.getByToken(authHeader).getToken().equals(authHeader)){
                System.out.println("pryc");
                throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
            }
        } catch (NullPointerException | NoSuchElementException e) {
            System.out.println("gruh");
            return new ResponseEntity<Song>(HttpStatus.UNAUTHORIZED);

        }
        System.out.println("cunk");
        return new ResponseEntity<Song>(HttpStatus.UNAUTHORIZED);

    }


//fix  when trying to post without title
    @PostMapping(path = "/songs", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createSong(@RequestBody Song song, @RequestHeader(value = "Authorization") String authHeader) {

        System.out.println("RECEIVED: " + song.toString());
        try {
            if (us.getByToken(authHeader).getToken().equals(authHeader)) {
        try {
            if (song.getTitle().equals(null)) {
                throw new NullPointerException();
            }
            Song savedSong = songRepository.save(song);
            String path = "Location: /songsWS-gabs-KBE/rest/songs?songId=" + song.getId();
            HttpHeaders headers = new HttpHeaders();
            headers.set("location", path);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
            } else {
                throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
            }
        } catch (NullPointerException e) {

        }
        throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
    }

    @PutMapping(path = "/songs/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Song> updateSong(@PathVariable(value = "id") Long id,
                                           @RequestBody Song songToPut, @RequestHeader(value = "Authorization") String authHeader) {
        try {
            if (us.getByToken(authHeader).getToken().equals(authHeader)) {
                Song song = songRepository.findById(id)
                        .orElseThrow(() -> new RessourceNotFoundException("Note", "id", id));
          //   song.setId(songToPut.getId());

          song.setTitle(songToPut.getTitle());
          song.setArtist(songToPut.getArtist());
          song.setLabel(songToPut.getLabel());
          song.setReleased(songToPut.getReleased());
          songRepository.save(song);
          return ResponseEntity.ok(song);
      } else {
                throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
            }
        } catch (NullPointerException e) {
            System.out.println("pryc");
        }
        throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
    }




    @DeleteMapping(path = "/songs/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable(value = "id") long id,  @RequestHeader(value = "Authorization") String authHeader) {
        try {
            if (us.getByToken(authHeader).getToken().equals(authHeader)) {

                Song song = songRepository.findById(id)
                        .orElseThrow(() -> new RessourceNotFoundException("Song", "id", id));
                songRepository.delete(song);
                return ResponseEntity.noContent().build();
            } else {
                throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
            }
        } catch (NullPointerException e) {

        }
        throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
    }

}



