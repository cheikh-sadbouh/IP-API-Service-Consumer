package com.roche.interview.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IpLocationFinderControllerTest {

    private final static String url = "http://localhost:8888/api/v1/northern-countries";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Should_Return_Country_Names_When_Country_Is_From_Northern_Hemisphere() throws Exception {

        mockMvc.perform(get(url + "?ip=8.8.8.2&ip=8.8.8.8").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasItem("United States")));
    }

    @Test
    public void Should_Return_BadRequest_When_IpAddress_Is_Invalid() throws Exception {
        mockMvc.perform(get(url + "?ip=23.X.4.6"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("HAS_INVALID_IP_ADDRESS"));
    }


    @Test
    public void Should_Return_BadRequest_When_IpAddresses_Are_More_Than_Five() throws Exception {
        mockMvc.perform(get(url + "?ip=1.1.1.1&ip=2.2.2.2&ip=3.3.3.3&ip=4.4.4.4&ip=5.5.5.5&ip=3.3.3.3&ip=4.4.4.4&ip=5.5.5.5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("INPUT_SIZE_IS_GREATER_THAN_FIVE"));
    }

    @Test
    public void Should_Return_BadRequest_When_IpAddresses_Are_More_Less_Than_One() throws Exception {
        mockMvc.perform(get(url + "?ip="))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("INPUT_SIZE_IS_LESS_THAN_ONE"));

    }

    @Test
    public void Should_Return_BadRequest_When_No_Parameter_Is_Present() throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void Should_Return_No_Country_Found_When_Country_Is_Not_From_Northern_Hemisphere() throws Exception {
        mockMvc.perform(get(url + "?ip=119.18.2.212"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("No country found"));

    }


}
