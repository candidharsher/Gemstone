package ui;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.models.UsuarioModel;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PerfilView {
	private UsuarioModel usuario;

	public PerfilView() {
	}

	public UsuarioModel getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioModel usuario) {
		this.usuario = usuario;
	}

	public VBox mostrarPerfil(Stage perfilStage) {
		// Crea un diseño para la vista del perfil
		VBox perfilLayout = new VBox();
		perfilLayout.setSpacing(10);

		// Crea etiquetas para mostrar los datos del perfil
		Label usernameLabel = new Label("Nombre de usuario: " + usuario.getUser());
		Label emailLabel = new Label("Email: " + usuario.getEmail());

		// Agrega las etiquetas al diseño
		perfilLayout.getChildren().addAll(usernameLabel, emailLabel);
		// Crea una sección para la foto de perfil
		HBox photoBox = new HBox();
		ImageView photoImageView = new ImageView();
		photoImageView.setFitWidth(100);
		photoImageView.setFitHeight(100);
		photoImageView.setImage(new Image("file:placeholder.jpg")); // Ruta de la imagen de placeholder

		Button changePhotoButton = new Button("Cambiar Foto");
		changePhotoButton.setOnAction(e -> cambiarFotoPerfil(photoImageView));

		photoBox.getChildren().addAll(photoImageView, changePhotoButton);
		perfilLayout.getChildren().add(photoBox);
		if (usuario.isAdmin()) {
			ImageView imageView = new ImageView();
			// Carga la imagen del usuario en el ImageView
			String imagePath = "31692344.jpg"; // Reemplaza con la ruta real de la imagen. en mi caso esta en la
												// carp raiz del proyecto donde el pom y todo eso
			String imageUrl = Paths.get(imagePath).toUri().toString();
			Image image = new Image(imageUrl);
			imageView.setImage(image);
			perfilLayout.getChildren().add(imageView);
		}

		// Botones y campos de edición
		Button cerrarSesionButton = new Button("Cerrar Sesión");
		Button guardarCambiosButton = new Button("Guardar Cambios");
		TextField emailField = new TextField(usuario.getEmail());

		// Manejadores de eventos
		cerrarSesionButton.setOnAction(e -> cerrarSesion(perfilStage));
		guardarCambiosButton.setOnAction(e -> actualizarCorreoElectronico(emailField.getText()));

		// Agrega elementos al diseño del perfil
		perfilLayout.getChildren().addAll(emailField, guardarCambiosButton, cerrarSesionButton);

		// Configura la escena con el diseño del perfil y ábrela en el nuevo escenario
		Scene perfilScene = new Scene(perfilLayout, 300, 200); // Ajusta el tamaño según tus preferencias
		perfilStage.setScene(perfilScene);
		// Agregar botones para manejar solicitudes de amistad en el método
		// mostrarPerfil
		Button agregarAmigoButton = new Button("Agregar amigo");
		Button aceptarSolicitudButton = new Button("Aceptar solicitud");

		// Lógica para enviar solicitudes de amistad
		agregarAmigoButton.setOnAction(e -> enviarSolicitudAmistad());

		// Lógica para aceptar solicitudes de amistad pendientes
		aceptarSolicitudButton.setOnAction(e -> aceptarSolicitudAmistadPendiente());
		// Crear una sección para mostrar los mensajes recibidos
	    VBox mensajesRecibidosBox = new VBox();
	    mensajesRecibidosBox.setSpacing(5);

	    Label mensajesRecibidosLabel = new Label("Mensajes Recibidos:");
	    mensajesRecibidosBox.getChildren().add(mensajesRecibidosLabel);

	    // Obtener mensajes recibidos del usuario
	    for (Mensaje mensaje : usuario.getMensajesRecibidos()) {
	        Label mensajeLabel = new Label(mensaje.getContenido());
	        mensajesRecibidosBox.getChildren().add(mensajeLabel);
	    }

	    // Agregar la sección de mensajes recibidos al diseño del perfil
	    perfilLayout.getChildren().add(mensajesRecibidosBox);

		// Agregar los botones al diseño del perfil
		perfilLayout.getChildren().addAll(agregarAmigoButton, aceptarSolicitudButton);
		
		// Muestra el escenario de la vista del perfil
		perfilStage.show();
		return perfilLayout;
	}

	private void cambiarFotoPerfil(ImageView photoImageView) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Seleccionar nueva foto de perfil");
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			Image newPhoto = new Image(selectedFile.toURI().toString());
			photoImageView.setImage(newPhoto);
		}
	}

	private void cerrarSesion(Stage perfilStage) {
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
	}

	// Método para enviar solicitudes de amistad
	private void enviarSolicitudAmistad() {
		// Lógica para enviar la solicitud de amistad
		// Puedes usar AutenticacionSingleton para obtener información del usuario
		// actual y enviar la solicitud
	}

	// Método para aceptar solicitudes de amistad pendientes
	private void aceptarSolicitudAmistadPendiente() {
		// Lógica para aceptar solicitudes de amistad pendientes
		// Puedes utilizar AutenticacionSingleton para obtener las solicitudes
		// pendientes del usuario actual y aceptarlas
	}

	private void actualizarCorreoElectronico(String nuevoEmail) {
		usuario.setEmail(nuevoEmail); // Actualiza el correo electrónico en el objeto UsuarioModel

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<UsuarioModel> response = restTemplate.postForEntity("http://localhost:8080/usuario/editar-email",
				usuario, UsuarioModel.class);

		// Maneja la respuesta según tus necesidades
		if (response.getStatusCode().equals(HttpStatus.OK)) {
			// Actualización exitosa
			// Puedes mostrar un mensaje de éxito o realizar alguna acción adicional
			System.out.println("Correo electrónico actualizado correctamente");
		} else {
			// Actualización fallida
			// Maneja el error adecuadamente
			System.out.println("Error al actualizar el correo electrónico");
		}
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

	private void mostrarMensajesRecibidos() {
		// Mostrar los mensajes recibidos en la interfaz
		AutenticacionSingleton AS = AutenticacionSingleton.getInstance();
		UsuarioModel usuarioConectado = AS.getUsuarioConectado();
		for (Mensaje mensaje : usuarioConectado.getMensajesRecibidos()) {
			// Mostrar cada mensaje en la interfaz
		}
	}

	private void enviarMensaje(UsuarioModel destinatario, String contenido) {
		AutenticacionSingleton AS = AutenticacionSingleton.getInstance();
		UsuarioModel usuarioConectado = AS.getUsuarioConectado();
		usuarioConectado.enviarMensaje(destinatario, contenido);
		// Actualizar la interfaz para mostrar el mensaje enviado
	}

}
