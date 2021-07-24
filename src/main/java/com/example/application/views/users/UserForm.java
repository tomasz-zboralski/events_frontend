package com.example.application.views.users;

import com.example.application.domain.EventDto;
import com.example.application.domain.UserDto;
import com.example.application.service.EventService;
import com.example.application.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

//@Component
@SpringComponent
@UIScope
public class UserForm extends FormLayout {
    private Binder<UserDto> binder = new Binder<UserDto>(UserDto.class);
    private UsersView usersView;
    private UserService userService;
    private EventService eventService;
    EventDto eventDto;

    private TextField name = new TextField("Name");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private ComboBox<EventDto> events = new ComboBox<>();
    Button addButton = new Button("Add User To Event", event -> addToEvent());

    public UserForm(UsersView usersView, UserService userService, EventService eventService) {
        this.usersView = usersView;
        this.userService = userService;
        this.eventService = eventService;
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        events.setItems(eventService.getEvents());
        events.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                eventDto = null;
            } else {
                eventDto = event.getValue();
            }
        });

        HorizontalLayout eventsLayout = new HorizontalLayout(events, addButton);

        name.setWidthFull();

        VerticalLayout content = new VerticalLayout(name, eventsLayout, buttons);
        add(content);

        binder.bindInstanceFields(this);

        save.addClickListener(event -> save());
        delete.addClickListener(event -> delete());
    }

    private void save() {
        UserDto userDto = binder.getBean();
        userService.addUser(userDto);
        usersView.refresh();
        setUser(null);
    }

    private void delete() {
        UserDto userDto = binder.getBean();
        userService.deleteUser(userDto.getUserId());
        usersView.refresh();
        setUser(null);
    }

    public void setUser(UserDto userDto) {
        binder.setBean(userDto);

        if (userDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            name.focus();
        }
    }

    private void addToEvent() {
        if (eventDto != null && binder.getBean() != null) {
            eventService.addUserToEvent(eventDto, binder.getBean());
            usersView.refresh();
        } else {
            events.setValue(null);
        }
    }
}
