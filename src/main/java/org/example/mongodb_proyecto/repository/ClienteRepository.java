package org.example.mongodb_proyecto.repository;

import org.example.mongodb_proyecto.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClienteRepository extends MongoRepository<Cliente, String> {
    Cliente findByEmail(String email);
}
