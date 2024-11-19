package tienda;

//import carrito_servidor.Producto;
import java.io.*;
import java.net.*;
import java.util.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class Cliente {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escriba la dirección IP del servidor:");
        String host = sc.nextLine();
        System.out.println("Escriba el puerto:");
        int puerto = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        try {
            // Conectar al servidor
            Socket socket = new Socket(host, puerto);
            System.out.println("Conectado al servidor.");

            // Recibir catálogo del servidor
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<Producto> catalogo = (ArrayList<Producto>) ois.readObject();

            System.out.println("Catálogo recibido:");
            for (Producto p : catalogo) {
                System.out.println(p.getId() + " - " + p.getNombre() + " - " + p.getStock() + " disponibles - $" + p.getPrecio());
            }

            // Carrito de compras
            ArrayList<Producto> carrito = new ArrayList<>();
            String opcion;
            do {
                System.out.println("\nOpciones:");
                System.out.println("1. Agregar al carrito");
                System.out.println("2. Eliminar del carrito");
                System.out.println("3. Ver carrito");
                System.out.println("4. Comprar");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");
                opcion = sc.nextLine();

                switch (opcion) {
                    case "1": // Agregar producto
                        System.out.print("Ingrese el ID del producto: ");
                        String id = sc.nextLine();
                        Producto producto = buscarProducto(catalogo, id);
                        if (producto != null && producto.getStock() > 0) {
                            System.out.print("Cantidad: ");
                            int cantidad = sc.nextInt();
                            sc.nextLine();
                            if (cantidad <= producto.getStock()) {
                                producto.setStock(producto.getStock() - cantidad);
                                carrito.add(new Producto(producto.getId(), producto.getNombre(), cantidad, producto.getPrecio()));
                                System.out.println("Producto agregado al carrito.");
                            } else {
                                System.out.println("Stock insuficiente.");
                            }
                        } else {
                            System.out.println("Producto no encontrado o sin stock.");
                        }
                        break;

                    case "2": // Eliminar producto
                        System.out.print("Ingrese el ID del producto a eliminar: ");
                        id = sc.nextLine();
                        carrito.removeIf(p -> p.getId().equals(id));
                        System.out.println("Producto eliminado del carrito.");
                        break;

                    case "3": // Ver carrito
                        System.out.println("Carrito de compras:");
                        for (Producto p : carrito) {
                            System.out.println(p.getId() + " - " + p.getNombre() + " - " + p.getStock() + " unidades - $" + p.getPrecio());
                        }
                        break;

                    case "4": // Comprar
                        generarRecibo(carrito);
                        carrito.clear();
                        System.out.println("Compra realizada con éxito.");
                        break;

                    case "5":
                        System.out.println("Saliendo...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            } while (!opcion.equals("5"));

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Buscar producto por ID
    private static Producto buscarProducto(ArrayList<Producto> catalogo, String id) {
        for (Producto p : catalogo) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    // Generar recibo en PDF
    private static void generarRecibo(ArrayList<Producto> carrito) {
        try {
            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream("recibo.pdf"));
            documento.open();
            documento.add(new Paragraph("Recibo de Compra"));
            documento.add(new Paragraph("Fecha: " + new Date()));
            documento.add(new Paragraph(" "));
            double total = 0;
            for (Producto p : carrito) {
                documento.add(new Paragraph(p.getId() + " - " + p.getNombre() + " - " + p.getStock() + " unidades - $" + p.getPrecio()));
                total += p.getStock() * p.getPrecio();
            }
            documento.add(new Paragraph("Total a pagar: $" + total));
            documento.close();
            System.out.println("Recibo generado: recibo.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
