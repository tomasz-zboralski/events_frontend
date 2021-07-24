package com.example.application.views.events;


import com.example.application.domain.EventDto;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.MainLayout;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

//@Component
@SpringComponent
@UIScope
@PageTitle("Events")
@Route(value = "events", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class EventsView extends Div implements AfterNavigationObserver {

    private final EventService eventService;
    private final UserService userService;
    private final EventForm form;

    Grid<EventDto> grid = new Grid<>();
    private Button addNewEvent = new Button("Add new event");

    public EventsView(EventService eventService, UserService userService) {


        this.eventService = eventService;
        this.userService = userService;

        form = new EventForm(this, eventService);

        addClassName("events-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
//        grid.addComponentColumn(i -> createCard(i));

        grid.addComponentColumn(this::createCard);
        HorizontalLayout toolbar = new HorizontalLayout(addNewEvent);
        //form.setHeight("60%");
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        form.setWidth("40%");
        mainContent.setSizeFull();
        add(toolbar, mainContent);
        form.setEvent(null);
        //grid.asSingleSelect().addValueChangeListener(event -> form.setEvent(grid.asSingleSelect().getValue()));
        grid.addItemClickListener(event -> form.setEvent(grid.asSingleSelect().getValue()));





        addNewEvent.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setEvent(new EventDto());
        });

    }

    private HorizontalLayout createCard(EventDto eventDto) {
//        Button addNewParticipant = new Button("Add new participant");

        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Icon image = VaadinIcon.CHECK_CIRCLE_O.create();
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

        Span name = new Span(eventDto.getName());
        name.addClassName("name");
        name.getElement().getStyle().set("font-size", "25px");
        header.add(name);

        Span eventDescription = new Span(eventDto.getDescription());
        eventDescription.addClassName("eventdescription");
        eventDescription.setHeight("150px");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon likeIcon = VaadinIcon.USERS.create();
        likeIcon.addClassName("icon");
        Span eventsNumber = new Span(String.valueOf(eventDto.getUsers().size()));
        eventsNumber.addClassName("events");


        Button addMeButton = createAddMeButton(grid, eventDto);


//        addNewParticipant.addClickListener(e -> {
//            refresh();
//        });

        actions.add(likeIcon, eventsNumber, addMeButton);

        description.add(header, eventDescription, actions);
        card.add(image, description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        grid.setItems(eventService.getEvents());

    }

    public void refresh() {
        grid.setItems(eventService.getEvents());
    }

    private Button createAddMeButton(Grid<EventDto> grid, EventDto item) {
        @SuppressWarnings("unchecked")
        Button button = new Button("Add me as a participant", clickEvent -> {
            ListDataProvider<EventDto> dataProvider = (ListDataProvider<EventDto>) grid
                    .getDataProvider();
            dataProvider.getItems().stream().filter(i -> i.equals(item)).findFirst().get().getUsers().add(userService.getUserById(1L));
            //dataProvider.getItems().remove(item);
            eventService.addEvent(item);

            dataProvider.refreshAll();
            form.setEvent(item);
            refresh();
            //item.setParticipants(item.getParticipants() + 1);
        });
        return button;
    }


}
