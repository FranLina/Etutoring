package com.flb.etutoring.services.Impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flb.etutoring.models.Municipios;
import com.flb.etutoring.models.Provincias;
import com.flb.etutoring.services.MunicipiosService;

@Service
public class MunicipiosServiceImpl implements MunicipiosService {
    @Value("${url.wsetutoring.rest.service}")
    String urlWSetutoring;

    @Autowired
    RestTemplate restTemplate;

    private Municipios[] mu;

    @Override
    public List<Municipios> findAll() {
        mu = restTemplate.getForObject(urlWSetutoring + "municipios", Municipios[].class);
        List<Municipios> municipios = Arrays.asList(mu);
        return municipios;
    }

    @Override
    public Municipios findById(int id) {
        Municipios municipio = restTemplate.getForObject(urlWSetutoring + "municipios/{id}", Municipios.class, id);
        return municipio;
    }

    @Override
    public List<Municipios> findByProvincia(Provincias provincia) {
        mu = restTemplate.getForObject(urlWSetutoring + "municipios/provincia/{id}", Municipios[].class,
                provincia.getIdProvincia());
        List<Municipios> municipios = Arrays.asList(mu);
        return municipios;
    }

}
