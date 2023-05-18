package com.flb.etutoring.services;

import java.util.List;

import com.flb.etutoring.models.Valoracion;

public interface ValoracionService {
    
    public List<Valoracion> findAll();

    public Valoracion findById(int id);

    public Valoracion save(Valoracion valoracion);

    public void update(Valoracion valoracion);

    public void deleteById(int id);
}
