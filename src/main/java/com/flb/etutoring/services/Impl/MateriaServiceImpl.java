package com.flb.etutoring.services.Impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flb.etutoring.models.Materia;
import com.flb.etutoring.services.MateriaService;

@Service
public class MateriaServiceImpl implements MateriaService {

    @Value("${url.wsetutoring.rest.service}")
    String urlWSetutoring;

    @Autowired
    RestTemplate restTemplate;

    private Materia[] mat;

    @Override
    public List<Materia> findAll() {
        mat = restTemplate.getForObject(urlWSetutoring + "materias", Materia[].class);
        List<Materia> materias = Arrays.asList(mat);
        return materias;
    }

    @Override
    public Materia findById(int id) {
        Materia m = restTemplate.getForObject(urlWSetutoring + "materias/{id}", Materia.class, id);
        return m;
    }

    @Override
    public Materia save(Materia materia) {
        Materia m = restTemplate.postForObject(urlWSetutoring + "materias", materia, Materia.class);
        materia.setId(m.getId());
        return m;
    }

    @Override
    public void update(Materia materia) {
        restTemplate.put(urlWSetutoring + "materias/{id}", materia, materia.getId());
    }

    @Override
    public void deleteById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
