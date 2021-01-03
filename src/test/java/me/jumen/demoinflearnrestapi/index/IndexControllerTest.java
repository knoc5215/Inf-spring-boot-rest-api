package me.jumen.demoinflearnrestapi.index;

import me.jumen.demoinflearnrestapi.common.BaseTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class IndexControllerTest extends BaseTest {

    @Test
    void index() throws Exception {
        mockMvc.perform(get("/api/"))
                .andExpect((status().isOk()))
                .andDo(print())
                .andExpect(jsonPath("_links.events").exists())
        ;
    }
}
