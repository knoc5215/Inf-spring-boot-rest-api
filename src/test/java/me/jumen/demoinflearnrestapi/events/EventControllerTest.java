package me.jumen.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API dev with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 12, 25, 12, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 12, 26, 12, 12))
                .beginEventDateTime(LocalDateTime.of(2020, 12, 27, 12, 12))
                .endEventDateTime(LocalDateTime.of(2020, 12, 28, 12, 12))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(1000)
                .location("D2 Factory")
                .build();

//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.DRAFT)))
        ;
    }

    @Test
    void createEvent_Bad_Reqeust() throws Exception {
        Event event = Event.builder()

                .name("Spring")
                .description("REST API dev with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 12, 25, 12, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 12, 26, 12, 12))
                .beginEventDateTime(LocalDateTime.of(2020, 12, 27, 12, 12))
                .endEventDateTime(LocalDateTime.of(2020, 12, 28, 12, 12))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(1000)
                .location("D2 Factory")
                /* EventDto에 없는 field를 넘겨줄때 ? 400 bad_request 가 더 엄격한 처리 */
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .id(100)
                .build();


        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())

        ;
    }

    @Test
    void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andExpect(status().isBadRequest());

    }
}
