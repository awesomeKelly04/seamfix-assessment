package com.example.demo;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {})
@WebMvcTest
class MainTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Valid BVN in request payload")
    void testWithValidBVN() throws Exception {
        JSONObject data = new JSONObject();
        String value = "11111111111";
        data.put("bvn", value);
        Thread.sleep(5000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/bv-service/svalidate/wrapper")
                        .content(String.valueOf(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONObject responseBody = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(responseBody);
        assertEquals("Success", responseBody.getString("message"));
        assertEquals("00", responseBody.getString("code"));
        assertEquals(value, responseBody.getString("bvn"));
        assertNotNull(responseBody.getString("ImageDetail"));
        assertNotEquals("", responseBody.getString("ImageDetail"));
        assertNotNull(responseBody.getString("BasicDetail"));
        assertNotEquals("", responseBody.getString("BasicDetail"));
    }

    @Test
    @DisplayName("Empty BVN in request payload")
    void testWithEmptyBVN() throws Exception {
        JSONObject data = new JSONObject();
        String value = "";
        data.put("bvn", value);
        Thread.sleep(1000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/bv-service/svalidate/wrapper")
                        .content(String.valueOf(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONObject responseBody = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(responseBody);
        assertEquals("One or more of your request parameters failed validation. Please retry", responseBody.getString("message"));
        assertEquals("400", responseBody.getString("code"));
    }

    @Test
    @DisplayName("Invalid BVN in request payload ")
    void testWithInvalidBVN() throws Exception {
        JSONObject data = new JSONObject();
        String value = "99999999999";
        data.put("bvn", value);
        Thread.sleep(1000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/bv-service/svalidate/wrapper")
                        .content(String.valueOf(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONObject responseBody = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(responseBody);
        assertEquals("The searched BVN does not exist", responseBody.getString("message"));
        assertEquals("01", responseBody.getString("code"));
        assertEquals(value, responseBody.getString("bvn"));
    }

    @Test
    @DisplayName("Invalid BVN (Less than 11 BVN digits) in request payload")
    void testWithBVNOfLessDigits() throws Exception {
        JSONObject data = new JSONObject();
        String value = "2222222222";
        data.put("bvn", value);
        Thread.sleep(1000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/bv-service/svalidate/wrapper")
                        .content(String.valueOf(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONObject responseBody = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(responseBody);
        assertEquals("TThe searched BVN is invalid", responseBody.getString("message"));
        assertEquals("02", responseBody.getString("code"));
        assertEquals(value, responseBody.getString("bvn"));
    }

    @Test
    @DisplayName("Invalid BVN (Contains non digits) in request payload")
    void testWithBVNOfNonDigits() throws Exception {
        JSONObject data = new JSONObject();
        String value = "22a222d222";
        data.put("bvn", value);
        Thread.sleep(1000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/bv-service/svalidate/wrapper")
                        .content(String.valueOf(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONObject responseBody = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(responseBody);
        assertEquals("TThe searched BVN is invalid", responseBody.getString("message"));
        assertEquals("400", responseBody.getString("code"));
        assertEquals(value, responseBody.getString("bvn"));
    }

    @Test
    @DisplayName("Valid BasicDetail base64 encoded string in response payload")
    void testWithValidBVNValidBasicDetail() throws Exception {
        JSONObject data = new JSONObject();
        String value = "11111111111";
        data.put("bvn", value);
        Thread.sleep(5000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/bv-service/svalidate/wrapper")
                        .content(String.valueOf(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONObject responseBody = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(responseBody);
        assertTrue(responseBody.getString("BasicDetail").matches("^(([\\w+/]{4}){19}\r\n)*(([\\w+/]{4})*([\\w+/]{4}|[\\w+/]{3}=|[\\w+/]{2}==))$"));
    }

    @Test
    @DisplayName("All the above")
    void testAllTheAbove() throws Exception {
    }
}