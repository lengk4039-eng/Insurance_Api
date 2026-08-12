package com.api;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}