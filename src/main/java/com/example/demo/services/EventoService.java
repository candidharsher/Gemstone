package com.example.demo.services;

import java.util.ArrayList;
import java.util.Optional;

import com.example.demo.models.EventoModel;
import com.example.demo.repositories.EventoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventoService {

    @Autowired
    EventoRepository eventoRepository;

    public EventoModel guardarEvento(EventoModel evento) {
        return eventoRepository.save(evento);
    }

    public boolean eliminarEvento(Long id) {
        try {
            eventoRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    public Optional<EventoModel> obtenerPorId(Long id) {
        return eventoRepository.findById(id);
    }

    // Otras funciones de consulta, filtrado, etc.
}

