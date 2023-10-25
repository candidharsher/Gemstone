package ui;

import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.models.UsuarioModel;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PerfilView {
	UsuarioModel usuario;

	public PerfilView(UsuarioModel usuario) {
		this.usuario = usuario;
	}

	public void mostrarPerfil(Stage perfilStage) {
		// Crea un diseño para la vista del perfil
		VBox perfilLayout = new VBox();
		perfilLayout.setSpacing(10);

		// Crea etiquetas para mostrar los datos del perfil
		Label usernameLabel = new Label("Nombre de usuario: " + usuario.getUser());
		Label emailLabel = new Label("Email: " + usuario.getEmail());

		// Agrega las etiquetas al diseño
		perfilLayout.getChildren().addAll(usernameLabel, emailLabel);
		if (usuario.isAdmin()) {
			ImageView imageView = new ImageView();
			// Carga la imagen del usuario en el ImageView
			String imagePath = "31692344.jpg";// Reemplaza con la ruta real de la imagen. en mi caso esta en la
												// carp raiz del proyecto donde el pom y todo eso
			String imageUrl = Paths.get(imagePath).toUri().toString();
			Image image = new Image(imageUrl);
			imageView.setImage(image);
			perfilLayout.getChildren().add(imageView);
		}
		// AGREGADO!!! un botón de Cerrar Sesión
		Button cerrarSesionButton = new Button("Cerrar Sesión");
		cerrarSesionButton.setOnAction(e -> {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/usuario/cerrar-sesion",
					usuario, String.class);

			// Maneja la respuesta según tus necesidades.
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				// Cierre de sesión exitoso
				// redirigir a la página de inicio de sesión, por ejemplo.
				openLoginUI(perfilStage);
				perfilStage.close(); // Cierra la ventana actual
			} else {
				// Cierre de sesión fallido
				// Maneja el error adecuadamente
				System.out.println("Error al cerrar la sesión");
			}
		});
		perfilLayout.getChildren().add(cerrarSesionButton);
		// Crea un nuevo escenario (Stage) para la vista del perfil
		perfilStage.setTitle("Perfil de Usuario");

		// Configura la escena con el diseño del perfil y ábrela en el nuevo escenario
		Scene perfilScene = new Scene(perfilLayout, 300, 200); // Ajusta el tamaño según tus preferencias
		perfilStage.setScene(perfilScene);

		// Muestra el escenario de la vista del perfil
		perfilStage.show();
	}

	private void openLoginUI(Stage perfilStage) {
		Stage loginStage = new Stage();
		loginStage.setTitle("Inicio de Sesión");

		// Crea una instancia de PerfilView y pasa el nombre de usuario
		LoginUI login = new LoginUI();

		// Muestra la vista del perfil en la nueva ventana
		login.start(loginStage);
		// Cierra la ventana actual de inicio de sesión (LoginUI)
		perfilStage.close();

	}
}
