package com.flb.etutoring.services.Impl;

import java.util.Arrays;
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
    public Calendario findById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Calendario save(Calendario calendario) {
        Calendario c = restTemplate.postForObject(urlWSetutoring + "calendarios", calendario, Calendario.class);
        calendario.setId(c.getId());
        return c;
    }

    @Override
    public void update(Calendario calendario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void deleteById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
