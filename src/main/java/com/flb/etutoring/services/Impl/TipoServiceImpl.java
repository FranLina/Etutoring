package com.flb.etutoring.services.Impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flb.etutoring.models.Tipo;
import com.flb.etutoring.services.TipoService;

@Service
public class TipoServiceImpl implements TipoService {

    @Value("${url.wsetutoring.rest.service}")
    String urlWSetutoring;

    @Autowired
    RestTemplate restTemplate;

    private Tipo[] ti;

    @Override
    public List<Tipo> findAll() {
        ti = restTemplate.getForObject(urlWSetutoring + "tipos", Tipo[].class);
        List<Tipo> tipos = Arrays.asList(ti);
        return tipos;
    }

    @Override
    public Tipo findById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Tipo save(Tipo tipo) {
        Tipo t = restTemplate.postForObject(urlWSetutoring + "tipos", tipo, Tipo.class);
        tipo.setId(t.getId());
        return t;
    }

    @Override
    public void update(Tipo tipo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void deleteById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
