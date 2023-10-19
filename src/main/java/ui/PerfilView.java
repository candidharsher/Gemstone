package ui;

import com.example.demo.models.UsuarioModel;

import javafx.scene.Scene;
import javafx.scene.control.Label;
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

        // Crea un nuevo escenario (Stage) para la vista del perfil
        perfilStage.setTitle("Perfil de Usuario");

        // Configura la escena con el diseño del perfil y ábrela en el nuevo escenario
        Scene perfilScene = new Scene(perfilLayout, 300, 200); // Ajusta el tamaño según tus preferencias
        perfilStage.setScene(perfilScene);

        // Muestra el escenario de la vista del perfil
        perfilStage.show();
    }
}

