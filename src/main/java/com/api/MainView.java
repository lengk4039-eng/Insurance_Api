package com.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.api.entity.User;
import com.api.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("") // http://localhost:8081/
public class MainView extends VerticalLayout {

    // 1. Declare UI Components
    private final Grid<User> grid = new Grid<>(User.class, false);
    private final TextField nameField = new TextField("Name");
    private final TextField emailField = new TextField("Email");
    private final Button addButton = new Button("Add User");

    // 2. Inject Service
    private final UserService userService;

    @Autowired
    public MainView(UserService userService) {
        this.userService = userService;

        // Configure Grid Columns
        grid.addColumn(User::getUserID).setHeader("User ID");
        grid.addColumn(User::getName).setHeader("Name");
        grid.addColumn(User::getEmail).setHeader("Email");

        // Load Initial Data from Backend
        refreshGrid();

        // Button Click Event using Lambda syntax
        addButton.addClickListener(click -> {
            if (!nameField.getValue().isEmpty()) {
                User user = new User();
                user.setName(nameField.getValue());
                user.setEmail(emailField.getValue()); // Set email as well if field is filled
                
                // Save via Service
                userService.saveUser(user);
                
                // Refresh UI and clear inputs
                refreshGrid();
                nameField.clear();
                emailField.clear();
                Notification.show("User saved successfully!");
            } else {
                Notification.show("Please enter a name!");
            }
        });

        // Add components to layout
        add(nameField, emailField, addButton, grid);
    }

    private void refreshGrid() {
        grid.setItems(userService.getAllUsers());
    }
}