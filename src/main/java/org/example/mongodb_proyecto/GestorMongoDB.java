package org.example.mongodb_proyecto;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.inc;

public class GestorMongoDB {
    private MongoDatabase database;
    private final MongoCollection<Document> collectionClientes;
    private final MongoCollection<Document> collectionVideojuegos;
    private final MongoCollection<Document> collectionVentas;

    public GestorMongoDB(MongoDatabase database) {
        this.database = database;

        collectionClientes = database.getCollection("clientes");
        collectionVideojuegos = database.getCollection("videojuegos");
        collectionVentas = database.getCollection("ventas");
    }

    public void cargarDatos() {
        List<MongoCollection<Document>> collections = new ArrayList<>(List.of(
                collectionClientes,
                collectionVideojuegos,
                collectionVentas
        ));

        collections.forEach(MongoCollection::drop);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            collections.getFirst().insertMany(List.of(
                    new Document("nombre", "Daniel").append("email", "Danielin@gmail.com")
                            .append("fecha_registro", formatter.parse("11/02/2013")),
                    new Document("nombre", "Lolo").append("email", "Lolitogoku@gmail.com")
                            .append("fecha_registro", formatter.parse("10/02/2016"))
            ));
        } catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        collections.get(1).insertMany(List.of(
                new Document("titulo", "Pokemon Blanco").append("genero", "RPG")
                        .append("precio", 30).append("stock", 10),
                new Document("titulo", "Zelda: Majora's Mask").append("genero", "Aventura")
                        .append("precio", 45).append("stock", 20),
                new Document("titulo", "Hollow Knight").append("genero", "Metroidvania")
                        .append("precio", 20).append("stock", 40),
                new Document("titulo", "Inazuma Eleven: Victory Road").append("genero", "Deporte")
                        .append("precio", 70).append("stock", 5),
                new Document("titulo", "Monster Hunter: Wilds").append("genero", "Accion")
                        .append("precio", 70).append("stock", 60)

        ));

        System.out.println("Datos cargados con exito");

        esperarUnSegundo();
    }

    public void darAlta(int opc, Document doc) {
        switch (opc) {
            case 1:
                if (doc.getDouble("precio") < 0) {
                    System.out.println("El precio del juego no puede ser negativo");
                    esperarUnSegundo();
                    return;
                }

                collectionVideojuegos.insertOne(new Document("titulo", doc.getString("titulo"))
                        .append("genero", doc.getString("genero")).append("precio", doc.getDouble("precio"))
                        .append("stock", doc.getInteger("stock")));
                System.out.println("Videojuego dado de alta correctamente");
                break;

            case 2:
                if (!doc.getString("email").matches("\\b[a-zA-Z.-_]+@[a-zA-Z.-_]+\\b")) {
                    System.out.println("El formato del email del cliente es incorrecto, debe ser algo como 'ejemplo@ejemplo");
                    System.out.println("IMPORTANTE PONER LA '@' EN EL EMAIL");
                    esperarUnSegundo();
                    return;
                }

                collectionClientes.insertOne(
                        new Document("nombre", doc.getString("nombre")).append("email", doc.getString("email"))
                                .append("fecha_registro", doc.getDate("fecha_registro"))
                );

                System.out.println("Cliente dado de alta correctamente");
                break;

            default:
                System.out.println("Por favor, introduzca una opcion correcta");
                break;
        }
        esperarUnSegundo();
    }

    public void procesarVenta(String emailCliente, String tituloJuego) {
        if (!emailCliente.matches("\\b[a-zA-Z.-_]+@[a-zA-Z.-_]+\\b")) {
            System.out.println("El formato del email del cliente es incorrecto, debe ser algo como 'ejemplo@ejemplo");
            System.out.println("IMPORTANTE PONER LA '@' EN EL EMAIL");
            esperarUnSegundo();
            return;
        }

        Document cliente = collectionClientes.find(eq("email", emailCliente)).first();

        if (cliente == null) {
            throw new NullPointerException("No existe un cliente con el email: " + emailCliente);
        }

        List<Document> juegos = new ArrayList<>();

        collectionVideojuegos.find(eq("titulo", tituloJuego)).forEach(juegos::add);

        Document videojuego = null;

        if (juegos.isEmpty()) {
            System.out.println("No existen videojuegos con el titulo: " + tituloJuego);
            esperarUnSegundo();
            return;

        } else if (juegos.size() > 1) {
            Scanner sc = new Scanner(System.in);

            System.out.println("Se encontraron varios videojuegos: ");

            for (int i = 0; i < juegos.size(); i++) {
                System.out.printf("- %d) %s\n", i + 1, juegos.get(i).get("titulo"));
            }

            System.out.print("Introduzca el numero del juego a seleccionar");
            int opc = Integer.parseInt(sc.nextLine());

            if (opc > juegos.size()) {
                System.out.println("No existe ningun juego en el numero: " + opc);
                esperarUnSegundo();
                return;
            }

            videojuego = juegos.get(opc);
        } else {
            videojuego = juegos.get(0);
        }

        System.out.println("Juego seleccionado: " + videojuego.get("titulo"));

        if (videojuego.getInteger("stock") <= 0) {
            System.out.println("No queda stock de ese juego");
            esperarUnSegundo();
            return;
        }

        collectionVentas.insertOne(new Document("fecha", Date.from(Instant.now()))
                .append("cliente_id", cliente.getObjectId("_id")).append("juego_id", videojuego.getObjectId("_id"))
                .append("titulo_snapshot", videojuego.get("titulo")).append("precio_snapshot", videojuego.get("precio")));

        collectionVideojuegos.findOneAndUpdate(eq("_id", videojuego.getObjectId("_id")),
                inc("stock", -1));

        System.out.println("Venta realizada con exito");
    }

    public void historialCliente(String email) {
        if (!email.matches("\\b[a-zA-Z.-_]+@[a-zA-Z.-_]+\\b")) {
            System.out.println("El formato del email del cliente es incorrecto, debe ser algo como 'ejemplo@ejemplo");
            System.out.println("IMPORTANTE PONER LA '@' EN EL EMAIL");
            esperarUnSegundo();
            return;
        }

        ObjectId idCliente = collectionClientes.find(eq("email", email)).first().getObjectId("_id");

        if (idCliente == null) {
            System.out.println("No existe un cliente con el email: " + email);
            esperarUnSegundo();
            return;
        }

        System.out.println("ID del cliente: " + idCliente);
        System.out.println("Ventas en las que aparece el cliente con el email: " + email);

        List<Document> ventas = new ArrayList<>();
        collectionVentas.find(eq("cliente_id", idCliente)).forEach(ventas::add);

        if (ventas.isEmpty()) {
            System.out.println("Este cliente no aparece en ninguna venta");
        } else {
            ventas.forEach(System.out::println);
        }
    }

    public void consultarOfertas() {
        List<Document> ofertas = new ArrayList<>();

        collectionVideojuegos.find(lt("precio", 25))
                .projection(fields(include("titulo", "precio"), exclude()))
                .forEach(ofertas::add);

        if (ofertas.isEmpty()) {
            System.out.println("No hay ofertas actualmente");
         } else {
            System.out.println("Ofertas Actuales: ");
            ofertas.forEach(System.out::println);
        }
    }

    private void esperarUnSegundo() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
