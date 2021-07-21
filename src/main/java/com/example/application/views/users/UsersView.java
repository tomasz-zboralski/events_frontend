package com.example.application.views.users;

import java.util.Arrays;
import java.util.List;

import com.example.application.domain.UserDto;
import com.example.application.service.UserService;
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

@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
public class UsersView extends Div implements AfterNavigationObserver {

    private final UserService userService;
    Grid<UserDto> grid = new Grid<>();

    public UsersView(UserService userService) {
        this.userService = userService;
        addClassName("users-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(user -> createCard(user));
        add(grid);
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

        Icon likeIcon = VaadinIcon.HEART.create();
        likeIcon.addClassName("icon");
        Span eventsNumber = new Span(String.valueOf(userDto.getEventsParticipation()));
        eventsNumber.addClassName("events");

        actions.add(likeIcon, eventsNumber);

        description.add(header, actions);
        card.add(image, description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

//        List<UserDto> users = Arrays.asList( //
//                createUser(1L, "John Smith", 2),
//                createUser(2L, "Mark Morgan", 4)
//
//        );

        grid.setItems(userService.getUsers());
    }

//    private static UserDto createUser(Long id, String name, int events) {
//        UserDto user = new UserDto(id, name, events);
//
//        return user;
//    }

}
