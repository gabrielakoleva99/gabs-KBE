package htwb.ai;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    // http://localhost:8080/songsservlet/songs


    @Override
    public void init(ServletConfig servletConfig)
            throws ServletException {

        this.uriToDB = servletConfig
                .getInitParameter("uriToDB");
        super.init(servletConfig);
     emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        songManager = new SongManager(emf);
        System.out.println("Server initialised");

    }

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
//    }
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try {
            if (request.getContentType() != null) {
                if (request.getContentType().equals("application/json")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Song song = objectMapper.readValue(request.getReader(), Song.class);
                    if (song.getTitle()==null||song.getArtist()==null||song.getLabel()==null||song.getReleased()==0) {
                        throw new IllegalArgumentException();
                    }
                    Integer id = songManager.saveSong(song);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.getOutputStream().print("Location: /songsservlet-gabs-KBE/songs?songId=" + id);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (PersistenceException | JsonParseException | MismatchedInputException | IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest reqest, HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

    }


    @Override
    public void destroy() {
        if (emf != null) {
            super.destroy();
            emf.close();
        }
    }
}
