package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;
import ui.LoginUI;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		// Iniciar la aplicación JavaFX
        Application.launch(LoginUI.class, args);
	}

}
