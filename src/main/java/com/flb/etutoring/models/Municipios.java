package com.flb.etutoring.models;

public class Municipios {

    private int idMunicipio;
    private Provincias provincia;
    private int codMunicipio;
    private int dc;
    private String municipio;

    public Municipios(int idMunicipio, Provincias provincia, int codMunicipio, int dc, String municipio) {
        this.idMunicipio = idMunicipio;
        this.provincia = provincia;
        this.codMunicipio = codMunicipio;
        this.dc = dc;
        this.municipio = municipio;
    }

    public Municipios(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Municipios() {
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Provincias getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincias provincia) {
        this.provincia = provincia;
    }

    public int getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public int getDc() {
        return dc;
    }

    public void setDc(int dc) {
        this.dc = dc;
    }

    public String getMunicipio() {
        return this.municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

}
