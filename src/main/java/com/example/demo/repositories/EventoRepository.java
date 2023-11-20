package com.example.demo.repositories;

import java.util.ArrayList;
import java.util.Optional;

import com.example.demo.models.EventoModel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends CrudRepository<EventoModel, Long> {
    // Aquí puedes agregar métodos personalizados de consulta si los necesitas
    // Por ejemplo:
    // public abstract ArrayList<EventoModel> findByFechaInicioAfter(Date fecha);
    // public abstract Optional<EventoModel> findByCreadorAndId(UsuarioModel creador, Long id);
    // ...otros métodos de consulta
}
