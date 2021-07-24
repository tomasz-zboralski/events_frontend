package com.example.application.client;

import com.example.application.domain.EventDto;
import com.example.application.domain.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventsClient {

    private final RestTemplate restTemplate;

    private final static String BACKEND = "https://eventssss.herokuapp.com/v1/events";

    private URI getUri() {
        return UriComponentsBuilder.fromHttpUrl(BACKEND)
                .build().encode().toUri();
    }

    public void create(EventDto eventDto) {
        URI url = getUri();

        restTemplate.postForObject(url, eventDto, EventDto.class);
    }

    public void update(EventDto eventDto) {
        URI url = getUri();

        restTemplate.put(url, eventDto);
    }

    public void remove(long id) {

        URI url = UriComponentsBuilder.fromHttpUrl(BACKEND + "/" + id)
                .build().encode().toUri();

        restTemplate.delete(url);
    }

    public Set<EventDto> findAll() {
        URI url = getUri();

        return getEventsDto(url);
    }

    public void addParticipant(EventDto eventDto, UserDto userDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(BACKEND + "/" + eventDto.getEventId() + "/" + userDto.getUserId())
                .build().encode().toUri();


        restTemplate.put(url, eventDto);
    }

    private Set<EventDto> getEventsDto(URI url) {
        try {
            EventDto[] response = restTemplate.getForObject(url, EventDto[].class);

            return Optional.ofNullable(response)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(e -> Objects.nonNull(e.getEventId()) && Objects.nonNull(e.getName()))
                    .collect(Collectors.toSet());
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return Collections.emptySet();
        }
    }

}
