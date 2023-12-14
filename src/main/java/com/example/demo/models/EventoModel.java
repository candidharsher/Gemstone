package com.example.demo.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "eventos")
public class EventoModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String titulo;

	@Column(nullable = true)
	private String descripcion;

	@Column(nullable = false)
	private Date fechaInicio;

	@Column(nullable = false)
	private Date fechaFin;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "juego_id")
	private JuegoModel juego_id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creador_id")
	private UsuarioModel creador_id;

	public EventoModel() {
		// Constructor vacío
	}

	public EventoModel(String titulo, String descripcion, Date fechaInicio, Date fechaFin, JuegoModel videojuego,
			UsuarioModel creador) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.juego_id = videojuego;
		this.creador_id = creador;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public JuegoModel getVideojuego() {
		return juego_id;
	}

	public void setVideojuego(JuegoModel videojuego) {
		this.juego_id = videojuego;
	}

	public UsuarioModel getCreador() {
		return creador_id;
	}

	public void setCreador(UsuarioModel creador) {
		this.creador_id = creador;
	}
}
