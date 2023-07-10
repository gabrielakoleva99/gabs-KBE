//package htwb.ai;
//
//
//
//import org.assertj.core.api.Assertions;
//
//
//import htwb.ai.controller.SongController;
//import htwb.ai.controller.UserController;
//import htwb.ai.repo.SongRepository;
//import htwb.ai.repo.UserRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.Assert;
//
//import java.sql.SQLOutput;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@TestPropertySource(locations = "/test.properties")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class SongControllerTest {
//
//    private static MockMvc mockMvc;
//
//    @Autowired
//    private SongRepository sRepo;
//
//    @BeforeEach
//    public void setupMockMvc() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new SongController(sRepo)).build();
//
//    }
//
//    @Test
//    @Order(1)
//    void getAllSongsJson() throws Exception {
//        String example = "[{\"id\":1,\"title\":\"MacArthur Park\",\"artist\":" +
//                "\"Richard Harris\",\"label\":\"Dunhill Records\",\"released\":1968},{\"id\":" +
//                "2,\"title\":\"Afternoon Delight\",\"artist\":\"Starland Vocal Band\",\"label\":\"" +
//                "Windsong\",\"released\":1976},{\"id\":3,\"title\":\"Muskrat Love\",\"artist\":\"Captain and Tennille\",\"label\":\"A&M\",\"released\"" +
//                ":1976}]";
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songs"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andReturn();
//
//
//        String str = result.getResponse().getContentAsString();
//        Assertions.assertThat(str).isEqualToIgnoringCase(example);
//
//    }
//
//    @Test
//    @Order(7)
//    void getWrongWithNonExistingId() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songs/100"))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    @Order(5)
//    void postWrongWithoutTitle() throws Exception{
//        String example = "{\"artist\":\"MILEY CYRUS\",\"label\":\"RCA\",\"released\":2013}    \n";
//        mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/songs").contentType(MediaType.APPLICATION_JSON)
//                        .content(example))
//                .andExpect(status().isBadRequest());
//
//    }
//    @Test
//    @Order(3)
//    void deleteWorks204() throws Exception{
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/songsWS-gabs-KBE/rest/songs/2"))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songs/2"))
//                .andExpect(status().isNotFound());
//
//    }
//    @Test
//    @Order(4)
//    void deleteWrongWithNonExistingId() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.delete("/songsWS-gabs-KBE/rest/songs/20"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @Order(6)
//    void getWorkingWith1SongJson() throws Exception {
//        sRepo.findAll().forEach( u -> System.out.println("EEEEEE: " + u.toString()));
//
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songs/1").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.title").value("MacArthur Park"))
//                .andExpect(jsonPath("$.artist").value("Richard Harris"))
//                .andExpect(jsonPath("$.label").value("Dunhill Records"))
//                .andExpect(jsonPath("$.released").value(1968));
//
//
//
//    }
//    @Test
//    @Order(223)
//    void postCorrectReturnUrl() throws Exception {
//        String example = "{\"title\":\"Wrecking Ball\",\"artist\":\"MILEY CYRUS\",\"label\":\"RCA\",\"released\":2013}    \n";
//        String expected = "Location: /songsWS-gabs-KBE/rest/songs?songId=4";
//        MvcResult result =  mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/songs").contentType(MediaType.APPLICATION_JSON)
//                        .content(example))
//                .andReturn();
//        Assertions.assertThat(result).isNotNull();
//        String str = result.getResponse().getHeader("location");
//        Assertions.assertThat(str).isEqualToIgnoringCase(expected);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songs/4")
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
//
//    }
//
//
//    @Test
//    @Order(8)
//    void putWrongWithNonExistingIdInURL() throws Exception{
//        String example = "{\"id\":\"1\",\"title\":\"Wrecking Ball\",\"artist\":\"MILEY CYRUS\",\"label\":\"RCA\",\"released\":2013}    \n";
//        mockMvc.perform(MockMvcRequestBuilders.put("/songsWS-gabs-KBE/rest/songs/50").contentType(MediaType.APPLICATION_JSON)
//                        .content(example))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    @Order(9)
//    void putWorks200() throws Exception{
//        String example = "{\"id\":\"1\",\"title\":\"Wrecking Ball\",\"artist\":\"MILEY CYRUS\",\"label\":\"RCA\",\"released\":2013}    \n";
//        mockMvc.perform(MockMvcRequestBuilders.put("/songsWS-gabs-KBE/rest/songs/1").contentType(MediaType.APPLICATION_JSON)
//                        .content(example))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/songsWS-gabs-KBE/rest/songs/1")
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
//
//    }
//
//}
