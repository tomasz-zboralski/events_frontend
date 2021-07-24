package com.example.application.views.users;

import com.example.application.domain.EventDto;
import com.example.application.domain.UserDto;
import com.example.application.service.EventService;
import com.example.application.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.MainLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

//@Component
@SpringComponent
@UIScope
@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
public class UsersView extends Div implements AfterNavigationObserver {

    private final UserService userService;
    private final EventService eventService;
    private final UserForm form;

    Grid<UserDto> grid = new Grid<>();
    private Button addNewUser = new Button("Add new user");

    public UsersView(UserService userService, EventService eventService) {

        this.userService = userService;
        this.eventService = eventService;
        form = new UserForm(this, userService, eventService);


        addClassName("users-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);
        HorizontalLayout toolbar = new HorizontalLayout(addNewUser);
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        add(toolbar, mainContent);

        form.setUser(null);
        //grid.asSingleSelect().addValueChangeListener(event -> form.setUser(grid.asSingleSelect().getValue()));
        grid.addItemClickListener(event -> form.setUser(grid.asSingleSelect().getValue()));

        addNewUser.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setUser(new UserDto());
        });
    }

    private HorizontalLayout createCard(UserDto userDto) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Icon image = VaadinIcon.USER.create();
        image.setSize("100px");
        image.addClassName("image");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(userDto.getName());
        name.addClassName("name");
        header.add(name);

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon likeIcon = VaadinIcon.EYE.create();
        likeIcon.addClassName("icon");
        Span eventsNumber = new Span(String.valueOf(userDto.getParticipation()));
        eventsNumber.addClassName("events");

        actions.add(likeIcon, eventsNumber);

        description.add(header, actions);
        card.add(image, description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        grid.setItems(userService.getUsers());
    }

    public void refresh() {
        grid.setItems(userService.getUsers());
    }


}
