package com.flb.etutoring.services;

import java.util.List;

import com.flb.etutoring.models.Usuario;

public interface UsuarioService {
    public List<Usuario> findAll();

    public Usuario findById(int id);

    public Usuario getByUsername(String username);

    public Usuario save(Usuario usuario);

    public void update(Usuario usuario);

    public void deleteById(int id);
}
