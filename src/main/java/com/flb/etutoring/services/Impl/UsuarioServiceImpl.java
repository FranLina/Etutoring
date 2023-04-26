package com.flb.etutoring.services.Impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Value("${url.wsetutoring.rest.service}")
    String urlWSetutoring;

    @Autowired
    RestTemplate restTemplate;

    private Usuario[] usu;

    @Override
    public List<Usuario> findAll() {
        usu = restTemplate.getForObject(urlWSetutoring + "usuarios", Usuario[].class);
        List<Usuario> usuarios = Arrays.asList(usu);
        return usuarios;
    }

    @Override
    public Usuario findById(int id) {
        Usuario u = restTemplate.getForObject(urlWSetutoring + "usuarios/{id}", Usuario.class, id);
        return u;
    }

    @Override
    public List<Usuario> findByMateria(int materia_id) {
        usu = restTemplate.getForObject(urlWSetutoring + "usuarios/materia/{materia_id}", Usuario[].class, materia_id);
        List<Usuario> usuarios = Arrays.asList(usu);
        return usuarios;
    }

    @Override
    public Usuario getByUsername(String username) {
        Usuario u = restTemplate.getForObject(urlWSetutoring + "usuarios/buscar/{username}", Usuario.class, username);
        return u;
    }

    @Override
    public Usuario save(Usuario usuario) {
        Usuario u = restTemplate.postForObject(urlWSetutoring + "usuarios", usuario, Usuario.class);
        usuario.setId(u.getId());
        return u;
    }

    @Override
    public void update(Usuario usuario) {
        restTemplate.put(urlWSetutoring + "usuarios/{id}", usuario, usuario.getId());
    }

    @Override
    public void deleteById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
