package com.flb.etutoring.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Valoracion {
    private int id;
    private Clase claseValorada;
    private Usuario usuarioValorador;
    private String comentario;
    private int puntuacion;
    private Date fechaValoracion;

    public Date getFechaValoracion() {
        return fechaValoracion;
    }

    public void setFechaValoracion(Date fechaValoracion) {
        this.fechaValoracion = fechaValoracion;
    }

    public long getDias() {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaV = fechaValoracion.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(fechaV, hoy);
    }

    public Valoracion() {
    }

    public Valoracion(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Clase getClaseValorada() {
        return claseValorada;
    }

    public void setClaseValorada(Clase claseValorada) {
        this.claseValorada = claseValorada;
    }

    public Usuario getUsuarioValorador() {
        return usuarioValorador;
    }

    public void setUsuarioValorador(Usuario usuarioValorador) {
        this.usuarioValorador = usuarioValorador;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
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
        Valoracion other = (Valoracion) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
