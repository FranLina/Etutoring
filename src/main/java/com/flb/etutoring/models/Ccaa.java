package com.flb.etutoring.models;

public class Ccaa {
    private int idCCAA;
    private String nombre;

    public Ccaa() {
    }

    public Ccaa(int idCCAA) {
        this.idCCAA = idCCAA;
    }

    public Ccaa(int idCCAA, String nombre) {
        this.idCCAA = idCCAA;
        this.nombre = nombre;
    }

    public int getIdCCAA() {
        return idCCAA;
    }

    public void setIdCCAA(int idCCAA) {
        this.idCCAA = idCCAA;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
