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

import com.flb.etutoring.models.Calendario;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.CalendarioService;

@Service
public class CalendarioServiceImpl implements CalendarioService {

    @Value("${url.wsetutoring.rest.service}")
    String urlWSetutoring;

    @Autowired
    RestTemplate restTemplate;

    private Calendario[] cal;

    @Override
    public List<Calendario> findAll() {
        cal = restTemplate.getForObject(urlWSetutoring + "calendarios", Calendario[].class);
        List<Calendario> calendarios = Arrays.asList(cal);
        return calendarios;
    }

    @Override
    public List<Calendario> findByProfesor(Usuario profesor) {
        cal = restTemplate.getForObject(urlWSetutoring + "calendarios/profesor/{id}", Calendario[].class,
                profesor.getId());
        List<Calendario> calendarios = Arrays.asList(cal);
        return calendarios;
    }

    @Override
    public List<Calendario> findByProfesorAndFecha(Usuario profesor, Date fecha) {
        LocalDate diaSeleccionado = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        cal = restTemplate.getForObject(urlWSetutoring + "calendarios/reservar/{id}/{year}/{month}/{day}",
                Calendario[].class,
                profesor.getId(), diaSeleccionado.getYear(), diaSeleccionado.getMonthValue(),
                diaSeleccionado.getDayOfMonth());

        List<Calendario> calendarios = Arrays.asList(cal);
        return calendarios;
    }

    @Override
    public Calendario findByFechaAndHorariosAndProfesor(Date fecha, String horarios, Usuario profesor) {

        LocalDate diaSeleccionado = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Calendario c = restTemplate.getForObject(
                urlWSetutoring + "calendarios/cancelar/{year}/{month}/{day}/{horarios}/{id}",
                Calendario.class, diaSeleccionado.getYear(), diaSeleccionado.getMonthValue(),
                diaSeleccionado.getDayOfMonth(), horarios, profesor.getId());
        return c;
    }

    @Override
    public Calendario findById(int id) {
        Calendario c = restTemplate.getForObject(urlWSetutoring + "calendarios/{id}", Calendario.class, id);
        return c;
    }

    @Override
    public Calendario save(Calendario calendario) {
        Calendario c = restTemplate.postForObject(urlWSetutoring + "calendarios", calendario, Calendario.class);
        calendario.setId(c.getId());
        return c;
    }

    @Override
    public void update(Calendario calendario) {
        restTemplate.put(urlWSetutoring + "calendarios/{id}", calendario, calendario.getId());
    }

    @Override
    public void deleteById(int id) {
        restTemplate.delete(urlWSetutoring + "calendarios/{id}", id);
    }

}
