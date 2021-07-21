package com.example.application.service;

import com.example.application.client.EventsClient;
import com.example.application.domain.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventsClient eventsClient;

    public Set<EventDto> getEvents() {
        return eventsClient.findAll();
    }

    public void addEvent(EventDto eventDto) {
        eventsClient.create(eventDto);
    }

    public void deleteEvent(Long eventId) {
        eventsClient.remove(eventId);
    }
}
