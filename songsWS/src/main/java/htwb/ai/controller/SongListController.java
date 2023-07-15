package htwb.ai.controller;

import htwb.ai.exception.ForbiddenException;
import htwb.ai.exception.RessourceNotFoundException;
import htwb.ai.exception.UnsuccessfulAuthorizationException;
import htwb.ai.modell.Song;
import htwb.ai.modell.SongList;
import htwb.ai.modell.User;
import htwb.ai.repo.SongListRepository;
import htwb.ai.repo.SongRepository;
import htwb.ai.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/songsWS-gabs-KBE/rest/songLists")
public class SongListController {

    @Autowired
    private final SongListRepository songListRepo;

    public SongListController(SongListRepository repo, UserRepository urepo, SongRepository srepo) {
        this.songListRepo = repo;
        this.ur = urepo;
        this.songRepo = srepo;
        us = new UserController(ur);
        sc = new SongController(songRepo, ur);
    }

    UserRepository ur;
    UserController us;

    SongRepository songRepo;
    SongController sc;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, path = "/{id}")
    public Iterable<SongList> getSongList(@PathVariable(value = "id") int id, @RequestHeader(value = "Authorization") String authHeader) {
        try{
            if(us.getByToken(authHeader).getToken().equals(authHeader)){
                if(us.getByToken(authHeader).getUserId().equals("maxime")){
                    if(!songListRepo.findByIdAndOwnerId(id, "maxime").isEmpty()){
                        return songListRepo.findByIdAndOwnerId(id, "maxime");
                    }else if(!songListRepo.findByIsPrivateAndUserIdAndId(false, "jane", id).isEmpty()){
                        return songListRepo.findByIsPrivateAndUserIdAndId(false,"jane", id);
                    } else if(!songListRepo.findByIsPrivateAndUserIdAndId(true, "jane", id).isEmpty()) {
                        throw new ForbiddenException("SongList", "Song", id);
                    }
                }else if(us.getByToken(authHeader).getUserId().equals("jane")){
                    if(!songListRepo.findByIdAndOwnerId(id, "jane").isEmpty()){
                        return songListRepo.findByIdAndOwnerId(id, "jane");
                    }else if(!songListRepo.findByIsPrivateAndUserIdAndId(false, "maxime", id).isEmpty()){
                        return songListRepo.findByIsPrivateAndUserIdAndId(false, "maxime", id);
                    }else if(!songListRepo.findByIsPrivateAndUserIdAndId(true, "maxime", id).isEmpty()){
                        throw new ForbiddenException("SongList", "Song", id);

                    }
                }
            }
        }catch (NullPointerException e){
            throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
        } throw new ForbiddenException("SongList", "Song", id);
    }


    //http://localhost:8080/songsWS-gabs-KBE/rest/songLists?userId=maxime

    //GET /songsWS-gabs/rest/songLists?userId=maxime
    //Accept: application/json
    @RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE})
    public Iterable<SongList> getAllSongLists(@RequestParam(value = "userId") String userId, @RequestHeader(value = "Authorization") String authHeader) {
        try {
            if (us.getByToken(authHeader).getToken().equals(authHeader)) {
                if (us.getByToken(authHeader).getUserId().equals("maxime")) {
                    if (userId.equals("maxime")) {
                        return songListRepo.selectSongListByUser("maxime");
                    } else if (userId.equals("jane")) {
                        return songListRepo.findByIsPrivateAndUserId(false, "jane");
                    } else throw new RessourceNotFoundException("User", "userId", userId);
                } else if (us.getByToken(authHeader).getUserId().equals("jane")) {
                    if (userId.equals("jane")) {
                        return songListRepo.selectSongListByUser("jane");
                    } else if(userId.equals("maxime")){
                        return songListRepo.findByIsPrivateAndUserId(false, "maxime");
                    }
                } else throw new RessourceNotFoundException("User", "userId", userId);
            }
        }catch (NullPointerException e) {
            throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
        }
        throw new RessourceNotFoundException("User", "userId", userId);
    }


    //POST /songsWS-gabs-KBE/rest/songLists
    //Accept: application/json

    //http://localhost:8080/songsWS-gabs-KBE/rest/songLists
//    {
//        "name": "JanesPrivateNumeroDue",
//            "songs": [
//        {
//            "id": 10,
//                "title": "Chinese Food",
//                "artist": "Alison Gold",
//                "label": "PMW Live",
//                "released": 2013
//        },
//        {
//            "id": 9,
//                "title": "My Humps",
//                "artist": "Black Eyed Peas",
//                "label": "Universal Music",
//                "released": 2003
//        }
//	],
//        "private": true
//    }

    //opravi payloada
    @PostMapping
    public ResponseEntity<String> createSongList(@RequestBody SongList songList, @RequestHeader(value = "Authorization") String authHeader) {
        try{
            if (us.getByToken(authHeader).getToken().equals(authHeader)) {

                if (songList.getSongs() == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                for (Song s : songList.getSongs()) {
                    if (!songRepo.existsById((long) s.getId())) {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }

                User u = ur.findByToken(authHeader);

                songList.setOwnerId(u);

                songListRepo.save(songList);

                String songLocation = "Location: /songLists/" + songList.getId();
                return new ResponseEntity<>(songLocation, HttpStatus.OK);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //http://localhost:8080/songsWS-gabs/rest/songLists/5

    //DELETE /songsWS-gabs/rest/songLists/5
    //Accept: application/json
    //Authorization: 30bd4652d7c6181a59da0a2685bc1fa7912bc0d844368a8907209093f2f00775
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteSongList(@PathVariable(value = "id") int id, @RequestHeader(value = "Authorization") String authHeader) {
        try {
            if (us.getByToken(authHeader).getToken().equals(authHeader)) {
                if (us.getByToken(authHeader).getUserId().equals("maxime")) {
                    if(!songListRepo.findByIdAndOwnerId(id, "jane").isEmpty()) {
                        SongList sl = songListRepo.findSongListByIdAndUserId(id,"maxime");
                        songListRepo.delete(sl);
                        return ResponseEntity.noContent().build();
                    } else{
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }

                } else if(us.getByToken(authHeader).getUserId().equals("jane")){
                    if(!songListRepo.findByIdAndOwnerId(id, "jane").isEmpty()){
                        SongList sl = songListRepo.findSongListByIdAndUserId(id,"jane");
                        songListRepo.delete(sl);
                        return ResponseEntity.noContent().build();
                    }return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            }
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }
        throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
    }

}
