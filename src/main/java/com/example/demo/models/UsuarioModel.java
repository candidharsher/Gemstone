package com.example.demo.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import ui.Mensaje;

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
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "amistades",
        joinColumns = @JoinColumn(name = "usuario_id_1"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id_2")
    )
    private Set<UsuarioModel> amigos = new HashSet<>();
    private List<Mensaje> mensajesEnviados;
    private List<Mensaje> mensajesRecibidos;
    
    public List<Mensaje> getMensajesEnviados() {
		return mensajesEnviados;
	}
	public void setMensajesEnviados(List<Mensaje> mensajesEnviados) {
		this.mensajesEnviados = mensajesEnviados;
	}
	public List<Mensaje> getMensajesRecibidos() {
		return mensajesRecibidos;
	}
	public void setMensajesRecibidos(List<Mensaje> mensajesRecibidos) {
		this.mensajesRecibidos = mensajesRecibidos;
	}
	public UsuarioModel() {
		this.mensajesEnviados = new ArrayList<>();
	    this.mensajesRecibidos = new ArrayList<>();
    	
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
		this.mensajesEnviados = new ArrayList<>();
	    this.mensajesRecibidos = new ArrayList<>();
	}

	public UsuarioModel(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		this.authToken=null;
		this.juegos_en_propiedad=null;
		this.email="demo@defaultaddress.com";
		this.admin = false;
		this.mensajesEnviados = new ArrayList<>();
	    this.mensajesRecibidos = new ArrayList<>();
		
	}


	public void setPrioridad(boolean admin){
        this.admin = admin;
    }

    public boolean isAdmin(){
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
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public ArrayList<JuegoModel> getJuegosEnPropiedad() {
		return juegos_en_propiedad;
	}
	public void setJuegosEnPropiedad(ArrayList<JuegoModel> juegos_en_propiedad) {
		this.juegos_en_propiedad = juegos_en_propiedad;
	}
	
    public Set<UsuarioModel> getAmigos() {
        return amigos;
    }

    public void setAmigos(Set<UsuarioModel> amigos) {
        this.amigos = amigos;
    }

    // Método para agregar amigos
    public void agregarAmigo(UsuarioModel amigo) {
        this.amigos.add(amigo);
        amigo.getAmigos().add(this);
    }

    // Método para eliminar amigos
    public void eliminarAmigo(UsuarioModel amigo) {
        this.amigos.remove(amigo);
        amigo.getAmigos().remove(this);
    }
    public void enviarMensaje(UsuarioModel destinatario, String contenido) {
        Mensaje mensaje = new Mensaje(this, destinatario, contenido);
        destinatario.recibirMensaje(mensaje);
        mensajesEnviados.add(mensaje);
    }

    public void recibirMensaje(Mensaje mensaje) {
        mensajesRecibidos.add(mensaje);
    }
}