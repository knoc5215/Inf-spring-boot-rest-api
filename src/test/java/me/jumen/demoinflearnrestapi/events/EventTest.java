package me.jumen.demoinflearnrestapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API dev with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    void javaBean() {
        String name = "Event";
        String description = "Spring";

        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}