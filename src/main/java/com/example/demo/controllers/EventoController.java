package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.demo.models.EventoModel;
import com.example.demo.models.UsuarioModel;
import com.example.demo.services.EventoService;
import com.example.demo.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evento")
public class EventoController {

	@Autowired
	EventoService eventoService;

	@Autowired
	UsuarioService usuarioService;

	// Endpoint para obtener la lista de eventos
	@GetMapping("/obtener-eventos")
	public ResponseEntity<List<EventoModel>> obtenerEventos() {
		List<EventoModel> eventos = eventoService.obtenerTodosLosEventos();
		return new ResponseEntity<>(eventos, HttpStatus.OK);
	}

	@PostMapping("/crear")
	public ResponseEntity<String> crearEvento(@RequestBody EventoModel evento) {
		long usuarioId = evento.getCreador().getId();
		Optional<UsuarioModel> usuario = usuarioService.obtenerPorId(usuarioId);
		if (usuario.isPresent() && usuario.get().isAdmin()) {
			evento.setCreador(usuario.get()); // Asignar el usuario creador al evento

			EventoModel nuevoEvento = eventoService.guardarEvento(evento);

			if (nuevoEvento != null) {
				return new ResponseEntity<>("Evento creado con éxito", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Error al crear el evento", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("No tienes permiso para crear eventos", HttpStatus.UNAUTHORIZED);
		}
	}

	@DeleteMapping("/borrar/{id}")
	public ResponseEntity<String> borrarEvento(@PathVariable Long id) {
		boolean eliminado = eventoService.eliminarEvento(id);
		if (eliminado) {
			return new ResponseEntity<>("Evento eliminado con éxito", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("No se pudo eliminar el evento", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/apuntarse/{eventoId}/{usuarioId}")
	public ResponseEntity<String> apuntarseEvento(@PathVariable Long eventoId, @PathVariable Long usuarioId) {
		Optional<EventoModel> evento = eventoService.obtenerPorId(eventoId);
		Optional<UsuarioModel> usuario = usuarioService.obtenerPorId(usuarioId);
		if (evento.isPresent() && usuario.isPresent()) {
			// Lógica para inscribir al usuario al evento
			usuario.get().getEventosInscritos().add(evento.get());
			// Aquí puedes implementar la lógica para agregar al usuario al evento en tu
			// aplicación
			// Por ejemplo:
			evento.get().getUsuariosInscritos().add(usuario.get());
			eventoService.guardarEvento(evento.get());
			usuarioService.guardarUsuario(usuario.get());
			return new ResponseEntity<>("Te has apuntado al evento", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("No se pudo realizar la inscripción", HttpStatus.BAD_REQUEST);
		}
	}

	// Otras funciones de consulta, obtención de eventos, etc.
}
