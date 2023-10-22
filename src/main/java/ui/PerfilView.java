package ui;

import com.example.demo.models.UsuarioModel;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PerfilView {
	UsuarioModel usuario;
    public PerfilView(UsuarioModel usuario) {
		this.usuario=usuario;
	}

	public void mostrarPerfil(Stage perfilStage) {
        // Crea un diseño para la vista del perfil
        VBox perfilLayout = new VBox();
        perfilLayout.setSpacing(10);

        // Crea etiquetas para mostrar los datos del perfil
        Label usernameLabel = new Label("Nombre de usuario: " + usuario.getUser());
        Label emailLabel = new Label("Email: " + usuario.getEmail());

        // Agrega las etiquetas al diseño
        perfilLayout.getChildren().addAll(usernameLabel, emailLabel);
     // Verifica si el usuario es un administrador y muestra una imagen si es el caso
        if (usuario.isAdmin()) {
            // Crea un nodo de imagen y configura la imagen que deseas mostrar
            Image adminImage = new Image(getClass().getClassLoader().getResourceAsStream("31692344.jpg")); // reemplazar con ruta que tu quieras. esto es en local pero se puede web url.
            ImageView imageView = new ImageView(adminImage);

            // Agrega la imagen al diseño
            perfilLayout.getChildren().add(imageView);
        }

        // Crea un nuevo escenario (Stage) para la vista del perfil
        perfilStage.setTitle("Perfil de Usuario");

        // Configura la escena con el diseño del perfil y ábrela en el nuevo escenario
        Scene perfilScene = new Scene(perfilLayout, 300, 200); // Ajusta el tamaño según tus preferencias
        perfilStage.setScene(perfilScene);

        // Muestra el escenario de la vista del perfil
        perfilStage.show();
    }
}

