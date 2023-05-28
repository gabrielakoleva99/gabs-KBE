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

    @Test
    public void getShouldSucceed() throws IOException {
        request.addParameter("songId", "2");
        servlet.doGet(request, response);

        String expectedResult = "{\"id\":2,\"title\":\"Afternoon Delight\",\"artist\":\"Starland Vocal Band\",\"label\":\"Windsong\",\"released\":1976}";

        assertEquals(expectedResult, response.getContentAsString());
        assertEquals(200, response.getStatus());
    }

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

    @Test
    public void getUnavilableResourceShouldFail() throws IOException {
        request.addParameter("songId", "300");
        servlet.doGet(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    public void getWithoutParameters() throws IOException{
        request.addParameter("", "");
        servlet.doGet(request,response);
        assertEquals(400,response.getStatus());
    }
}
