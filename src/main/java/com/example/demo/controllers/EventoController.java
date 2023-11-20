package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Date;
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

    @PostMapping("/crear")
    public ResponseEntity<String> crearEvento(@RequestBody EventoModel evento, @RequestParam Long usuarioId) {
        Optional<UsuarioModel> usuario = usuarioService.obtenerPorId(usuarioId);
        if (usuario.isPresent() && usuario.get().isAdmin()) {
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

    @PostMapping("/apuntarse")
    public ResponseEntity<String> apuntarseEvento(@RequestParam Long eventoId, @RequestParam Long usuarioId) {
        Optional<EventoModel> evento = eventoService.obtenerPorId(eventoId);
        Optional<UsuarioModel> usuario = usuarioService.obtenerPorId(usuarioId);
        if (evento.isPresent() && usuario.isPresent()) {
            // Lógica para añadir al usuario al evento (ejemplo)
            // evento.get().addUsuarioInscrito(usuario.get());
            // eventoService.guardarEvento(evento.get());
            return new ResponseEntity<>("Te has apuntado al evento", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo realizar la inscripción", HttpStatus.BAD_REQUEST);
        }
    }

    // Otras funciones de consulta, obtención de eventos, etc.
}
