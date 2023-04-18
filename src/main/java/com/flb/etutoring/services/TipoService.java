package com.flb.etutoring.services;

import java.util.List;

import com.flb.etutoring.models.Tipo;

public interface TipoService {
    public List<Tipo> findAll();

    public Tipo findById(int id);

    public Tipo save(Tipo tipo);

    public void update(Tipo tipo);

    public void deleteById(int id);
}
