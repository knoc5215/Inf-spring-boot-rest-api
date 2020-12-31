package me.jumen.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jumen.demoinflearnrestapi.common.RestDocsConfiguration;
import me.jumen.demoinflearnrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)    // bean import
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
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
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.DRAFT)))

                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("location").description("location of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of new event"),
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("free").description("it tells is this event if free or not"),
                                fieldWithPath("offline").description("it tells is this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event status"),

                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events"),
                                fieldWithPath("_links.update-event.href").description("link to update an existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )

                ))
        ;

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스")
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
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("입력 값이 잘못된 에러가 발생하는 테스트")
    void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API dev with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 12, 26, 12, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 12, 25, 12, 12))
                .beginEventDateTime(LocalDateTime.of(2020, 12, 28, 12, 12))
                .endEventDateTime(LocalDateTime.of(2020, 12, 27, 12, 12))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(1000)
                .location("D2 Factory")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
        ;

    }

    @ParameterizedTest
    @MethodSource(value = "paramsForTestFree")
    void testFree(int basePrice, int maxPrice, boolean isFree) {
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        event.update();

        assertThat(event.isFree()).isEqualTo(isFree);

    }

    private static Object[] paramsForTestFree() {
        return new Object[]{
                new Object[]{0, 0, true},
                new Object[]{100, 0, false},
                new Object[]{0, 100, false},
                new Object[]{100, 100, false}
        };
    }

    @ParameterizedTest
    @MethodSource(value = "paramsForTestOffline")
    void testOffline(String location, boolean isOffline) {
        Event event = Event.builder()
                .location(location)
                .build();
        event.update();

        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private static Object[] paramsForTestOffline() {
        return new Object[]{
                new Object[]{"강남", true},
                new Object[]{null, false},
                new Object[]{"   ", false},
        };
    }


}
