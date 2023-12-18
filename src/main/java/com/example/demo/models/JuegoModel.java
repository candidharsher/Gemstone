package com.example.demo.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuarios_juegos", // Nombre de la tabla intermedia
			inverseJoinColumns = @JoinColumn(name = "usuario_id"), // Columna del usuario
			joinColumns = @JoinColumn(name = "juego_id") // Columna del juego
	)
	private Set<JuegoModel> usuarios_que_han_comprado = new HashSet<>();

	public JuegoModel() {
		// Constructor vacío
	}

	public JuegoModel(String nombre, String descripcion) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = 0;
		// Inicializa otros atributos si los tienes
	}

	public JuegoModel(String nombre, String descripcion, double precio) {
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
