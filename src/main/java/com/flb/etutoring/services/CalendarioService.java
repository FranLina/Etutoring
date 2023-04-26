package com.flb.etutoring.services;

import java.util.List;

import com.flb.etutoring.models.Calendario;
import com.flb.etutoring.models.Usuario;

public interface CalendarioService {
    public List<Calendario> findAll();

    public Calendario findById(int id);

    public List<Calendario> findByProfesor(Usuario usuario);

    public Calendario save(Calendario calendario);

    public void update(Calendario calendario);

    public void deleteById(int id);
}
