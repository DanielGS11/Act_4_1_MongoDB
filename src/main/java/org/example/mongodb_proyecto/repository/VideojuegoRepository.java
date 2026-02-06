package org.example.mongodb_proyecto.repository;

import org.example.mongodb_proyecto.model.Videojuego;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VideojuegoRepository extends MongoRepository<Videojuego, String> {
    List<Videojuego> findByTitulo(String titulo);
}
