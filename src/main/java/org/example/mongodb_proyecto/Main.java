package org.example.mongodb_proyecto;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.Instant;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://user:userpassword@localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("tienda_gaming");
            GestorMongoDB gestorMongoDB = new GestorMongoDB(database);

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("""
                        TIENDA DE JUEGOS EN MONGODB
                        - 1) Cargar datos
                        - 2) Dar de alta un cliente o videojuego
                        - 3) Vender un juego a un cliente
                        - 4) Ver el historial de ventas de un cliente
                        - 5) Ver las ofertas de videojuegos que hay
                        - 6) salir
                        
                        Introduzca el NUMERO de la opcion que desee: """);

                int opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1:
                        gestorMongoDB.cargarDatos();
                        break;

                    case 2:
                        System.out.print("""
                                - 1) Dar de alta un videojuego
                                - 2) Dar de alta un cliente
                                
                                Introduzca el NUMERO de la opcion que desee: """);

                        opc = Integer.parseInt(sc.nextLine());

                        switch (opc) {
                            case 1:
                                System.out.println("Vamos a dar de alta un juego\n" +
                                        "Introduzca los datos que se vayan solicitando");

                                System.out.print("Titulo del juego: ");
                                String titulo = sc.nextLine();

                                System.out.print("Genero: ");
                                String genero = sc.nextLine();

                                System.out.print("Precio: ");
                                double precio = Double.parseDouble(sc.nextLine());

                                System.out.print("Stock: ");
                                int stock = Integer.parseInt(sc.nextLine());

                                gestorMongoDB.darAlta(opc, new Document("titulo", titulo).append("genero", genero)
                                        .append("precio", precio).append("stock", stock));
                                break;

                            case 2:
                                System.out.println("Vamos a dar de alta un cliente\n" +
                                        "Introduzca los datos que se vayan solicitando");

                                System.out.print("Nombre del cliente: ");
                                String nombre = sc.nextLine();

                                System.out.print("email: ");
                                String email = sc.nextLine();

                                gestorMongoDB.darAlta(opc, new Document("nombre", nombre).append("email", email)
                                        .append("fecha_registro", Date.from(Instant.now())));
                                break;

                            default:
                                System.out.println("Opcion no valida");
                                break;
                        }
                        break;

                    case 3:
                        System.out.println("Vamos a realizar una venta, Introduzca los siguientes datos");
                        System.out.print("Email del cliente: ");
                        String email = sc.nextLine();

                        System.out.print("Titulo del juego: ");
                        String titulo = sc.nextLine();

                        gestorMongoDB.procesarVenta(email, titulo);
                        break;

                    case 4:
                        System.out.print("Introduzca el email del cliente: ");
                        gestorMongoDB.historialCliente(sc.nextLine());
                        break;

                    case 5:
                        gestorMongoDB.consultarOfertas();
                        break;

                    case 6:
                        System.out.println("¡Hasta la proxima!");
                        return;
                    default:
                        System.out.println("Por favor, ingrese un numero valido");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
