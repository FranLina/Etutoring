package com.flb.etutoring.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.flb.etutoring.models.Tipo;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.TipoService;
import com.flb.etutoring.services.UsuarioService;

@Controller
public class LoginController {

    @Autowired
    UsuarioService uService;

    @Autowired
    TipoService tService;

    @Autowired
    PasswordEncoder encoder;

    @RequestMapping("/login")
    public String login() {
        return "../views/login";
    }

    @GetMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @GetMapping(value = "/registry")
    public ModelAndView registry() {
        List<Tipo> tipos = tService.findAll();
        ModelAndView modelAndView = new ModelAndView("../views/registry");
        modelAndView.addObject("tipos", tipos);
        return modelAndView;
    }

    @PostMapping(value = "/saveRegistro")
    public ModelAndView save(Usuario usuario, @RequestParam(value = "ck_tipos") int ck_tipos) {

        ModelAndView modelAndView = new ModelAndView();

        List<Tipo> tipos = new ArrayList<Tipo>();
        tipos.add(new Tipo(ck_tipos));
        usuario.setTipo(tipos);
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        /* Usuario u = */uService.save(usuario);

        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }
}
