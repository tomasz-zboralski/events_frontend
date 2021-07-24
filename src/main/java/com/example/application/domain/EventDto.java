package com.example.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDto {
    @JsonProperty(value = "eventId")
    private Long eventId;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "description")
    private String description;
    @JsonProperty(value = "users")
    private Set<UserDto> users;

    @Override
    public String toString() {
        return name;
    }
}
