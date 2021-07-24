package com.example.application.service;

import com.example.application.client.EventsClient;
import com.example.application.client.UserClient;
import com.example.application.domain.EventDto;
import com.example.application.domain.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserClient userClient;

    public Set<UserDto> getUsers() {
        return userClient.findAll();
    }

    public UserDto getUserById(Long id) {
        return userClient.getUserById(id);
    }

    public void addUser(UserDto userDto) {
        userClient.create(userDto);
    }

    public void deleteUser(Long eventId) {
        userClient.remove(eventId);
    }
}
