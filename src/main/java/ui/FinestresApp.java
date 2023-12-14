package ui;

import com.example.demo.models.UsuarioModel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FinestresApp extends Application {

	private TabPane tabPane;
	private PerfilView perfilView;
	private ListaEventosUI listaEventosUI;
	private TiendaJuegosUI tiendaJuegosUI;
	private UsuarioModel usuario;
	private BibliotecaJuegosUI biblioteca;
	private UsuariosUI usuarios;

	public FinestresApp(UsuarioModel usuario) {
		this.usuario = usuario;
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("PIXELVENTURE");

		tabPane = new TabPane();
		listaEventosUI = new ListaEventosUI();
		tiendaJuegosUI = new TiendaJuegosUI(); // Inicializa tiendaJuegosUI
		biblioteca = new BibliotecaJuegosUI(); // Inicializa biblioteca
		usuarios = new UsuariosUI(); // Inicializa usuarios
		// Crear las pestañas de perfil y eventos
		BorderPane perfilPane = new BorderPane();

		VBox tiendaPane = new VBox();
		tiendaJuegosUI.start(primaryStage);
		Tab tabTienda = new Tab("Tienda");
		tabTienda.setContent(tiendaJuegosUI.start(primaryStage));

		VBox biblioPane = new VBox();
		biblioteca.start(primaryStage);
		Tab tabBiblio = new Tab("Biblioteca");
		tabBiblio.setContent(biblioteca.start(primaryStage));

		VBox eventosPane = new VBox();
		listaEventosUI.start(primaryStage);
		Tab tabEventos = new Tab("Eventos");
		tabEventos.setContent(listaEventosUI.start(primaryStage));

		perfilView = new PerfilView();
		perfilView.setUsuario(usuario);
		perfilView.mostrarPerfil(primaryStage);
		Tab tabPerfil = new Tab("Perfil");
		tabPerfil.setContent(perfilView.mostrarPerfil(primaryStage));

		VBox usuariosPane = new VBox();
		usuarios.start(primaryStage);
		Tab usuariosTab = new Tab("Usuarios");
		usuariosTab.setContent(usuarios.start(primaryStage));

		// Agregar las pestañas de perfil y eventos al TabPane
		tabPane.getTabs().addAll(tabTienda, tabBiblio, tabEventos, usuariosTab, tabPerfil);

		// Seleccionar la pestaña de perfil después del inicio de sesión
		tabPane.getSelectionModel().select(tabPerfil);
		Scene scene = new Scene(tabPane, 800, 600);
		primaryStage.setScene(scene);

	}

	// Resto del código...
}
