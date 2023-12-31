package com.example.demo.controllers;

import com.example.demo.models.JuegoModel;
import com.example.demo.models.UsuarioModel;
import com.example.demo.services.JuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.UsuarioService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/juegos")
public class JuegoController {

	private final JuegoService juegoService;
	private final UsuarioService usuarioService;

	@Autowired
	public JuegoController(JuegoService juegoService, UsuarioService usuarioService) {
		this.juegoService = juegoService;
		this.usuarioService = usuarioService;
	}

	// Obtener todos los juegos
	@GetMapping("/allgames")
	public ResponseEntity<List<JuegoModel>> obtenerTodosLosJuegos() {
		List<JuegoModel> juegos = juegoService.obtenerTodosLosJuegos();
		return new ResponseEntity<>(juegos, HttpStatus.OK);
	}

	// Obtener un juego por su ID
	@GetMapping("/{id}")
	public ResponseEntity<JuegoModel> obtenerJuegoPorId(@PathVariable Long id) {
		JuegoModel juego = juegoService.obtenerJuegoPorId(id);
		return new ResponseEntity<>(juego, HttpStatus.OK);
	}

	// Crear un nuevo juego
	@PostMapping
	public ResponseEntity<JuegoModel> crearJuego(@RequestBody JuegoModel juego) {
		JuegoModel nuevoJuego = juegoService.crearJuego(juego);
		return new ResponseEntity<>(nuevoJuego, HttpStatus.CREATED);
	}

	// Actualizar un juego existente
	@PutMapping("/{id}")
	public ResponseEntity<JuegoModel> actualizarJuego(@PathVariable Long id, @RequestBody JuegoModel juego) {
		JuegoModel juegoActualizado = juegoService.actualizarJuego(id, juego);
		return new ResponseEntity<>(juegoActualizado, HttpStatus.OK);
	}

	// Borrar un juego
	@DeleteMapping("/{id}")
	public ResponseEntity<?> borrarJuego(@PathVariable Long id) {
		juegoService.borrarJuego(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/agregar-juego/{usuarioId}")
	public ResponseEntity<String> agregarJuegoAUsuario(@PathVariable Long usuarioId, @RequestBody JuegoModel juego) {
		Optional<UsuarioModel> usuarioOptional = usuarioService.obtenerPorId(usuarioId);
		if (usuarioOptional.isPresent()) {
			UsuarioModel usuario = usuarioOptional.get();
			usuario.agregarJuegoEnPropiedad(juego); // Utiliza el método para agregar el juego al usuario
			usuarioService.guardarUsuario(usuario);
			return new ResponseEntity<>("Juego agregado al usuario con éxito", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		}
	}

}
