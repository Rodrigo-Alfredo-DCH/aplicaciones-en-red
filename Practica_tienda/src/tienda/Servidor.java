package tienda;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;

public class Servidor {

    public static void main(String[] args) {
        int puerto = 6040;

        try {
            ServerSocket serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor iniciado en el puerto " + puerto);

            while (true) {
                // Esperar conexión de cliente
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clienteSocket.getInetAddress());

                // Cargar catálogo desde archivo serializado
                ArrayList<Producto> catalogo = cargarCatalogo();

                // Enviar catálogo serializado al cliente
                ObjectOutputStream oos = new ObjectOutputStream(clienteSocket.getOutputStream());
                oos.writeObject(catalogo);
                oos.flush();

                // Recibir imágenes de productos del cliente
                DataInputStream dis = new DataInputStream(clienteSocket.getInputStream());
                String imagenNombre = dis.readUTF();
                long tam = dis.readLong();
                File imagen = new File("servidor_imagenes/" + imagenNombre);
                try (FileOutputStream fos = new FileOutputStream(imagen)) {
                    byte[] buffer = new byte[1024];
                    long recibidos = 0;
                    int n;
                    while (recibidos < tam) {
                        n = dis.read(buffer);
                        fos.write(buffer, 0, n);
                        recibidos += n;
                    }
                }
                System.out.println("Imagen recibida: " + imagenNombre);

                // Cerrar conexión con el cliente
                clienteSocket.close();
                System.out.println("Cliente desconectado. Listo para la siguiente conexión.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para cargar el catálogo desde un archivo serializado
    private static ArrayList<Producto> cargarCatalogo() {
        ArrayList<Producto> catalogo = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("catalogo.dat"))) {
            catalogo = (ArrayList<Producto>) ois.readObject();
        } catch (Exception e) {
            System.out.println("No se pudo cargar el catálogo, inicializando uno nuevo.");
            catalogo.add(new Producto("001", "Laptop", 10, 15000));
            catalogo.add(new Producto("002", "Mouse", 50, 500));
            catalogo.add(new Producto("003", "Teclado", 20, 1000));
        }
        return catalogo;
    }
}

// Clase Producto para manejar el catálogo
class Producto implements Serializable {
    private String id;
    private String nombre;
    private int stock;
    private double precio;

    public Producto(String id, String nombre, int stock, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
    }

    // Getters y setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public int getStock() { return stock; }
    public double getPrecio() { return precio; }
    public void setStock(int stock) { this.stock = stock; }
}
