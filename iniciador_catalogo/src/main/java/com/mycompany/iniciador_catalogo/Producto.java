
package com.mycompany.iniciador_catalogo;

public class Producto {
    
    private String codigo;
    private String nombre;
    private int stock;
    private double precio;
   
    public Producto(String codigo, String nombre, int stock, int precio){
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
    }
    // Getters y setters
    public String getCodigo() { 
        return codigo;
    }
    public String getNombre() {
        return nombre; 
    }
    public int getStock() { 
        return stock; 
    }
    public void setStock(int stock) { 
        this.stock = stock; 
    }
    public double getPrecio() { 
        return precio; 
    }
}
