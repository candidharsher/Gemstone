package com.example.demo.services;

import com.example.demo.models.JuegoModel;
import com.example.demo.repositories.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JuegoService {

    private final JuegoRepository juegoRepository;

    @Autowired
    public JuegoService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    public List<JuegoModel> obtenerTodosLosJuegos() {
        return juegoRepository.findAll();
    }

    public JuegoModel obtenerJuegoPorId(Long id) {
        Optional<JuegoModel> juegoOptional = juegoRepository.findById(id);
        return juegoOptional.orElse(null);
    }

    public JuegoModel crearJuego(JuegoModel juego) {
        return juegoRepository.save(juego);
    }

    public JuegoModel actualizarJuego(Long id, JuegoModel juego) {
        JuegoModel juegoExistente = obtenerJuegoPorId(id);
        if (juegoExistente != null) {
            juego.setId(id);
            return juegoRepository.save(juego);
        }
        return null;
    }

    public void borrarJuego(Long id) {
        juegoRepository.deleteById(id);
    }
}
