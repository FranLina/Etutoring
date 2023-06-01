package com.flb.etutoring.services;

import java.util.List;

import com.flb.etutoring.models.Ccaa;
import com.flb.etutoring.models.Provincias;

public interface ProvinciasService {
    public List<Provincias> findAll();

    public Provincias findById(int id);

    public List<Provincias> findByCcaa(Ccaa ccaa);
}
