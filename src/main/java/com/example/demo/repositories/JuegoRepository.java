package com.example.demo.repositories;

import com.example.demo.models.JuegoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends JpaRepository<JuegoModel, Long> {
    // Puedes agregar métodos adicionales de consulta si los necesitas
}
