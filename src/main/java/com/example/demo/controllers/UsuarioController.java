package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import com.example.demo.models.UsuarioModel;
import com.example.demo.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	private static Set<String> tokensRevocados = new HashSet<>();

    @Autowired
    UsuarioService usuarioService;

    @GetMapping()
    public ArrayList<UsuarioModel> obtenerUsuarios(){
        return usuarioService.obtenerUsuarios();
        
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@RequestBody UsuarioModel usuario) {
        if (usuario == null) {
            return new ResponseEntity<>("Error al registrar el usuario", HttpStatus.BAD_REQUEST);
        }

        // Verifica si el usuario ya existe
        UsuarioModel usuarioRegistrado = usuarioService.registrarUsuario(usuario);

        if (usuarioRegistrado == null) {
            return new ResponseEntity<>("El nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.OK);
        }
    }

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<String> iniciarSesion(@RequestBody UsuarioModel credenciales) {
        // Verifica las credenciales (nombre de usuario y contraseña) en la base de datos.
    	 UsuarioModel usuario = usuarioService.iniciarSesion(credenciales);
    	  if (usuario != null) {
              // Si las credenciales son válidas, genera un token de autenticación.
              String authToken = generarTokenDeAutenticacion();

              // Asigna el token al usuario (guardarlo en la base de datos o en una cookie, según tu elección).
              usuario.setAuthToken(authToken);
              usuarioService.guardarUsuario(usuario); // Actualiza datos, aunque por el momento no ha cambiado nada.

              // Devuelve el token de autenticación al cliente.
              return new ResponseEntity<>(authToken, HttpStatus.OK);
          } else {
              // Si las credenciales no son válidas, devuelve una respuesta de error.
              return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
          }
    }
    public String generarTokenDeAutenticacion() {
        // Define una clave secreta que se utilizará para firmar el token. Esta clave debe ser segura y mantenerse en secreto.
        String claveSecreta = "TuClaveSecreta"; // Reemplaza con tu clave secreta real.

        // Define la duración del token en milisegundos (por ejemplo, 1 dia).
        long duracionToken = 24 * 60 * 60 * 1000; // 1 dia

        // Obtiene la fecha actual para establecer el tiempo de emisión del token.
        long ahora = System.currentTimeMillis();

        // Crea el token JWT con los claims personalizados que desees (puedes incluir información adicional en el token).
        String authToken = Jwts.builder()
                .setIssuedAt(new Date(ahora))
                .setExpiration(new Date(ahora + duracionToken))
                .signWith(SignatureAlgorithm.HS256, claveSecreta)
                .compact();

        return authToken;
    }
    @PostMapping("/cerrar-sesion")
    public ResponseEntity<String> cerrarSesion(@RequestBody UsuarioModel usuario) {
    	// Obtén el token de autenticación del usuario
        String authToken = usuario.getAuthToken();
        if (tokensRevocados.contains(authToken)) {
            return new ResponseEntity<>("La sesión ya está cerrada", HttpStatus.OK);
        }
     // Agrega el token al conjunto de tokens revocados
        tokensRevocados.add(authToken);
        
     // Puedes eliminar el token del usuario si lo deseas (dependiendo de tu implementación).
        usuario.setAuthToken(null);
        // Devuelve una respuesta exitosa.
        return new ResponseEntity<>("Sesión cerrada con éxito", HttpStatus.OK);
    }


    @GetMapping( path = "/{id}")
    public Optional<UsuarioModel> obtenerPorId(@PathVariable("id") Long id) {
        return this.usuarioService.obtenerPorId(id);
    }
    @GetMapping( path = "/username/{username}")
    public @ResponseBody UsuarioModel obtenerPorUsername(@PathVariable("username") String username) {
        return this.usuarioService.obtenerPorUser(username);
    }
    @GetMapping("/query")
    public ArrayList<UsuarioModel> obtenerUsuarioPorAdmin(@RequestParam("admin") boolean admin){
        return this.usuarioService.obtenerPorAdmin(admin);
    }

    @DeleteMapping( path = "/delete/{id}")
    public String eliminarPorId(@PathVariable("id") Long id){
        boolean ok = this.usuarioService.eliminarUsuario(id);
        if (ok){
            return "Se eliminó el usuario con id " + id;
        }else{
            return "No pudo eliminar el usuario con id" + id;
        }
    }

}