package ui;

import java.awt.TextArea;

import org.springframework.web.client.RestTemplate;

import com.example.demo.models.JuegoModel;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NuevoJuegoDialog {
	private RestTemplate restTemplate = new RestTemplate();
	private Stage dialogStage = new Stage();

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void mostrarDialog(Stage ownerStage) {
		Stage dialogStage = new Stage();
		dialogStage.initOwner(ownerStage);

		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10));

		TextField nombreField = new TextField();
		nombreField.setPromptText("Nombre del juego");

		TextField descripcionArea = new TextField();
		descripcionArea.setText("Descripción del juego");

		TextField precioField = new TextField();
		precioField.setPromptText("Precio del juego");

		Button guardarButton = new Button("Guardar");
		guardarButton.setOnAction(e -> {
			// Aquí obtienes los valores de los campos nombre, descripción y precio
			String nombre = nombreField.getText();
			String descripcion = descripcionArea.getText();
			double precio = Double.parseDouble(precioField.getText());

			// Luego, envías estos datos al backend para crear un nuevo juego
			crearNuevoJuego(nombre, descripcion, precio);

			dialogStage.close();
		});

		root.getChildren().addAll(nombreField, descripcionArea, precioField, guardarButton);

		Scene dialogScene = new Scene(root, 300, 200);
		dialogStage.setScene(dialogScene);
		dialogStage.show();
	}

	private void crearNuevoJuego(String nombre, String descripcion, double precio) {
		JuegoModel nuevoJuego = new JuegoModel(nombre, descripcion, precio);
		restTemplate.postForObject("http://localhost:8080/juegos", nuevoJuego, JuegoModel.class);
	}
}