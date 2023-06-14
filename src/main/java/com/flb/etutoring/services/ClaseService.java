package com.flb.etutoring.services;

import java.util.Date;
import java.util.List;

import com.flb.etutoring.models.Clase;
import com.flb.etutoring.models.Usuario;

public interface ClaseService {
    public List<Clase> findAll();

    public List<Clase> findByALumno(Usuario alumno);

    public List<Clase> findByProfesor(Usuario profesor);

    public Clase findByFechaAndHorariosAndProfesor(Date fecha, String horarios, Usuario profesor);

    public Clase findByFechaAndHorariosAndAlumno(Date fecha, String horarios, Usuario alumno);

    public List<Clase> findByProfesorAndFecha(Usuario profesor, Date fecha);

    public Clase findById(int id);

    public Clase save(Clase clase);

    public void update(Clase clase);

    public void deleteById(int id);
}
