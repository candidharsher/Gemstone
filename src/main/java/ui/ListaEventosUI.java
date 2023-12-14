package ui;

import com.example.demo.controllers.EventoController;
import com.example.demo.models.EventoModel;
import com.example.demo.models.JuegoModel;
import com.example.demo.models.UsuarioModel;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
public class ListaEventosUI {

	private ObservableList<EventoModel> eventos = FXCollections.observableArrayList(); // patron de diseño observable,
																						// revisar contenido M9
	private boolean esAdmin = false; // Variable para controlar si el usuario es administrador

	private RestTemplate restTemplate = new RestTemplate();

	public VBox start(Stage primaryStage) {
		primaryStage.setTitle("Lista de Eventos");
		AutenticacionSingleton AS = AutenticacionSingleton.getInstance();
		VBox root = new VBox();
		root.setSpacing(10);

		ListView<EventoModel> listView = new ListView<>();
		listView.setItems(eventos);

		Button apuntarseButton = new Button("Apuntarse");
		Button borrarButton = new Button("Borrar");
		Button crearNuevoButton = new Button("Crear Nuevo Evento");

		listView.setCellFactory(param -> new EventoCell(apuntarseButton, borrarButton));
		if (AS.getUsuarioConectado().isAdmin()) {
			this.esAdmin = true;
		} else {
			this.esAdmin = false;
		}
		// Si es administrador, muestra el botón para crear nuevo evento y borrarlos
		if (esAdmin) {
			root.getChildren().add(crearNuevoButton);
			root.getChildren().add(borrarButton);

		}
		root.getChildren().add(apuntarseButton);
		root.getChildren().addAll(listView);

		// Acción al hacer clic en el botón "Apuntarse"
		apuntarseButton.setOnAction(event -> {
			EventoModel eventoSeleccionado = listView.getSelectionModel().getSelectedItem();
			if (eventoSeleccionado != null) {

				// Enviar solicitud POST al backend para inscribirse en el evento seleccionado
				String apuntarseURL = "http://localhost:8080/evento/apuntarse/" + eventoSeleccionado.getId() + "/"
						+ AS.getUsuarioConectado().getId(); // Reemplaza "usuarioId" con el ID del usuario actual
				ResponseEntity<String> response = restTemplate.postForEntity(apuntarseURL, null, String.class);

				// Verificar la respuesta y actualizar la interfaz en consecuencia
				if (response.getStatusCode() == HttpStatus.OK) {
					// Si la inscripción es exitosa, muestra un mensaje o realiza alguna acción en
					// la interfaz
					System.out.println("Inscripción exitosa");
				} else {
					// Manejar la respuesta en caso de error
					System.out.println("Error al inscribirse al evento");
				}
			}
		});
		// Acción al hacer clic en el botón "Crear Nuevo Evento"
		crearNuevoButton.setOnAction(event -> {
			Stage crearEventoStage = new Stage();
			crearEventoStage.setTitle("Crear Nuevo Evento");

			VBox crearEventoRoot = new VBox();
			crearEventoRoot.setSpacing(10);

			// Campos para el formulario de creación de evento
			TextField tituloField = new TextField();
			tituloField.setPromptText("Título del evento");

			TextField descripcionField = new TextField();
			descripcionField.setPromptText("Descripción del evento");

			DatePicker fechaInicioPicker = new DatePicker();
			fechaInicioPicker.setPromptText("Fecha de inicio");

			DatePicker fechaFinPicker = new DatePicker();
			fechaFinPicker.setPromptText("Fecha de fin");

			// Selector de juegos desde la base de datos
			ComboBox<JuegoModel> juegosComboBox = new ComboBox<>();
			// Aquí debes cargar los juegos desde la base de datos
			// Por ejemplo, si tienes una lista de juegos, puedes cargarla así:

			List<JuegoModel> listaDeJuegos = obtenerJuegosDesdeLaBaseDeDatos();
			juegosComboBox.setItems(FXCollections.observableArrayList(listaDeJuegos));

			Button guardarButton = new Button("Guardar");

			// Acción al hacer clic en el botón "Guardar" del formulario de creación de
			// evento
			guardarButton.setOnAction(saveEvent -> {
				// Obtener los datos ingresados en los campos para crear un nuevo EventoModel
				EventoModel nuevoEvento = new EventoModel();
				nuevoEvento.setTitulo(tituloField.getText());
				nuevoEvento.setDescripcion(descripcionField.getText());
				nuevoEvento.setFechaInicio(java.sql.Date.valueOf(fechaInicioPicker.getValue()));
				nuevoEvento.setFechaFin(java.sql.Date.valueOf(fechaFinPicker.getValue()));
				nuevoEvento.setVideojuego(juegosComboBox.getValue()); // Asignar el juego seleccionado

				// Puedes obtener el ID del usuario administrador de alguna manera y asignarlo
				// al evento
				Long idUsuarioAdmin = AS.getUsuarioConectado().getId(); // Esto depende de cómo obtienes el ID del
																		// usuario admin
				UsuarioModel admin = new UsuarioModel();
				admin.setId(idUsuarioAdmin);
				nuevoEvento.setCreador(admin);

				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/evento/crear",
						nuevoEvento, String.class);

				// Verificar la respuesta y actualizar la interfaz en consecuencia
				if (response.getStatusCode() == HttpStatus.OK) {
					// Si la creación del evento es exitosa, puedes realizar alguna acción
					System.out.println("Evento creado con éxito");
					crearEventoStage.close(); // Cerrar la ventana emergente después de crear el evento
				} else {
					// Manejar la respuesta en caso de error
					System.out.println("Error al crear el evento");
				}
			});

			// Agregar los campos al contenedor
			crearEventoRoot.getChildren().addAll(tituloField, descripcionField, fechaInicioPicker, fechaFinPicker,
					juegosComboBox, guardarButton);

			Scene crearEventoScene = new Scene(crearEventoRoot, 300, 200);
			crearEventoStage.setScene(crearEventoScene);
			crearEventoStage.show();
		});

		// Acción al hacer clic en el botón "Borrar"
		borrarButton.setOnAction(event -> {
			EventoModel eventoSeleccionado = listView.getSelectionModel().getSelectedItem();
			if (eventoSeleccionado != null) {
				RestTemplate restTemplate = new RestTemplate();

				// Enviar solicitud DELETE al backend para eliminar el evento seleccionado
				String borrarURL = "http://localhost:8080/evento/borrar/" + eventoSeleccionado.getId();
				ResponseEntity<String> response = restTemplate.exchange(borrarURL, HttpMethod.DELETE, null,
						String.class);

				// Verificar la respuesta y actualizar la interfaz en consecuencia
				if (response.getStatusCode() == HttpStatus.OK) {
					// Si la eliminación es exitosa, actualiza la lista de eventos en la interfaz
					eventos.remove(eventoSeleccionado);
				} else {
					// Manejar la respuesta en caso de error
					System.out.println("Error al borrar el evento");
				}
			}
		});

		// ... (código posterior)

		Scene scene = new Scene(root, 400, 300);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Cargar eventos desde el servidor al iniciar la aplicación
		cargarEventosDesdeAPI();
		return root;
	}

	private List<JuegoModel> obtenerJuegosDesdeLaBaseDeDatos() {
		ResponseEntity<List<JuegoModel>> responseEntity = restTemplate.exchange(
	            "http://localhost:8080/juegos/allgames",
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<List<JuegoModel>>() {}
	    );

	    if (responseEntity.getStatusCode() == HttpStatus.OK) {
	        return responseEntity.getBody();
	    } else {
	        // Maneja el caso en que no se pudo obtener la lista de juegos
	        return null; // o lanza una excepción
	    }
	}

	// Método para cargar eventos desde el servidor y agregarlos a la lista
	private void cargarEventosDesdeAPI() {
		ResponseEntity<List<EventoModel>> response = restTemplate.exchange(
				"http://localhost:8080/evento/obtener-eventos", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<EventoModel>>() {
				});

		if (response.getStatusCode() == HttpStatus.OK) {
			eventos.addAll(response.getBody());
		}
	}

	// Define la celda para mostrar cada evento en la lista
	private class EventoCell extends ListCell<EventoModel> {
		private final Button apuntarseButton;
		private final Button borrarButton;

		public EventoCell(Button apuntarseButton, Button borrarButton) {
			this.apuntarseButton = apuntarseButton;
			this.borrarButton = borrarButton;
		}

		@Override
		protected void updateItem(EventoModel evento, boolean empty) {
			super.updateItem(evento, empty);

			if (empty || evento == null) {
				setText(null);
				setGraphic(null);
			} else {
				// Crea una estructura visual para mostrar los detalles del evento
				VBox vbox = new VBox();
				Label tituloLabel = new Label(evento.getTitulo());
				// Mostrar detalles como descripción, fecha de inicio y fin aquí
				vbox.getChildren().addAll(tituloLabel);

				// Si el usuario es administrador, muestra el botón para borrar
				if (esAdmin) {
					vbox.getChildren().add(borrarButton);
				}
				vbox.getChildren().add(apuntarseButton);

				setGraphic(vbox);
			}
		}
	}

}
