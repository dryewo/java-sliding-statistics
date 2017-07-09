package me.dryewo.sliding_statistics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void returns204OnOldTransactions() throws Exception {
        mockMvc
                .perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timestamp\": 0, \"amount\": 100}"))
                .andExpect(status().is(204));
        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().json("{count: 0, min: null, max: null, avg: 0, sum: 0}"));
    }

    @Test
    public void acceptsTransactions() throws Exception {
        long now = System.currentTimeMillis();
        mockMvc
                .perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timestamp\": " + now + ", \"amount\": 100}"))
                .andExpect(status().is(201));
        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().json("{count: 1, min: 100, max: 100, avg: 100, sum: 100}"));
    }

}
