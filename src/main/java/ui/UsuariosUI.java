package ui;

import com.example.demo.models.UsuarioModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UsuariosUI {

	private List<UsuarioModel> usuariosList = new ArrayList();
	private AutenticacionSingleton AS = AutenticacionSingleton.getInstance();
	private RestTemplate restTemplate = new RestTemplate();

	public VBox start(Stage primaryStage) {
		primaryStage.setTitle("Usuarios");
		// Cargar la lista de usuarios al iniciar la aplicación
		this.usuariosList = cargarUsuarios();

		VBox root = new VBox();
		root.setSpacing(10);

		ListView<UsuarioModel> listView = new ListView<>();
		listView.setItems(cargarUsuarios());
		ListView<String> list = new ListView<>();

		// Recorre los juegos en propiedad y agrega sus representaciones de cadena al
		// ListView
		for (UsuarioModel usuario : usuariosList) {
			list.getItems().add(usuario.toString());
		}

		Button verPerfilBtn = new Button("Ver Perfil");
		Button agregarAmigoBtn = new Button("Agregar Amigo");
		Button denunciarBtn = new Button("Denunciar");

		HBox buttonsBox = new HBox();
		buttonsBox.getChildren().addAll(verPerfilBtn, agregarAmigoBtn, denunciarBtn);

		Label selectedUserLabel = new Label("Usuario seleccionado: ");

		root.getChildren().addAll(list, buttonsBox, selectedUserLabel);

		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Lógica para manejar acciones de botones y eventos de la lista
		listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				selectedUserLabel.setText("Usuario seleccionado: " + newValue.getUsername());
				// Aquí podrías hacer algo con el usuario seleccionado
			}
		});

		verPerfilBtn.setOnAction(event -> {
			// Lógica para ver el perfil del usuario seleccionado
		});

		agregarAmigoBtn.setOnAction(event -> {
			// Lógica para agregar al usuario como amigo
		});

		denunciarBtn.setOnAction(event -> {
			// Lógica para denunciar al usuario
		});
		return root;
	}

	// Método para cargar la lista de usuarios
	private ObservableList<UsuarioModel> cargarUsuarios() {
		ObservableList<UsuarioModel> usuarios = FXCollections.observableArrayList();
		// URL del endpoint para obtener la lista de usuarios
		String url = "http://localhost:8080/usuario/todos";

		// Realizar la solicitud GET al controlador
		ResponseEntity<ArrayList<UsuarioModel>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<ArrayList<UsuarioModel>>() {
				});

		// Verificar si la solicitud fue exitosa y agregar los usuarios a la lista
		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			usuarios.addAll(response.getBody());
		} else {
			// Manejar el caso en el que la solicitud falle
			System.out.println("Error al obtener la lista de usuarios desde el backend");
		}

		return usuarios;
	}

}
