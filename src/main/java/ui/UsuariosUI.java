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

public class UsuariosUI {

	private ObservableList<UsuarioModel> usuariosList = FXCollections.observableArrayList();
	private AutenticacionSingleton AS = AutenticacionSingleton.getInstance();

	public VBox start(Stage primaryStage) {
		primaryStage.setTitle("Usuarios");

		VBox root = new VBox();
		root.setSpacing(10);

		ListView<UsuarioModel> listView = new ListView<>();
		listView.setItems(usuariosList);

		Button verPerfilBtn = new Button("Ver Perfil");
		Button agregarAmigoBtn = new Button("Agregar Amigo");
		Button denunciarBtn = new Button("Denunciar");

		HBox buttonsBox = new HBox();
		buttonsBox.getChildren().addAll(verPerfilBtn, agregarAmigoBtn, denunciarBtn);

		Label selectedUserLabel = new Label("Usuario seleccionado: ");

		root.getChildren().addAll(listView, buttonsBox, selectedUserLabel);

		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Cargar la lista de usuarios al iniciar la aplicación
		cargarUsuarios();

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
	private void cargarUsuarios() {
		// Aquí deberías obtener la lista de usuarios desde algún servicio o base de
		// datos
		// Por ahora, usaré una lista de ejemplo
		ArrayList<UsuarioModel> usuariosEjemplo = obtenerUsuariosEjemplo();
		usuariosList.addAll(usuariosEjemplo);
	}

	// Método de ejemplo para obtener una lista de usuarios de ejemplo
	private ArrayList<UsuarioModel> obtenerUsuariosEjemplo() {
		ArrayList<UsuarioModel> usuarios = new ArrayList<>();
		// Aquí podrías crear usuarios de ejemplo y agregarlos a la lista
		return usuarios;
	}

}
