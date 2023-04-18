package com.flb.etutoring.services;

import java.util.List;

import com.flb.etutoring.models.Materia;

public interface MateriaService {
    public List<Materia> findAll();

    public Materia findById(int id);

    public Materia save(Materia materia);

    public void update(Materia materia);

    public void deleteById(int id);
}
