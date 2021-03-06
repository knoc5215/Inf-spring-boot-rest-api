package me.jumen.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.jumen.demoinflearnrestapi.accounts.Account;
import me.jumen.demoinflearnrestapi.accounts.AccountSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Event {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;
    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class) // Event를 serialize 하는 경우, manager field에 대해서는 AccountSerializer를 using
    private Account manager;

    public void update() {
        this.free = this.basePrice == 0 && this.maxPrice == 0;
        this.offline = this.location != null && !this.location.isBlank();
    }
}
