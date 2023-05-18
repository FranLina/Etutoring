package com.flb.etutoring.utils;

import com.flb.etutoring.models.Usuario;

public class CustomObject {

    private Usuario profesor;
    private Double puntuacion;

    public CustomObject(Usuario profesor, Double puntuacion) {
        this.profesor = profesor;
        this.puntuacion = puntuacion;
    }

    public Usuario getProfesor() {
        return profesor;
    }

    public void setProfesor(Usuario profesor) {
        this.profesor = profesor;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }

}
