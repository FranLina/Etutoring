package com.flb.etutoring.services.Impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flb.etutoring.models.Ccaa;
import com.flb.etutoring.services.CcaaService;

@Service
public class CcaaServviceImpl implements CcaaService {

    @Value("${url.wsetutoring.rest.service}")
    String urlWSetutoring;

    @Autowired
    RestTemplate restTemplate;

    private Ccaa[] ccaa;

    @Override
    public List<Ccaa> findAll() {
        ccaa = restTemplate.getForObject(urlWSetutoring + "ccaa", Ccaa[].class);
        List<Ccaa> comunidades = Arrays.asList(ccaa);
        return comunidades;
    }

    @Override
    public Ccaa findById(int id) {
        Ccaa comunidad = restTemplate.getForObject(urlWSetutoring + "ccaa/{id}", Ccaa.class, id);
        return comunidad;
    }

}
