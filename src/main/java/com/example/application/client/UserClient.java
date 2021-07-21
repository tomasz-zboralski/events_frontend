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
public class UserClient {
    private final RestTemplate restTemplate;

    private final static String BACKEND = "https://eventssss.herokuapp.com/v1/users";

    private URI getUri() {
        return UriComponentsBuilder.fromHttpUrl(BACKEND)
                .build().encode().toUri();
    }

    public void create(UserDto userDto) {
        URI url = getUri();

        restTemplate.postForObject(url, userDto, UserDto.class);
    }

    public void update(UserDto userDto) {
        URI url = getUri();

        restTemplate.put(url, userDto);
    }

    public void remove(long id) {

        URI url = UriComponentsBuilder.fromHttpUrl(BACKEND + "/" + id)
                .build().encode().toUri();

        restTemplate.delete(url);
    }

    public Set<UserDto> findAll() {
        URI url = getUri();

        return getUsersDto(url);
    }

    private Set<UserDto> getUsersDto(URI url) {
        try {
            UserDto[] response = restTemplate.getForObject(url, UserDto[].class);

            return Optional.ofNullable(response)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(e -> Objects.nonNull(e.getUserId()) && Objects.nonNull(e.getName()))
                    .collect(Collectors.toSet());
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return Collections.emptySet();
        }
    }
}
