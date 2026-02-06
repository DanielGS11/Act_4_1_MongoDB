package org.example.mongodb_proyecto.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "clientes")
public class Cliente {
    @Id
    private String id;

    private String nombre;

    @Indexed(unique = true)
    private String email;
    private Date fecha_registro;

    public Cliente(String nombre, String email, Date fecha_registro) {
        this.nombre = nombre;
        this.email = email;
        this.fecha_registro = fecha_registro;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }
}
