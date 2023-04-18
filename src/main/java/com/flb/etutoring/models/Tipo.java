package com.flb.etutoring.models;

import java.util.List;

public class Tipo {

    private int id;
    private String nombre;
    private List<Usuario> usuarios;

    private boolean perteneceUsuario;

    public Tipo() {
    }

    public Tipo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tipo other = (Tipo) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public boolean isPerteneceUsuario() {
        return perteneceUsuario;
    }

    public void setPerteneceUsuario(boolean perteneceUsuario) {
        this.perteneceUsuario = perteneceUsuario;
    }

}
