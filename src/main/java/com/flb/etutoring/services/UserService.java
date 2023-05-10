package com.flb.etutoring.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.flb.etutoring.models.Tipo;
import com.flb.etutoring.models.Usuario;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UsuarioService uService;

    @Autowired
    HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = uService.getByUsername(username);
        List<Tipo> tipos = u.getTipo();

        session.setAttribute("usuario_id", u.getId());
        
        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();

        for (Tipo tipo : tipos) {
            roles.add(new SimpleGrantedAuthority(tipo.getNombre()));
        }

        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPassword())
                .authorities(roles)
                .build();
        return user;
    }

}
