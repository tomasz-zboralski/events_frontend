package com.example.application.views.events;

import com.example.application.domain.EventDto;
import com.example.application.service.EventService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventForm extends FormLayout {

    private Binder<EventDto> binder = new Binder<EventDto>(EventDto.class);
    private EventsView eventsView;
    //@Autowired
    private EventService eventService;

    private TextField name = new TextField("Name");
    TextArea description = new TextArea("Description");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    public EventForm(EventsView eventsView, EventService eventService) {
        this.eventsView = eventsView;
        this.eventService = eventService;
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        description.getStyle().set("maxHeight", "150px");
        description.setPlaceholder("Write here ...");
        add(name, description, buttons);

        binder.bindInstanceFields(this);

        save.addClickListener(event -> save());
        delete.addClickListener(event -> delete());
    }

    private void save() {
        EventDto eventDto = binder.getBean();
        eventService.addEvent(eventDto);
        eventsView.refresh();
        setEvent(null);
    }

    private void delete() {
        EventDto eventDto = binder.getBean();
        eventService.deleteEvent(eventDto.getEventId());
        eventsView.refresh();
        setEvent(null);
    }

    public void setEvent(EventDto eventDto) {
        binder.setBean(eventDto);

        if (eventDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            description.focus();
        }
    }

}
