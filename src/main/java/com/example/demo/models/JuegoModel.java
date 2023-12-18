package com.example.demo.models;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "juegos")
public class JuegoModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = true)
	private String descripcion;

	@Column(nullable = false)
	private double precio;

	// Otros atributos del juego

	public JuegoModel() {
		// Constructor vacío
	}

	public JuegoModel(String nombre, String descripcion) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = 0;
		// Inicializa otros atributos si los tienes
	}

	public JuegoModel(String nombre, String descripcion, float precio) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "JuegoModel [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio
				+ "]";
	}

	// Otros getters y setters para los atributos del juego

}
