package com.flb.etutoring.services.Impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flb.etutoring.models.Valoracion;
import com.flb.etutoring.services.ValoracionService;

@Service
public class ValoracionServiceImpl implements ValoracionService {

    @Value("${url.wsetutoring.rest.service}")
    String urlWSetutoring;

    @Autowired
    RestTemplate restTemplate;

    private Valoracion[] val;

    @Override
    public List<Valoracion> findAll() {
        val = restTemplate.getForObject(urlWSetutoring + "valoraciones", Valoracion[].class);
        List<Valoracion> valoraciones = Arrays.asList(val);
        return valoraciones;
    }

    @Override
    public Valoracion findById(int id) {
        Valoracion v = restTemplate.getForObject(urlWSetutoring + "valoraciones/{id}", Valoracion.class, id);
        return v;
    }

    @Override
    public Valoracion save(Valoracion valoracion) {
        Valoracion v = restTemplate.postForObject(urlWSetutoring + "valoraciones", valoracion, Valoracion.class);
        valoracion.setId(v.getId());
        return v;
    }

    @Override
    public void update(Valoracion valoracion) {
        restTemplate.put(urlWSetutoring + "valoraciones/{id}", valoracion, valoracion.getId());
    }

    @Override
    public void deleteById(int id) {
        restTemplate.delete(urlWSetutoring + "valoraciones/{id}", id);
    }

}
