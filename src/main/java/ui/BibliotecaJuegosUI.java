package ui;

import com.example.demo.models.JuegoModel;
import com.example.demo.models.UsuarioModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BibliotecaJuegosUI {

	private ObservableList<JuegoModel> juegosEnPropiedad = FXCollections.observableArrayList();
	private AutenticacionSingleton AS = AutenticacionSingleton.getInstance();
	private ArrayList<JuegoModel> juegos = new ArrayList<>();

	public VBox start(Stage primaryStage) {
		primaryStage.setTitle("Biblioteca de Juegos");

		VBox root = new VBox();
		root.setSpacing(10);
		ListView<JuegoModel> listView = new ListView<>();
		cargarJuegosEnPropiedad();
		listView.setItems(juegosEnPropiedad);
		ListView<String> list = new ListView<>();

		// Recorre los juegos en propiedad y agrega sus representaciones de cadena al
		// ListView
		for (JuegoModel juego : juegosEnPropiedad) {
			list.getItems().add(juego.toString());
		}

		root.getChildren().addAll(list);

		Scene scene = new Scene(root, 400, 300);
		primaryStage.setScene(scene);
		primaryStage.show();

		TextField buscarTextField = new TextField();
		buscarTextField.setPromptText("Buscar por nombre de juego");
		buscarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			// Aquí puedes manejar la lógica de búsqueda
			// Por ejemplo, puedes filtrar la lista 'juegos' basándote en el texto
			// introducido
			filtrarJuegos(newValue, listView);
		});
		return root;
	}

	// Método para cargar los juegos en la biblioteca del usuario
	private void cargarJuegosEnPropiedad() {
		// Aquí deberías obtener el usuario actual, por ejemplo, desde algún singleton o
		// autenticación
		UsuarioModel usuarioActual = obtenerUsuarioActual();

		if (usuarioActual != null && usuarioActual.getJuegosEnPropiedad() != null) {
			// Obtener los juegos en propiedad del usuario y agregarlos a la lista
			ArrayList<JuegoModel> juegosPropiedad = usuarioActual.getJuegosEnPropiedad();
			juegosEnPropiedad.addAll(juegosPropiedad);
		}
	}

	// Ejemplo de obtener el usuario actual (puedes reemplazar esto con tu lógica
	// real)
	private UsuarioModel obtenerUsuarioActual() {
		// Aquí podrías obtener el usuario actual autenticado, por ejemplo, desde algún
		// singleton o servicio
		// En este ejemplo, devuelvo un usuario ficticio
		UsuarioModel usuario = AS.getUsuarioConectado();
		return usuario;
	}

	// Método de ejemplo para obtener una lista de juegos de ejemplo
	private ArrayList<JuegoModel> obtenerJuegosDeEjemplo() {
		juegos.add(new JuegoModel("Juego 1", "Descripción del Juego 1"));
		juegos.add(new JuegoModel("Juego 2", "Descripción del Juego 2"));
		// Agrega más juegos si es necesario
		return juegos;
	}

	private void filtrarJuegos(String textoBusqueda, ListView<JuegoModel> listView) {
		if (textoBusqueda == null || textoBusqueda.isEmpty()) {
			// Si el campo de búsqueda está vacío, restaura la lista original de juegos en
			// propiedad
			listView.setItems(juegosEnPropiedad);
		} else {
			// Si hay texto en el campo de búsqueda, filtra los juegos por nombre
			ObservableList<JuegoModel> juegosFiltrados = juegosEnPropiedad
					.filtered(juego -> juego.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase()));
			listView.setItems(juegosFiltrados); // Muestra solo los juegos que coincidan con la búsqueda
		}

	}
}
