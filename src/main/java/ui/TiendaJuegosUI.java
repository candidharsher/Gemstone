package ui;

import com.example.demo.models.JuegoModel;
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

public class TiendaJuegosUI extends Application {

    private ObservableList<JuegoModel> juegos = FXCollections.observableArrayList();
    private boolean esAdmin = false; // Variable para controlar si el usuario es administrador

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tienda de Juegos");

        VBox root = new VBox();
        root.setSpacing(10);

        ListView<JuegoModel> listView = new ListView<>();
        listView.setItems(juegos);

        Button comprarButton = new Button("Comprar");
        Button crearNuevoButton = new Button("Crear Nuevo Juego");

        listView.setCellFactory(param -> new JuegoCell(comprarButton));

        // Si es administrador, muestra el botón para crear nuevo juego
        if (esAdmin) {
            root.getChildren().add(crearNuevoButton);
        }

        root.getChildren().addAll(listView);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
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

                // Si el usuario es administrador, muestra el botón para borrar
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
        // Lógica para obtener juegos de la base de datos y agregarlos a la lista 'juegos'
    }

    public static void main(String[] args) {
        launch(args);
    }
}
