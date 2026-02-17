package com.example.moviebooking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReferenceDataController.class)
class ReferenceDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCitiesReturnsTopIndianCities() throws Exception {
        mockMvc.perform(get("/api/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Mumbai"))
                .andExpect(jsonPath("$[1]").value("Delhi"))
                .andExpect(jsonPath("$[2]").value("Bengaluru"))
                .andExpect(jsonPath("$[3]").value("Hyderabad"))
                .andExpect(jsonPath("$[4]").value("Chennai"))
                .andExpect(jsonPath("$[5]").value("Kolkata"));
    }
}
