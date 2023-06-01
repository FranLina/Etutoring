package com.flb.etutoring.services;

import java.util.List;

import com.flb.etutoring.models.Municipios;
import com.flb.etutoring.models.Provincias;

public interface MunicipiosService {
    public List<Municipios> findAll();

    public Municipios findById(int id);

    public List<Municipios> findByProvincia(Provincias provincia);
}
