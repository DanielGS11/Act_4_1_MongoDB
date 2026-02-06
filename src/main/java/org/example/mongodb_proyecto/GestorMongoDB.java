package org.example.mongodb_proyecto;

import org.example.mongodb_proyecto.model.Cliente;
import org.example.mongodb_proyecto.model.Videojuego;
import org.example.mongodb_proyecto.repository.ClienteRepository;
import org.example.mongodb_proyecto.repository.VentaRepository;
import org.example.mongodb_proyecto.repository.VideojuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class GestorMongoDB {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VideojuegoRepository videojuegoRepository;

    public void cargarDatos() {
        ventaRepository.deleteAll();
        clienteRepository.deleteAll();
        videojuegoRepository.deleteAll();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        List<Cliente> clientes = new ArrayList<>();

        try {
            clientes = List.of(
                    new Cliente("Daniel", "Danielin@gmail.com", formatter.parse("11/02/2013")),
                    new Cliente("Lolo", "Lolitogoku@gmail.com", formatter.parse("10/02/2016"))
            );
        } catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        List<Videojuego> juegos = new ArrayList<>(List.of(
                new Videojuego("Pokemon Blanco", "RPG", 30, 10),
                new Videojuego("Zelda: Majora's Mask", "Aventura", 45, 20),
                new Videojuego("Hollow Knight", "Metroidvania", 20, 40),
                new Videojuego("Inazuma Eleven: Victory Road", "Deporte", 70, 5),
                new Videojuego("Monster Hunter: Wilds", "Accion", 70, 60)
        ));

        clienteRepository.saveAll(clientes);
        videojuegoRepository.saveAll(juegos);

        System.out.println("Datos cargados con exito");

        esperarUnSegundo();
    }

    public void darAlta(Videojuego juego, Cliente cliente) {
        if (juego.getPrecio() < 0) {
            System.out.println("El precio del juego no puede ser negativo");
            esperarUnSegundo();
            return;
        }

        if (!cliente.getEmail().matches("\\b[a-zA-Z.-_]+@[a-zA-Z.-_]+\\b")) {
            System.out.println("El formato del email del cliente es incorrecto, debe ser algo como 'ejemplo@ejemplo");
            System.out.println("IMPORTANTE LA @");
            esperarUnSegundo();
            return;
        }
    }

    public void procesarVenta(String emailCliente, String tituloJuego) {
        if (!emailCliente.matches("\\b[a-zA-Z.-_]+@[a-zA-Z.-_]+\\b")) {
            System.out.println("El formato del email del cliente es incorrecto, debe ser algo como 'ejemplo@ejemplo");
            System.out.println("IMPORTANTE LA @");
            esperarUnSegundo();
            return;
        }

        Cliente cliente = clienteRepository.findByEmail(emailCliente);

        if (cliente == null) {
            System.out.println("No existe un cliente con el email: " + emailCliente);
            esperarUnSegundo();
            return;
        }

        List<Videojuego> juegos = videojuegoRepository.findByTitulo(tituloJuego);

        Videojuego videojuego = null;

        if (juegos.isEmpty()) {
            System.out.println("No existen videojuegos con el titulo: " + tituloJuego);
        } else if (juegos.size() > 1) {
            Scanner sc = new Scanner(System.in);

            System.out.println("Se encontraron varios videojuegos: ");

            for (int i = 0; i <= juegos.size(); i++) {
                System.out.printf("- %d) %s\n", i + 1, juegos.get(i));
            }
            System.out.print("Introduzca el numero del juego a seleccionar");
            int opc = Integer.parseInt(sc.nextLine());

            if (opc > juegos.size()) {
                System.out.println("No existe ningun juego en el numero: " + opc);
                esperarUnSegundo();
                return;
            }

            videojuego = juegos.get(opc);
        }

        System.out.println("Juego seleccionado: " + videojuego.getTitulo());

        if (videojuego.getStock() <= 0) {
            System.out.println("No queda stock de ese juego");
            esperarUnSegundo();
            return;
        }

    }

    public void esperarUnSegundo() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
