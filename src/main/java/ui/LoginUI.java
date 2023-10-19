package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.models.UsuarioModel;

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

		// Eventos de botones
		loginButton.setOnAction(e -> {
			String username = usernameInput.getText();
			String password = passwordInput.getText();

			// Crea una entidad HTTP con las credenciales
			UsuarioModel credenciales = new UsuarioModel(username, password);

			// Realiza una solicitud POST al backend para iniciar sesión
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/usuario/iniciar-sesion",
					HttpMethod.POST, new HttpEntity<>(credenciales), String.class);

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				// El inicio de sesión fue exitoso
				String authToken = response.getBody();

				// Aquí puedes guardar el token de autenticación en algún lugar seguro,
				// como en una variable de sesión o en un Singleton de autenticación.
				// Por ejemplo, puedes crear una clase Singleton de Autenticación para almacenar
				// el token.
				AutenticacionSingleton.getInstance().setAuthToken(authToken);

				// Luego, puedes navegar a otra vista o realizar cualquier acción adicional.
				// Por ejemplo, abrir la vista principal de la aplicación.
				openPerfilView(primaryStage, credenciales);

				// Puedes cerrar la ventana actual de inicio de sesión si es necesario.
				primaryStage.close();
			} else {
				// El inicio de sesión falló, maneja el error adecuadamente
				System.out.println("Inicio de sesión fallido. Credenciales incorrectas.");
				// Puedes mostrar un mensaje de error al usuario en la interfaz.
			}
		});

		registerButton.setOnAction(e -> {
			// Agrega aquí la lógica para mostrar la ventana de registro
			// Puedes crear otra ventana o diálogo para el registro de usuarios.
		});

		grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton,
				registerButton);

		Scene scene = new Scene(grid, 300, 200);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void openPerfilView(Stage primaryStage, UsuarioModel usuario) {
	    // Crea una instancia de la vista principal (PerfilView) y pasa el UsuarioModel como argumento.
		// Crea una nueva ventana para la vista del perfil
        Stage perfilStage = new Stage();
        perfilStage.setTitle("Perfil de Usuario");

        // Crea una instancia de PerfilView y pasa el nombre de usuario
        PerfilView perfilView = new PerfilView(usuario);

        // Muestra la vista del perfil en la nueva ventana
        perfilView.mostrarPerfil(perfilStage);

        // Cierra la ventana actual de inicio de sesión (LoginUI)
        primaryStage.close();
    }


}
