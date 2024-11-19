package com.mycompany.tiendita;

import java.io.*;
import java.util.ArrayList;

public class InicializarCatalogo {
    public static void main(String[] args) {
        // Lista para almacenar los productos
        ArrayList<Producto> catalogo = new ArrayList<>();

        // Agregar productos al catálogo
        catalogo.add(new Producto("P001", "Laptop Dell Inspiron", 10, 15000));
        catalogo.add(new Producto("P002", "Smartphone Samsung Galaxy", 15, 12000));
        catalogo.add(new Producto("P003", "Audífonos Bluetooth Sony", 30, 2500));
        catalogo.add(new Producto("P004", "Tablet Apple iPad", 8, 20000));
        catalogo.add(new Producto("P005", "Monitor LG Ultrawide", 5, 9000));
        catalogo.add(new Producto("P006", "Teclado Mecánico Corsair", 20, 3500));
        catalogo.add(new Producto("P007", "Mouse Gaming Logitech", 25, 1500));
        catalogo.add(new Producto("P008", "Disco Duro Externo 1TB", 18, 2000));
        catalogo.add(new Producto("P009", "Smartwatch Garmin Vivoactive", 12, 7000));
        catalogo.add(new Producto("P010", "Cámara Digital Canon", 6, 25000));
        catalogo.add(new Producto("P011", "Bocina Portátil JBL", 22, 3000));
        catalogo.add(new Producto("P012", "Router WiFi TP-Link", 35, 1200));
        catalogo.add(new Producto("P013", "Impresora Multifuncional HP", 10, 5000));
        catalogo.add(new Producto("P014", "Memoria USB 128GB", 40, 600));
        catalogo.add(new Producto("P015", "Cargador Rápido USB-C", 50, 800));
        catalogo.add(new Producto("P016", "Proyector Epson", 4, 18000));
        catalogo.add(new Producto("P017", "Smart TV Samsung 55\"", 7, 25000));
        catalogo.add(new Producto("P018", "Cámara de Seguridad Hikvision", 15, 4000));
        catalogo.add(new Producto("P019", "Consola de Videojuegos PlayStation 5", 5, 14000));
        catalogo.add(new Producto("P020", "Batería Externa 20000mAh", 30, 2500));

        // Guardar el catálogo en un archivo serializado
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("catalogo.dat"))) {
            oos.writeObject(catalogo);
            System.out.println("Catálogo inicializado con 20 productos.");
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
