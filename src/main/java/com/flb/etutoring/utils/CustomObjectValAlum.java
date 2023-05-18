package com.flb.etutoring.utils;

import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.models.Valoracion;

public class CustomObjectValAlum {

    private Valoracion valoracion;
    private Usuario alumno;

    public CustomObjectValAlum(Valoracion valoracion, Usuario alumno) {
        this.valoracion = valoracion;
        this.alumno = alumno;
    }

    public Valoracion getValoracion() {
        return valoracion;
    }

    public void setValoracion(Valoracion valoracion) {
        this.valoracion = valoracion;
    }

    public Usuario getAlumno() {
        return alumno;
    }

    public void setAlumno(Usuario alumno) {
        this.alumno = alumno;
    }
}
