package com.flb.etutoring.services;

import java.util.List;

import com.flb.etutoring.models.Direccion;
import com.flb.etutoring.models.Usuario;

public interface DireccionService {
    public List<Direccion> findAll();

    public List<Direccion> findByUsuario(Usuario usuario);

    public Direccion findById(int id);

    public Direccion save(Direccion direccion);

    public void update(Direccion direccion);

    public void deleteById(int id);
}
