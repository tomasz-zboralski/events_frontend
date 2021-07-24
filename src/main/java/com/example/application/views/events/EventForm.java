package com.example.application.views.events;

import com.example.application.domain.EventDto;
import com.example.application.domain.UserDto;
import com.example.application.service.EventService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

//@Component
@SpringComponent
@UIScope
public class EventForm extends Div {

    private Binder<EventDto> binder = new Binder<EventDto>(EventDto.class);
    private EventsView eventsView;
    private EventService eventService;
    private Grid<UserDto> users = new Grid<>(UserDto.class);

    private TextField name = new TextField("Name");
    TextArea description = new TextArea("Description");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    public EventForm(EventsView eventsView, EventService eventService) {

        this.eventService = eventService;
        VerticalLayout content = new VerticalLayout(name, description);
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        name.setWidthFull();
        description.setWidthFull();
        description.setHeight("200px");
        description.setPlaceholder("Write here ...");

        users.setColumns("userId", "name");
        users.addComponentColumn(item -> createRemoveButton(users, item))
                .setHeader("Actions");

        users.setHeight("30%");



        add(content, users, buttons);

        binder.bindInstanceFields(this);
        this.eventsView = eventsView;

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
            name.focus();
            users.setItems(eventDto.getUsers());
        }
    }

    private Button createRemoveButton(Grid<UserDto> grid, UserDto item) {
        @SuppressWarnings("unchecked")
        Button button = new Button("Remove", clickEvent -> {
            ListDataProvider<UserDto> dataProvider = (ListDataProvider<UserDto>) grid
                    .getDataProvider();
            dataProvider.getItems().remove(item);
            eventService.addEvent(binder.getBean());
            dataProvider.refreshAll();
            eventsView.refresh();

        });
        return button;
    }

}
