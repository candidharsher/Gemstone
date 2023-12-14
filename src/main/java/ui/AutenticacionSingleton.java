package ui;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.models.UsuarioModel;

public class AutenticacionSingleton {
	private String authToken;
	private UsuarioModel usuarioConectado;
	private static final AutenticacionSingleton instance = new AutenticacionSingleton();

	private AutenticacionSingleton() {
		// Inicializa el token de autenticación u otras variables si es necesario.
	}

	public static AutenticacionSingleton getInstance() {
		return instance;
	}

	public UsuarioModel getUsuarioConectado() {
		return usuarioConectado;
	}

	public void setUsuarioConectado(UsuarioModel usuarioConectado) {
		this.usuarioConectado = usuarioConectado;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	// Agrega métodos adicionales según tus necesidades, como para cerrar sesión,
	// etc.
	// Método para enviar solicitudes de amistad
	public void enviarSolicitudAmistad(String nombreUsuario) {
		// Lógica para enviar la solicitud de amistad a 'nombreUsuario'
	}

	// Método para aceptar solicitudes de amistad
	public void aceptarSolicitudAmistad(String nombreUsuario) {
		// Lógica para aceptar la solicitud de amistad de 'nombreUsuario'
	}

	public UsuarioModel obtenerUsuarioConectado() {
		// Realiza una solicitud al backend para obtener los datos del usuario basados
		// en el token
		// Utiliza el authToken para realizar una solicitud al backend y obtener los
		// datos del usuario

		// Por ejemplo:
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<UsuarioModel> responseUsuario = restTemplate.exchange("http://localhost:8080/usuario/perfil/", // Reemplaza
																														// con
																														// la
																														// URL
																														// correcta
																														// para
																														// obtener
																														// el
																														// perfil
																														// del
																														// usuario
				HttpMethod.GET, entity, UsuarioModel.class);

		// Verificar si la solicitud fue exitosa y devolver el usuario
		if (responseUsuario.getStatusCode().equals(HttpStatus.OK)) {
			return responseUsuario.getBody();
		} else {
			// Manejar el caso en que no se pudo obtener el perfil del usuario
			System.out.println("Error al obtener el perfil del usuario");
			return null;
		}
	}
}
