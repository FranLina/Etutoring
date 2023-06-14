package com.flb.etutoring.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Calendario {
    private int id;
    private Usuario profesor;
    private Date fecha;
    private String horarios;
    private Boolean reservado;

    public Calendario(int id) {
        this.id = id;
    }

    public Calendario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getProfesor() {
        return profesor;
    }

    public void setProfesor(Usuario profesor) {
        this.profesor = profesor;
    }

    public Date getFecha() {
        return fecha;
    }

    public LocalDate getLocalFecha() {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((profesor == null) ? 0 : profesor.hashCode());
        result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
        result = prime * result + ((horarios == null) ? 0 : horarios.hashCode());
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
        Calendario other = (Calendario) obj;
        if (profesor == null) {
            if (other.profesor != null)
                return false;
        } else if (!profesor.equals(other.profesor))
            return false;
        if (fecha == null) {
            if (other.fecha != null)
                return false;
        } else if (!fecha.equals(other.fecha))
            return false;
        if (horarios == null) {
            if (other.horarios != null)
                return false;
        } else if (!horarios.equals(other.horarios))
            return false;
        return true;
    }

    public Boolean getReservado() {
        return reservado;
    }

    public void setReservado(Boolean reservado) {
        this.reservado = reservado;
    }

}
