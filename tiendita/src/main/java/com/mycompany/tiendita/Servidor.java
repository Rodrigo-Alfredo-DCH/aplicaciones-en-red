package com.mycompany.tiendita;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Servidor {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6040)) {
            System.out.println("Servidor iniciado en el puerto 6040...");

            // Cargar catálogo desde archivo serializado
            ArrayList<Producto> catalogo = cargarCatalogo("catalogo.dat");
            System.out.println("Catálogo cargado con éxito.");

            while (true) {
                System.out.println("Esperando conexión...");
                try (Socket cliente = serverSocket.accept()) {
                    System.out.println("Cliente conectado desde " + cliente.getInetAddress());

                    // Enviar catálogo al cliente
                    ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
                    oos.writeObject(catalogo);
                    oos.flush();

                    // Actualizar catálogo después de la compra
                    ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
                    catalogo = (ArrayList<Producto>) ois.readObject();
                    guardarCatalogo(catalogo, "catalogo.dat");
                    System.out.println("Catálogo actualizado.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Producto> cargarCatalogo(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (ArrayList<Producto>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error al cargar el catálogo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void guardarCatalogo(ArrayList<Producto> catalogo, String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(catalogo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

