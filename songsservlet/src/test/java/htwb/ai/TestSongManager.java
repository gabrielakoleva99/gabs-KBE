package htwb.ai;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSongManager {

    private SongsServlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() throws ServletException {
        servlet = new SongsServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        MockServletConfig config = new MockServletConfig();
        servlet.init(config);
    }

//    @Test
//    public void getShouldSucceed() throws IOException {
//        request.addParameter("songId", "2");
//        servlet.doGet(request, response);
//
//        String expectedResult = "{\"id\":2,\"title\":\"Afternoon Delight\",\"artist\":\"Starland Vocal Band\",\"label\":\"Windsong\",\"released\":1976}";
//
//        assertEquals(expectedResult, response.getContentAsString());
//        assertEquals(200, response.getStatus());
//
//
//        request.addParameter("songId", "2");
//        servlet.doGet(request, response);
//
//
//    }

    @Test
    public void getMalformedValueShouldFail() throws IOException {
        request.addParameter("songId", "L");
        servlet.doGet(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void getMalformedParamShouldFail() throws IOException {
        request.addParameter("songID", "3");
        servlet.doGet(request, response);
        assertEquals(400, response.getStatus());
    }

//    @Test
//    public void getUnavilableResourceShouldFail() throws IOException {
//        request.addParameter("songId", "300");
//        servlet.doGet(request, response);
//        assertEquals(404, response.getStatus());
//    }

    @Test
    public void getWithoutParameters() throws IOException{
        request.addParameter("", "");
        servlet.doGet(request,response);
        assertEquals(400,response.getStatus());
    }
//
//    @Test
//    public void doPostSuccessfulCheckStatus() throws IOException{
//        request.setContentType("application/json");
//        request.setContent(("{\"title\": \"Wrecking Ball\",\n" +
//                "\t\t\"artist\": \"MILEY CYRUS\",\n" +
//                "\t\t\"label\": \"RCA\",\n" +
//                "\t\t\"released\": 2013}").getBytes());
//        servlet.doPost(request, response);
//        String expected = "Location: /songsservlet-gabs-KBE/songs?songId=11";
//        assertEquals(201,response.getStatus());
//        assertEquals(expected, response.getContentAsString());
//
//
//    }

//    @Test
//    public void doPostSuccessfullShowLocation() throws IOException{
//        request.setContentType("application/json");
//        request.setContent(("{\"title\": \"Wrecking Ball\",\n" +
//                "\t\t\"artist\": \"MILEY CYRUS\",\n" +
//                "\t\t\"label\": \"RCA\",\n" +
//                "\t\t\"released\": 2013}").getBytes());
//        servlet.doPost(request, response);
//        String expected = "Location: /songsservlet-gabs-KBE/songs?songId=11";
//        assertEquals(expected, response.getContentAsString());
//    }

    @Test
    public void doPostMissingParam() throws IOException{
        request.setContentType("application/json");
        request.setContent(("{\"title\": \"Wrecking Ball\",\n" +
                "\t\t\"label\": \"RCA\",\n" +
                "\t\t\"released\": 2013}").getBytes());
        servlet.doPost(request, response);
        assertEquals(400,response.getStatus());
    }

    @Test
    public void doPostWrongParam() throws IOException{
        request.setContentType("application/json");
        request.setContent(("blahblag").getBytes());
        servlet.doPost(request,response);
        assertEquals(400,response.getStatus());
    }

    @Test
    public void doPostNotJson() throws IOException{
        request.setContentType("text/plain");
        request.setContent(("blahblag").getBytes());
        servlet.doPost(request,response);
        assertEquals(415,response.getStatus());
    }

    @Test
    public void doPostNoContentType() throws IOException{
        request.setContentType("");
        request.setContent(("blahblag").getBytes());
        servlet.doPost(request,response);
        assertEquals(415,response.getStatus());
    }

    @Test
    public void doPostEmptyParam() throws IOException{
        request.setContentType("application/json");
        request.setContent(("").getBytes());
        servlet.doPost(request,response);
        assertEquals(400,response.getStatus());
    }

    @Test
    public void doPostMalformedJson() throws IOException{
        request.setContentType("application/json");
        request.setContent(("blub").getBytes());
        servlet.doPost(request,response);
        assertEquals(400,response.getStatus());
    }

    @Test
    public void doDeleteCheckStatus() throws IOException{
        servlet.doDelete(request, response);
        assertEquals(405, response.getStatus());
    }

    @Test
    public void doPutCheckStatus() throws IOException{
        servlet.doPut(request, response);
        assertEquals(405, response.getStatus());
    }
}
