package com.mycompany.tiendita;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Introduce la IP del servidor:");
            String host = scanner.nextLine();
            System.out.println("Introduce el puerto:");
            int puerto = Integer.parseInt(scanner.nextLine());

            try (Socket socket = new Socket(host, puerto)) {
                System.out.println("Conectado al servidor.");

                // Recibir catálogo
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ArrayList<Producto> catalogo = (ArrayList<Producto>) ois.readObject();
                System.out.println("Catálogo recibido:");
                mostrarCatalogo(catalogo);

                // Carrito de compras
                ArrayList<Producto> carrito = new ArrayList<>();
                boolean seguirComprando = true;

                while (seguirComprando) {
                    System.out.println("1. Agregar producto al carrito");
                    System.out.println("2. Consultar carrito");
                    System.out.println("3. Finalizar compra");
                    System.out.println("4. Salir");
                    int opcion = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer

                    switch (opcion) {
                        case 1:
                            agregarAlCarrito(catalogo, carrito, scanner);
                            break;
                        case 2:
                            mostrarCarrito(carrito);
                            break;
                        case 3:
                            finalizarCompra(carrito, catalogo, socket);
                            seguirComprando = false;
                            break;
                        case 4:
                            seguirComprando = false;
                            break;
                        default:
                            System.out.println("Opción no válida.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mostrarCatalogo(ArrayList<Producto> catalogo) {
        System.out.println("Código\tNombre\tStock\tPrecio");
        for (Producto p : catalogo) {
            System.out.printf("%s\t%s\t%d\t%.2f\n", p.getCodigo(), p.getNombre(), p.getStock(), p.getPrecio());
        }
    }

    private static void agregarAlCarrito(ArrayList<Producto> catalogo, ArrayList<Producto> carrito, Scanner scanner) {
        System.out.println("Introduce el código del producto:");
        String codigo = scanner.nextLine();
        System.out.println("Cantidad:");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        for (Producto p : catalogo) {
            if (p.getCodigo().equals(codigo) && p.getStock() >= cantidad) {
                carrito.add(new Producto(p.getCodigo(), p.getNombre(), cantidad, p.getPrecio()));
                p.setStock(p.getStock() - cantidad);
                System.out.println("Producto agregado al carrito.");
                return;
            }
        }
        System.out.println("Producto no encontrado o cantidad insuficiente.");
    }

    private static void mostrarCarrito(ArrayList<Producto> carrito) {
        System.out.println("Productos en el carrito:");
        System.out.println("Código\tNombre\tCantidad\tPrecio");
        for (Producto p : carrito) {
            System.out.printf("%s\t%s\t%d\t%.2f\n", p.getCodigo(), p.getNombre(), p.getStock(), p.getPrecio());
        }
    }

    private static void finalizarCompra(ArrayList<Producto> carrito, ArrayList<Producto> catalogo, Socket socket) throws IOException {
        double total = 0;
        StringBuilder recibo = new StringBuilder();
        recibo.append("Recibo de compra\n");
        recibo.append("Fecha y hora: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        recibo.append("Productos:\n");
        recibo.append("Código\tNombre\tCantidad\tPrecio\n");

        for (Producto p : carrito) {
            recibo.append(String.format("%s\t%s\t%d\t%.2f\n", p.getCodigo(), p.getNombre(), p.getStock(), p.getPrecio()));
            total += p.getStock() * p.getPrecio();
        }
        recibo.append("Total a pagar: ").append(total).append("\n");

        try (PrintWriter writer = new PrintWriter(new FileWriter("recibo.txt"))) {
            writer.print(recibo.toString());
        }

        System.out.println("Compra finalizada. Recibo generado como 'recibo.txt'.");

        // Enviar catálogo actualizado al servidor
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(catalogo);
        oos.flush();
    }
}
