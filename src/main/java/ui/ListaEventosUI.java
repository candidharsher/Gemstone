package ui;

import com.example.demo.models.EventoModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListaEventosUI extends Application {

    private ObservableList<EventoModel> eventos = FXCollections.observableArrayList();
    private boolean esAdmin = false; // Variable para controlar si el usuario es administrador

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lista de Eventos");

        VBox root = new VBox();
        root.setSpacing(10);

        ListView<EventoModel> listView = new ListView<>();
        listView.setItems(eventos);

        Button apuntarseButton = new Button("Apuntarse");
        Button borrarButton = new Button("Borrar");
        Button crearNuevoButton = new Button("Crear Nuevo Evento");

        listView.setCellFactory(param -> new EventoCell(apuntarseButton, borrarButton));

        // Si es administrador, muestra el botón para crear nuevo evento
        if (esAdmin) {
            root.getChildren().add(crearNuevoButton);
        }

        root.getChildren().addAll(listView);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
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
                } else {
                    vbox.getChildren().add(apuntarseButton);
                }

                setGraphic(vbox);
            }
        }
    }

    // Método para cargar eventos desde la API REST y agregarlos a la lista
    private void cargarEventosDesdeAPI() {
        // Lógica para obtener eventos de la API REST y agregarlos a la lista 'eventos'
    }

    public static void main(String[] args) {
        launch(args);
    }
}
