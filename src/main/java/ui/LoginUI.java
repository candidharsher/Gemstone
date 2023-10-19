package ui;

import java.awt.Insets;
import java.awt.TextField;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inicio de Sesión");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Etiquetas y campos de entrada
        Label usernameLabel = new Label("Nombre de usuario:");
        GridPane.setConstraints(usernameLabel, 0, 0);

        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 0);

        Label passwordLabel = new Label("Contraseña:");
        GridPane.setConstraints(passwordLabel, 0, 1);

        PasswordField passwordInput = new PasswordField();
        GridPane.setConstraints(passwordInput, 1, 1);

        // Botones
        Button loginButton = new Button("Iniciar Sesión");
        GridPane.setConstraints(loginButton, 1, 2);

        Button registerButton = new Button("Registrar");
        GridPane.setConstraints(registerButton, 1, 3);

        // Eventos de botones (aquí puedes agregar la lógica de inicio de sesión y registro)
        loginButton.setOnAction(e -> {
            // Agrega aquí la lógica para iniciar sesión
            // Debes realizar una solicitud al backend de Spring para verificar las credenciales
            // y manejar la respuesta.
        });

        registerButton.setOnAction(e -> {
            // Agrega aquí la lógica para mostrar la ventana de registro
            // Puedes crear otra ventana o diálogo para el registro de usuarios.
        });

        grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton, registerButton);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
