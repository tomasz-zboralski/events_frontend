package com.example.application.views.events;


import com.example.application.domain.EventDto;
import com.example.application.service.EventService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.MainLayout;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.stereotype.Component;

@Component
@PageTitle("Events")
@Route(value = "events", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class EventsView extends Div implements AfterNavigationObserver {

    private final EventService eventService;
    Grid<EventDto> grid = new Grid<>(EventDto.class);
    private EventForm form = new EventForm(this);

    public EventsView(EventService eventService) {
        this.eventService = eventService;
        grid.setColumns("name", "description");
        addClassName("events-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        add(mainContent);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        grid.setItems(eventService.getEvents());

    }

    public void refresh() {
        grid.setItems(eventService.getEvents());
    }


}
