package com.flb.etutoring.services.Impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flb.etutoring.models.Direccion;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.DireccionService;

@Service
public class DireccionServiceImpl implements DireccionService {

    @Value("${url.wsetutoring.rest.service}")
    String urlWSetutoring;

    @Autowired
    RestTemplate restTemplate;

    private Direccion[] dir;

    @Override
    public List<Direccion> findAll() {
        dir = restTemplate.getForObject(urlWSetutoring + "direcciones", Direccion[].class);
        List<Direccion> direcciones = Arrays.asList(dir);
        return direcciones;
    }

    @Override
    public List<Direccion> findByUsuario(Usuario usuario) {
        dir = restTemplate.getForObject(urlWSetutoring + "direcciones/usuario/{id}", Direccion[].class,
                usuario.getId());
        List<Direccion> direcciones = Arrays.asList(dir);
        return direcciones;
    }

    @Override
    public Direccion findById(int id) {
        Direccion d = restTemplate.getForObject(urlWSetutoring + "direcciones/{id}", Direccion.class, id);
        return d;
    }

    @Override
    public Direccion save(Direccion direccion) {
        Direccion dir = restTemplate.postForObject(urlWSetutoring + "direcciones", direccion, Direccion.class);
        direccion.setId(dir.getId());
        return dir;
    }

    @Override
    public void update(Direccion direccion) {
        restTemplate.put(urlWSetutoring + "direcciones/{id}", direccion, direccion.getId());
    }

    @Override
    public void deleteById(int id) {
        restTemplate.delete(urlWSetutoring + "direcciones/{id}", id);
    }
}
