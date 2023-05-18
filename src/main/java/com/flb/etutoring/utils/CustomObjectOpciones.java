package com.flb.etutoring.utils;

public class CustomObjectOpciones {
    private int opcion;
    private String nombre;

    
    public CustomObjectOpciones(int opcion, String nombre) {
        this.opcion = opcion;
        this.nombre = nombre;
    }
    public int getOpcion() {
        return opcion;
    }
    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
}
