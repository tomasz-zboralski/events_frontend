package com.example.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    @JsonProperty(value = "userId")
    private Long userId;
    @JsonProperty(value = "name")
    private String name;
    private int eventsParticipation = 10;
}
