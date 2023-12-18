package ui;

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

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class TiendaJuegosUI {

	private ObservableList<JuegoModel> juegos = FXCollections.observableArrayList();
	private boolean esAdmin = false;
	private final String clientId = "TU_CLIENT_ID";
	private final String clientSecret = "TU_CLIENT_SECRET";
	private RestTemplate restTemplate = new RestTemplate();
	private final String mode = "sandbox";
	private ListView<JuegoModel> listView;
	private AutenticacionSingleton AS = AutenticacionSingleton.getInstance();

	public VBox start(Stage primaryStage) {
		primaryStage.setTitle("Tienda de Juegos");

		VBox root = new VBox();
		root.setSpacing(10);
		listView = new ListView<>();
		cargarJuegosDesdeBaseDatos();
		ListView<String> list = new ListView<>();

		// Recorre los juegos en propiedad y agrega sus representaciones de cadena al
		// ListView
		for (JuegoModel juego : juegos) {
			list.getItems().add(juego.toString());
		}
		Button comprarButton = new Button("Comprar");
		Button crearNuevoButton = new Button("Insertar Nuevo Juego");

		listView.setCellFactory(param -> new JuegoCell(comprarButton));

		if (esAdmin) {
			root.getChildren().add(crearNuevoButton);
		}

		

		TextField buscarTextField = new TextField();
		buscarTextField.setPromptText("Buscar por nombre de juego");
		buscarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filtrarJuegos(newValue, listView);
		});
		root.getChildren().add(0, buscarTextField);
		root.getChildren().addAll(list, comprarButton, crearNuevoButton);

		comprarButton.setOnAction(event -> {
			JuegoModel juegoSeleccionado = listView.getSelectionModel().getSelectedItem();
			añadirJuegoSeleccionado(juegoSeleccionado);
			if (juegoSeleccionado != null) {
				PayPalPaymentHandler payPalHandler = new PayPalPaymentHandler(clientId, clientSecret, mode);
				double precioJuego = obtenerPrecioJuego(juegoSeleccionado);
				String orderId = payPalHandler.crearOrdenDePago(precioJuego);
				if (orderId != null) {
					redirigirAPayPal(orderId);
				} else {
					System.out.println("Error al crear la orden de pago");
				}
			} else {
				System.out.println("Por favor, seleccione un juego para comprar");
			}

		});

		Scene scene = new Scene(root, 400, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
		return root;
	}

	private void añadirJuegoSeleccionado(JuegoModel juegoSeleccionado) {
		if (AS.getUsuarioConectado() != null && juegoSeleccionado != null) {
			String url = "http://localhost:8080/usuario/agregar-juego/" + AS.getUsuarioConectado().getId();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<JuegoModel> request = new HttpEntity<>(juegoSeleccionado, headers);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				PayPalPaymentHandler payPalHandler = new PayPalPaymentHandler(clientId, clientSecret, mode);
				double precioJuego = obtenerPrecioJuego(juegoSeleccionado);
				String orderId = payPalHandler.crearOrdenDePago(precioJuego);
				if (orderId != null) {
					redirigirAPayPal(orderId);
				} else {
					System.out.println("Error al crear la orden de pago");
				}
			} else {
				System.out.println("Error al agregar el juego al usuario");
			}
		} else {
			System.out.println("Por favor, seleccione un juego y asegúrese de estar conectado");
		}

	}

	private double obtenerPrecioJuego(JuegoModel juegoSeleccionado) {
		return juegoSeleccionado.getPrecio();
	}

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
				VBox vbox = new VBox();
				Label tituloLabel = new Label(juego.getNombre());
				vbox.getChildren().addAll(tituloLabel, comprarButton);

				if (esAdmin) {
					Button borrarButton = new Button("Borrar");
					vbox.getChildren().add(borrarButton);
				}

				setGraphic(vbox);
			}
		}
	}

	private void cargarJuegosDesdeBaseDatos() {
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<List<JuegoModel>> response = restTemplate.exchange("http://localhost:8080/juegos/allgames",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<JuegoModel>>() {
				});

		if (response.getStatusCode() == HttpStatus.OK) {
			List<JuegoModel> juegosDesdeBD = response.getBody();
			juegos.addAll(juegosDesdeBD);
			listView.setItems(juegos);
		} else {
			System.out.println("Error al cargar los juegos desde la base de datos");
		}
	}

	private void filtrarJuegos(String textoBusqueda, ListView<JuegoModel> listView) {
		if (textoBusqueda == null || textoBusqueda.isEmpty()) {
			listView.setItems(juegos);
		} else {
			ObservableList<JuegoModel> juegosFiltrados = juegos
					.filtered(juego -> juego.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase()));
			listView.setItems(juegosFiltrados);
		}
	}

	private void redirigirAPayPal(String orderId) {
		PayPalPaymentHandler payPalHandler = new PayPalPaymentHandler(clientId, clientSecret, mode);
		String authorizationURL = payPalHandler.obtenerURLAutorizacion(orderId);
		if (authorizationURL != null && !authorizationURL.isEmpty()) {
			abrirNavegadorConURL(authorizationURL);
		} else {
			System.out.println("Error al obtener la URL de autorización de PayPal");
		}
	}

	private String obtenerURLPayPal(String orderId) {
		PayPalPaymentHandler payPalHandler = new PayPalPaymentHandler(clientId, clientSecret, mode);
		return payPalHandler.obtenerURLPayPal(orderId);
	}

	private void abrirNavegadorConURL(String urlPayPal) {
		try {
			java.awt.Desktop.getDesktop().browse(new java.net.URI(urlPayPal));
		} catch (java.io.IOException | java.net.URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
