package com.flb.etutoring.services.Impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flb.etutoring.models.Clase;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.ClaseService;

@Service
public class ClaseServiceImpl implements ClaseService {

    @Value("${url.wsetutoring.rest.service}")
    String urlWSetutoring;

    @Autowired
    RestTemplate restTemplate;

    private Clase[] cla;

    @Override
    public List<Clase> findAll() {
        cla = restTemplate.getForObject(urlWSetutoring + "clases", Clase[].class);
        List<Clase> clases = Arrays.asList(cla);
        return clases;
    }

    @Override
    public List<Clase> findByALumno(Usuario alumno) {
        cla = restTemplate.getForObject(urlWSetutoring + "clases/usuario/{id}", Clase[].class,
                alumno.getId());
        List<Clase> clases = Arrays.asList(cla);
        return clases;
    }

    @Override
    public List<Clase> findByProfesor(Usuario profesor) {
        cla = restTemplate.getForObject(urlWSetutoring + "clases/profesor/{id}", Clase[].class,
                profesor.getId());
        List<Clase> clases = Arrays.asList(cla);
        return clases;
    }

    @Override
    public List<Clase> findByProfesorAndValoracionNotNull(Usuario profesor) {
        cla = restTemplate.getForObject(urlWSetutoring + "clases/profesorValNotNull/{id}", Clase[].class,
                profesor.getId());
        List<Clase> clases = Arrays.asList(cla);
        return clases;
    }

    @Override
    public Clase findByFechaAndHorariosAndProfesor(Date fecha, String horarios, Usuario profesor) {
        LocalDate diaSeleccionado = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Clase c = restTemplate.getForObject(
                urlWSetutoring + "clases/cancelar/{year}/{month}/{day}/{horarios}/{id}",
                Clase.class, diaSeleccionado.getYear(), diaSeleccionado.getMonthValue(),
                diaSeleccionado.getDayOfMonth(), horarios, profesor.getId());
        return c;
    }

    @Override
    public Clase findByFechaAndHorariosAndAlumno(Date fecha, String horarios, Usuario alumno) {
        LocalDate diaSeleccionado = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Clase c = restTemplate.getForObject(
                urlWSetutoring + "clases/comprobar/{year}/{month}/{day}/{horarios}/{id}",
                Clase.class, diaSeleccionado.getYear(), diaSeleccionado.getMonthValue(),
                diaSeleccionado.getDayOfMonth(), horarios, alumno.getId());
        return c;
    }

    @Override
    public Clase findById(int id) {
        Clase c = restTemplate.getForObject(urlWSetutoring + "clases/{id}", Clase.class, id);
        return c;
    }

    @Override
    public Clase save(Clase clase) {
        Clase c = restTemplate.postForObject(urlWSetutoring + "clases", clase, Clase.class);
        clase.setId(c.getId());
        return c;
    }

    @Override
    public void update(Clase clase) {
        restTemplate.put(urlWSetutoring + "clases/{id}", clase, clase.getId());
    }

    @Override
    public void deleteById(int id) {
        restTemplate.delete(urlWSetutoring + "clases/{id}", id);
    }

    @Override
    public List<Clase> findByProfesorAndFecha(Usuario profesor, Date fecha) {
        LocalDate diaSeleccionado = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        cla = restTemplate.getForObject(urlWSetutoring + "clases/profesor/{id}/{year}/{month}/{day}",
                Clase[].class,
                profesor.getId(), diaSeleccionado.getYear(), diaSeleccionado.getMonthValue(),
                diaSeleccionado.getDayOfMonth());

        List<Clase> calendarios = Arrays.asList(cla);
        return calendarios;
    }

}
