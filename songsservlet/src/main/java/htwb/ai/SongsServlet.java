package htwb.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.ObjectNotFoundException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class SongsServlet extends HttpServlet {

    private static final String PERSISTENCE_UNIT_NAME = "songsDB";
    private static SongManager songManager;
    private static EntityManagerFactory emf;

    private String uriToDB = null;

    @Override
    public void init(ServletConfig servletConfig)
            throws ServletException {
        // Beispiel: Laden eines Initparameters
        // aus der web.xml
        this.uriToDB = servletConfig
                .getInitParameter("uriToDB");
        super.init(servletConfig);
     emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        songManager = new SongManager(emf);
        System.out.println("Server initialised");
    }


    // http://localhost:8080/songsservlet/song

//    @Override
//    public void init(ServletConfig servletConfig)
//            throws ServletException {
//        super.init(servletConfig);
//        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//        songManager = new SongManager(emf);
//    }

//    @Override
//    public void init(ServletConfig servletConfig) {
//        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//        songManager = new SongManager(emf);
//        try {
//            initSongs();
//        } catch (IOException e) {
//            System.out.println("Songs couldn't be loaded");
//        }
//    }
//
//    private void initSongs() throws IOException {
//        ObjectMapper objektMapper = new ObjectMapper();
//        try (Reader reader = new InputStreamReader(Objects.requireNonNull(
//                SongsServlet.class.getResourceAsStream("/songs.json")))) {
//            songManager.saveSongList(objektMapper.readValue(reader, new TypeReference<List<Song>>() {
//            }));
//        }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Enumeration<String> paramNames = request.getParameterNames();
        String param = paramNames.nextElement();
        response.setContentType("application/json");
        try {
            if (!paramNames.hasMoreElements() && param != null) {
                if (param.equals("all")) {
                    List<Song> songs = songManager.findAllSongs();
                    System.out.println(songs);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(songs);
                    response.getWriter().write(json);
                } else if (param.equals("songId")) {
                    Song song = songManager.findSong(request.getParameter(param));
                    System.out.println(song);
                    if (song == null) throw new ClassNotFoundException("No such song");
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(song);
                    response.getWriter().write(json);
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }


//    @Override
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Enumeration<String> paramNames = request.getParameterNames();
//        String param = paramNames.nextElement();
//        response.setContentType("application/json");
//        try {
//            if (!paramNames.hasMoreElements() && param != null) {
//                if (param.equals("all")) {
//                    List<Song> songs = songManager.findAllSongs();
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    objectMapper.writeValue(response.getOutputStream(), songs);
//                } else if (param.equals("songId")) {
//                    Song song = songManager.findSong(request.getParameter(param));
//                    if (song == null) throw new ClassNotFoundException("No such song");
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    objectMapper.writeValue(response.getOutputStream(), song);
//                } else {
//                    throw new IllegalArgumentException();
//                }
//            } else {
//                throw new IllegalArgumentException();
//            }
//        } catch (IllegalArgumentException e) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        } catch (ClassNotFoundException e) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//        }
//    }

    @Override
    public void destroy() {
        if (emf != null) {
            super.destroy();
            emf.close();
        }
    }
}
