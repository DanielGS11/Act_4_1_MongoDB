package org.example.mongodb_proyecto.repository;

import org.example.mongodb_proyecto.model.Venta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VentaRepository extends MongoRepository<Venta, String> {
}
