package ui;

import java.time.LocalDateTime;
import java.util.Date;

import com.example.demo.models.UsuarioModel;

public class Mensaje {
	private UsuarioModel remitente;
	private UsuarioModel destinatario;
	private String contenido;
	private LocalDateTime fechaEnvio;

	public Mensaje(UsuarioModel remitente, UsuarioModel destinatario, String contenido, LocalDateTime fechaEnvio) {
		super();
		this.remitente = remitente;
		this.destinatario = destinatario;
		this.contenido = contenido;
		this.fechaEnvio = fechaEnvio;
	}

	public Mensaje(UsuarioModel usuarioModel, UsuarioModel destinatario2, String contenido2) {
		this.remitente = remitente;
		this.destinatario = destinatario;
		this.contenido = contenido;
		this.fechaEnvio = LocalDateTime.now();
	}

	public UsuarioModel getRemitente() {
		return remitente;
	}

	public void setRemitente(UsuarioModel remitente) {
		this.remitente = remitente;
	}

	public UsuarioModel getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(UsuarioModel destinatario) {
		this.destinatario = destinatario;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(LocalDateTime fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	// Constructor, getters y setters

}
