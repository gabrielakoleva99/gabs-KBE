//package htwb.ai;
//
//
//import htwb.ai.controller.UserController;
//import htwb.ai.repo.UserRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.List;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@TestPropertySource(locations = "/test.properties")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class UserControllerTest {
//    private static MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository uRepo;
//
//    @BeforeEach
//    public void setupMockMvc() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(uRepo)).build();
//    }
//
//
//    @Test
//    @Order(1)
//    public void postUserShouldBeSuccessfullAndReturnStatus200() throws Exception {
//        String exampleJson = "{\"userId\":\"maxime\",\"password\":\"pass1234\"}";
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(exampleJson))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @Order(2)
//    public void postWithSecondUserShouldSucceed() throws Exception {
//        String exampleJson = "{\"userId\":\"jane\",\"password\":\"pass1234\"}";
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(exampleJson))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @Order(3)
//    public void postWithWrongUsernameShouldReturn401() throws Exception{
//        String exampleJson = "{\"userId\":\"falsch\",\"password\":\"pass1234\"}";
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
//                        .contentType(MediaType.APPLICATION_JSON).content(exampleJson))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    @Order(4)
//    public void postWithWrongPasswordShouldReturn401() throws Exception{
//        String exampleJson = "{\"userId\":\"jane\",\"password\":\"falsch\"}";
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
//                        .contentType(MediaType.APPLICATION_JSON).content(exampleJson))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    @Order(5)
//    public void postWithEmptyPasswordShouldReturn401() throws Exception{
//        String exampleJson = "{\"userId\":\"jane\"}";
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
//                        .contentType(MediaType.APPLICATION_JSON).content(exampleJson))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    @Order(6)
//    public void postWithEmptyUsernameShouldReturn401() throws Exception{
//        String exampleJson = "{\"password\":\"pass1234\"}";
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-gabs-KBE/rest/auth")
//                        .contentType(MediaType.APPLICATION_JSON).content(exampleJson))
//                .andExpect(status().isUnauthorized());
//    }
//}
