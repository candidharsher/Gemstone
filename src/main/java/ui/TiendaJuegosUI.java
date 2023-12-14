package ui;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.PayPalPaymentHandler;
import com.example.demo.models.JuegoModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TiendaJuegosUI {

	private ObservableList<JuegoModel> juegos = FXCollections.observableArrayList();
	private boolean esAdmin = false; // Variable para controlar si el usuario es administrador
	private final String clientId = "TU_CLIENT_ID";
	private final String clientSecret = "TU_CLIENT_SECRET";
	private final String mode = "sandbox"; // Puedes usar 'sandbox' para pruebas
	private ListView<JuegoModel> listView;

	public VBox start(Stage primaryStage) {
		primaryStage.setTitle("Tienda de Juegos");
		cargarJuegosDesdeBaseDatos();
		VBox root = new VBox();
		root.setSpacing(10);
		listView = new ListView<>();
		listView.setItems(juegos);

		Button comprarButton = new Button("Comprar");
		Button crearNuevoButton = new Button("Insertar Nuevo Juego");

		listView.setCellFactory(param -> new JuegoCell(comprarButton));

		// Si es administrador, muestra el botón para crear nuevo juego
		if (esAdmin) {
			root.getChildren().add(crearNuevoButton);
		}

		root.getChildren().addAll(listView);
		// Agregar un TextField para búsqueda
		TextField buscarTextField = new TextField();
		buscarTextField.setPromptText("Buscar por nombre de juego");
		buscarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			// Aquí puedes manejar la lógica de búsqueda
			// Por ejemplo, puedes filtrar la lista 'juegos' basándote en el texto
			// introducido
			filtrarJuegos(newValue, listView);
		});
		root.getChildren().add(0, buscarTextField);
		comprarButton.setOnAction(event -> {
			JuegoModel juegoSeleccionado = listView.getSelectionModel().getSelectedItem();
			if (juegoSeleccionado != null) {
				PayPalPaymentHandler payPalHandler = new PayPalPaymentHandler(clientId, clientSecret, mode);
				double precioJuego = obtenerPrecioJuego(juegoSeleccionado); // Método para obtener el precio del juego
				String orderId = payPalHandler.crearOrdenDePago(precioJuego);
				if (orderId != null) {
					// Redirigir a la URL de PayPal para que el usuario complete el pago
					redirigirAPayPal(orderId);
				} else {
					// Manejar el caso en que no se pueda crear la orden de pago
					System.out.println("Error al crear la orden de pago");
				}
			} else {
				// Manejar el caso en que no se haya seleccionado ningún juego
				System.out.println("Por favor, seleccione un juego para comprar");
			}
		});
		Scene scene = new Scene(root, 400, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
		return root;
	}

	private void redirigirAPayPal(String orderId) {
		// TODO Auto-generated method stub

	}

	private double obtenerPrecioJuego(JuegoModel juegoSeleccionado) {
		// TODO Auto-generated method stub
		return 0;
	}

	// Define la celda para mostrar cada juego en la lista
	private class JuegoCell extends ListCell<JuegoModel> {
		private final Button comprarButton;

		public JuegoCell(Button comprarButton) {
			this.comprarButton = comprarButton;
		}

		@Override
		protected void updateItem(JuegoModel juego, boolean empty) {
			super.updateItem(juego, empty);

			if (empty || juego == null) {
				setText(null);
				setGraphic(null);
			} else {
				// Crea una estructura visual para mostrar los detalles del juego
				VBox vbox = new VBox();
				Label tituloLabel = new Label(juego.getNombre());
				// Mostrar detalles adicionales del juego aquí
				vbox.getChildren().addAll(tituloLabel, comprarButton);

				// Si el usuario es administrador, muestra el botón para crear
				if (esAdmin) {
					Button borrarButton = new Button("Borrar");
					vbox.getChildren().add(borrarButton);
				}

				setGraphic(vbox);
			}
		}
	}

	// Método para cargar juegos desde la base de datos y agregarlos a la lista
	private void cargarJuegosDesdeBaseDatos() {
		RestTemplate restTemplate = new RestTemplate();

		// Realizar una solicitud GET al endpoint que devuelve todos los juegos
		ResponseEntity<List<JuegoModel>> response = restTemplate.exchange("http://localhost:8080/juegos/allgames",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<JuegoModel>>() {
				});

		if (response.getStatusCode() == HttpStatus.OK) {
			// Obtener la lista de juegos de la respuesta y agregarlos a la lista 'juegos'
			List<JuegoModel> juegosDesdeBD = response.getBody();
			juegos.addAll(juegosDesdeBD);
			// Actualizar la ListView con los juegos cargados
			listView.setItems(juegos);
		} else {
			// Manejar el caso en que no se puedan obtener los juegos
			System.out.println("Error al cargar los juegos desde la base de datos");
		}
	}

	private void filtrarJuegos(String textoBusqueda, ListView<JuegoModel> listView) {
		if (textoBusqueda == null || textoBusqueda.isEmpty()) {
			// Si el campo de búsqueda está vacío, muestra todos los juegos
			listView.setItems(juegos);
		} else {
			// Si hay texto en el campo de búsqueda, filtra los juegos por nombre
			ObservableList<JuegoModel> juegosFiltrados = juegos
					.filtered(juego -> juego.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase()));
			listView.setItems(juegosFiltrados); // Muestra solo los juegos que coincidan con la búsqueda
		}

	}
}
