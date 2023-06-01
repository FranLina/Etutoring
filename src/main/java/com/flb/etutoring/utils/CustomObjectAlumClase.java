package com.flb.etutoring.utils;

import com.flb.etutoring.models.Clase;
import com.flb.etutoring.models.Usuario;

public class CustomObjectAlumClase {

    private Clase clase;
    private Usuario alumno;

    public CustomObjectAlumClase(Clase clase, Usuario alumno) {
        this.clase = clase;
        this.alumno = alumno;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public Usuario getAlumno() {
        return alumno;
    }

    public void setAlumno(Usuario alumno) {
        this.alumno = alumno;
    }

}
