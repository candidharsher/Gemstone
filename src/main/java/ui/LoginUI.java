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
	private static boolean success;
	private static boolean loginButtonClicked;

	public static boolean isSuccess() {
		return success;
	}

	public static boolean isloginButtonClicked() {
		return loginButtonClicked;
	}

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
			this.loginButtonClicked = true;
			String username = usernameInput.getText();
			String password = passwordInput.getText();

			// Crea una entidad HTTP con las credenciales
			UsuarioModel credenciales = new UsuarioModel(username, password);

			// Realiza una solicitud POST al backend para iniciar sesión
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/usuario/iniciar-sesion",
					HttpMethod.POST, new HttpEntity<>(credenciales), String.class);

			// Agrega declaraciones de impresión para depuración
			System.out.println("Respuesta del backend: " + response.getStatusCodeValue());

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				// El inicio de sesión fue exitoso
				String authToken = response.getBody();

				// Aquí puedes guardar el token de autenticación en algún lugar seguro,
				// como en una variable de sesión o en un Singleton de autenticación.
				// Por ejemplo, puedes crear una clase Singleton de Autenticación para almacenar
				// el token.
				AutenticacionSingleton.getInstance().setAuthToken(authToken);
				/*
				 * Para un futuro, lo que sería más correcto: Modifica la solicitud adicional
				 * para obtener el usuario basado en el token. En lugar de buscar al usuario por
				 * su nombre de usuario, puedes buscarlo por el token de autenticación que se
				 * pasa en el encabezado de la solicitud. Esta es una forma más segura de
				 * obtener los datos del usuario después de la autenticación. Por ejemplo:
				 * ResponseEntity<UsuarioModel> responseUsuario = restTemplate.exchange(
				 * "http://localhost:8080/usuario/perfil", HttpMethod.GET, new
				 * HttpEntity<>(null, createHeaders(authToken)), UsuarioModel.class ); //
				 * Reemplazar con la URL y el endpoint correctos en nuestro backend
				 * 
				 * 
				 */
				// Luego, puedes navegar a otra vista o realizar cualquier acción adicional.
				// Realiza una solicitud adicional para obtener los datos del usuario
				ResponseEntity<UsuarioModel> responseUsuario = restTemplate.exchange(
						"http://localhost:8080/usuario/username/" + username, // Reemplaza con la URL correcta
						HttpMethod.GET, new HttpEntity<>(null), UsuarioModel.class);
				UsuarioModel usuario = responseUsuario.getBody();
				// Por ejemplo, abrir la vista principal de la aplicación.
				AutenticacionSingleton.getInstance().setUsuarioConectado(usuario);
				this.success = true;
				openFinestresApp(usuario);

				// Puedes cerrar la ventana actual de inicio de sesión si es necesario.
				primaryStage.close();
			} else {
				// El inicio de sesión falló, maneja el error adecuadamente
				this.success = false;
				System.out.println("Inicio de sesión fallido. Credenciales incorrectas.");
				// Puedes mostrar un mensaje de error al usuario en la interfaz.
			}
		});

		registerButton.setOnAction(e -> {
			// Agrega aquí la lógica para mostrar la ventana de registro

			// Puedes crear otra ventana o diálogo para el registro de usuarios.
			String username = usernameInput.getText();
			String password = passwordInput.getText();

			// Crea una entidad HTTP con las credenciales
			UsuarioModel credenciales = new UsuarioModel(username, password);

			// Realiza una solicitud POST al backend para registrar un nuevo usuario
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/usuario/registrar",
					HttpMethod.POST, new HttpEntity<>(credenciales), String.class);
			// Agrega declaraciones de impresión para depuración
			System.out.println("Respuesta del backend: " + response.getStatusCodeValue());

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				// El registro fue exitoso
				// Puedes realizar alguna acción adicional aquí, como mostrar un mensaje de
				// éxito.
				System.out.println("Feliciades por unirse a la plataforma.");
			} else {
				// El registro falló, maneja el error adecuadamente
				System.out.println("Error al registrar el usuario. Verifica las credenciales.");
				// Puedes mostrar un mensaje de error al usuario en la interfaz.
				// de momento sólo son en consola... hay que mejorarlo
			}
		});

		grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton,
				registerButton);

		Scene scene = new Scene(grid, 300, 200);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void openFinestresApp(UsuarioModel usuario) {
		FinestresApp finestresApp = new FinestresApp(usuario);
		Stage primaryStage = new Stage();
		finestresApp.start(primaryStage);
		primaryStage.show();
	}

}
