package com.flb.etutoring.services.Impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flb.etutoring.models.Ccaa;
import com.flb.etutoring.models.Provincias;
import com.flb.etutoring.services.ProvinciasService;

@Service
public class ProvinciasServiceImpl implements ProvinciasService {

    @Value("${url.wsetutoring.rest.service}")
    String urlWSetutoring;

    @Autowired
    RestTemplate restTemplate;

    private Provincias[] pro;

    @Override
    public List<Provincias> findAll() {
        pro = restTemplate.getForObject(urlWSetutoring + "provincias", Provincias[].class);
        List<Provincias> provincias = Arrays.asList(pro);
        return provincias;
    }

    @Override
    public Provincias findById(int id) {
        Provincias provincia = restTemplate.getForObject(urlWSetutoring + "provincias/{id}", Provincias.class, id);
        return provincia;
    }

    @Override
    public List<Provincias> findByCcaa(Ccaa ccaa) {
        pro = restTemplate.getForObject(urlWSetutoring + "provincias/ccaa/{id}", Provincias[].class,
                ccaa.getIdCCAA());
        List<Provincias> provincias = Arrays.asList(pro);
        return provincias;
    }

}
