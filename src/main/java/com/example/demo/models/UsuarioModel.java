package com.example.demo.models;

import java.util.ArrayList;

import javax.persistence.*;

@Entity
@Table(name = "usuarios")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = true)
    private String email;
    @Column(nullable = false)
    private boolean admin;
    @Column(nullable = true)
    private ArrayList<JuegoModel> juegos_en_propiedad;
    @Column(nullable = false)
    private String password;
    @Transient
    private String authToken;
    
    public UsuarioModel() {
    	
    }
    public UsuarioModel(String username, String email, boolean admin,
			String password) {
		super();
		this.username = username;
		this.email = email;
		this.admin = admin;
		this.password = password;
		this.authToken=null;
		this.juegos_en_propiedad=null;
	}

	public UsuarioModel(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		this.authToken=null;
		this.juegos_en_propiedad=null;
		this.email="demo@defaultaddress.com";
		this.admin = false;
		
	}


	public void setPrioridad(boolean admin){
        this.admin = admin;
    }

    public boolean getAdmin(){
        return this.admin;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return this.username;
    }

    public void setUser(String nombre) {
        this.username = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthToken(String authToken2) {
		// TODO Auto-generated method stub
		this.authToken=authToken2;
	}

	public String getAuthToken() {
		// TODO Auto-generated method stub
		return this.authToken;
	}
    
}