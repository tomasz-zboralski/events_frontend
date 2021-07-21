package com.example.application.views.users;

import com.example.application.domain.UserDto;
import com.example.application.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.stereotype.Component;

@Component
public class UserForm extends FormLayout {
    private Binder<UserDto> binder = new Binder<UserDto>(UserDto.class);
    private UsersView usersView;
    private UserService userService;

    private TextField name = new TextField("Name");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    public UserForm(UsersView usersView, UserService userService) {
        this.usersView = usersView;
        this.userService = userService;
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(name, buttons);

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
}
