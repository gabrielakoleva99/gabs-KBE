package htwb.ai.controller;

import htwb.ai.exception.UnsuccessfulAuthorizationException;
import htwb.ai.modell.User;
import htwb.ai.repo.SongRepository;
import htwb.ai.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping(value = "/songsWS-gabs-KBE/rest/auth")
public class UserController {

    private final UserRepository userRepository;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public UserController(UserRepository repo) {
        this.userRepository = repo;
    }
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authorize(@RequestBody User requestUser) {
        String userId = requestUser.getUserId();
        String password = requestUser.getPassword();

        User user = userRepository.findByUserId(requestUser.getUserId());
        System.out.println("userId: " + userId + ", password: " + password + ", user: " + user);
        if (user==null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (user.getUserId().equals(userId) && user.getPassword().equals(password)) {
            byte[] randomBytes = new byte[24];
            secureRandom.nextBytes(randomBytes);
            String response = base64Encoder.encodeToString(randomBytes);
            user.setToken(response);
            userRepository.save(user);
            System.out.println("U.GetTOKEN   " + user.getToken());

            System.out.println("USER  : " + user.getUserId() + "TOKEN --> " + user.getToken());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    public User getByToken(String token){
        if(token.equals(null)){
            throw new UnsuccessfulAuthorizationException("User", "token", token);
        }try {
            User u = userRepository.findByToken(token);
            return u;
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        throw new UnsuccessfulAuthorizationException("User", "token", token);
    }
}
