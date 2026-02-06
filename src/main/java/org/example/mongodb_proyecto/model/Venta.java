package org.example.mongodb_proyecto.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;

@Document(collection = "ventas")
public class Venta {
    @Id
    private String id;

    private Date fecha;
    private String cliente_id;
    private String juego_id;
    private String titulo_snapshot;
    private double precio_snapshot;

    public Venta(Date fecha, String cliente_id, String juego_id, String titulo_snapshot, double precio_snapshot) {
        this.fecha = fecha;
        this.cliente_id = cliente_id;
        this.juego_id = juego_id;
        this.titulo_snapshot = titulo_snapshot;
        this.precio_snapshot = precio_snapshot;
    }

    public String getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getJuego_id() {
        return juego_id;
    }

    public void setJuego_id(String juego_id) {
        this.juego_id = juego_id;
    }

    public String getTitulo_snapshot() {
        return titulo_snapshot;
    }

    public void setTitulo_snapshot(String titulo_snapshot) {
        this.titulo_snapshot = titulo_snapshot;
    }

    public double getPrecio_snapshot() {
        return precio_snapshot;
    }

    public void setPrecio_snapshot(double precio_snapshot) {
        this.precio_snapshot = precio_snapshot;
    }
}
