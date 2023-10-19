package com.example.demo.services;

import java.util.ArrayList;
import java.util.Optional;

import com.example.demo.models.UsuarioModel;
import com.example.demo.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;
    
    public ArrayList<UsuarioModel> obtenerUsuarios(){
        return (ArrayList<UsuarioModel>) usuarioRepository.findAll();
    }

    public UsuarioModel guardarUsuario(UsuarioModel usuario){
        return usuarioRepository.save(usuario);
    }

    public Optional<UsuarioModel> obtenerPorId(Long id){
        return usuarioRepository.findById(id);
    }

    public UsuarioModel obtenerPorUser(String username){
        return usuarioRepository.findByUsername(username);
    }
    public ArrayList<UsuarioModel>  obtenerPorAdmin(boolean admin) {
        return usuarioRepository.findByAdmin(admin);
    }
    public UsuarioModel registrarUsuario(UsuarioModel usuario){
    	// Verificar si ya existe un usuario con el mismo nombre de usuario
        UsuarioModel usuarioExistente = usuarioRepository.findByUsername(usuario.getUser());

        if (usuarioExistente != null) {
            // Si ya existe un usuario con el mismo nombre de usuario, devuelve null para indicar un fallo.
            return null;
        }

        // Si no existe un usuario con el mismo nombre de usuario, procede a guardar el nuevo usuario
        return usuarioRepository.save(usuario);
    }
    public boolean eliminarUsuario(Long id) {
        try{
            usuarioRepository.deleteById(id);
            return true;
        }catch(Exception err){
            return false;
        }
    }

    public UsuarioModel iniciarSesion(UsuarioModel credenciales) {
        // Buscar un usuario con el nombre de usuario proporcionado
        UsuarioModel usuario = usuarioRepository.findByUsername(credenciales.getUser());

        // Verificar si se encontró un usuario con el nombre de usuario
        if (usuario != null) {
            // Compara la contraseña proporcionada con la contraseña almacenada en la base de datos
            if (credenciales.getPassword().equals(usuario.getPassword())) {
                // Las credenciales son válidas, devuelve el usuario
                return usuario;
            }
        }

        // Si las credenciales no son válidas o no se encontró un usuario, devuelve null
        return null;
    }


    
}