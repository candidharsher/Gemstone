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

    // Otros atributos del juego

    public JuegoModel() {
        // Constructor vacío
    }

    public JuegoModel(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        // Inicializa otros atributos si los tienes
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

    // Otros getters y setters para los atributos del juego

}
