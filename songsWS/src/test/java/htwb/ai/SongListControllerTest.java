package htwb.ai;

import htwb.ai.controller.SongListController;
import htwb.ai.controller.UserController;
import htwb.ai.modell.SongList;
import htwb.ai.modell.User;
import htwb.ai.repo.SongListRepository;
import htwb.ai.repo.SongRepository;
import htwb.ai.repo.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SongListControllerTest {
    private static MockMvc mockMvc;
    private static MockMvc mockMvc2;


    @Autowired
    private SongListRepository songListRepository;
    @Autowired
    private SongRepository songRepo;
    @Autowired
    private UserRepository userRepo;

    MvcResult result;

    @BeforeEach
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userRepo)).build();
        mockMvc2 = MockMvcBuilders.standaloneSetup(new SongListController(songListRepository, userRepo, songRepo)).build();
    }

    @Test
    @Order(1)
    void getWrongWithNonExistingId() throws Exception {

        String exampleJson = "{\"userId\":\"jane\",\"password\":\"pass1234\"}";
        String token;

        result =   mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(exampleJson))
                .andExpect(status().isOk())
                .andReturn();

        token = result.getResponse().getContentAsString();
        System.out.println(token);

        mockMvc2.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/100" )
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", token))
                .andExpect(status().isForbidden());
    }


    @Test
    @Order(2)
        public void testGetSongList_UnsuccessfulAuthorization_ReturnsUnauthorized() throws Exception {

        String exampleJson = "{\"userId\":\"maxime\",\"password\":\"pass1234\"}";
        String token = "blob";

        result =   mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(exampleJson))
                .andExpect(status().isOk())
                .andReturn();


        mockMvc2.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/3" )
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }


        @Test
    @Order(3)
    public void testGetSongListWorksWithSuccessfulAuthorization()throws Exception{

            String exampleJson = "{\"userId\":\"jane\",\"password\":\"pass1234\"}";
            String token;

            result =   mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(exampleJson))
                    .andExpect(status().isOk())
                    .andReturn();

          token = result.getResponse().getContentAsString();
            System.out.println(token);

          mockMvc2.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/1" )
                          .param("userId", "maxime")
                          .param("password", "pass1234")
                          .header("Authorization", token))
                  .andExpect(status().isOk());
        }


    @Test
    @Order(4)
    public void testGetSongListSearchPrivateSongListShouldFail()throws Exception{

        String exampleJson = "{\"userId\":\"jane\",\"password\":\"pass1234\"}";
        String token;

        result =   mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(exampleJson))
                .andExpect(status().isOk())
                .andReturn();

        token = result.getResponse().getContentAsString();
        System.out.println(token);

        mockMvc2.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists/2" )
                        .param("userId", "maxime")
                        .param("password", "pass1234")
                        .header("Authorization", token))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    void getALlSongListsVonMaximeShouldBeSuccessfull() throws Exception{
        String exampleJson = "{\"userId\":\"maxime\",\"password\":\"pass1234\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(exampleJson))
                .andReturn();

        String str = result.getResponse().getContentAsString();

        mockMvc2.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists")
                        .param("userId", "jane")
                        .param("password", "pass1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", str))
                .andExpect(status().isOk());


    }

    @Test
    @Order(6)
    void getALlSongListsNonExististingUser() throws Exception{
        String exampleJson = "{\"userId\":\"maxime\",\"password\":\"pass1234\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(exampleJson))
                .andReturn();

        String str = result.getResponse().getContentAsString();

        mockMvc2.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songLists")
                        .param("userId", "blob")
                        .param("password", "pass1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", str))
                .andExpect(status().isNotFound());


    }

    }
