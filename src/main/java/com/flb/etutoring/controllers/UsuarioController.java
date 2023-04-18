package com.flb.etutoring.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.flb.etutoring.models.Materia;
import com.flb.etutoring.models.Tipo;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.DireccionService;
import com.flb.etutoring.services.MateriaService;
import com.flb.etutoring.services.TipoService;
import com.flb.etutoring.services.UsuarioService;

@Controller
@RequestMapping("usuarios")
public class UsuarioController {
    @Autowired
    UsuarioService uService;

    @Autowired
    MateriaService mService;

    @Autowired
    TipoService tService;

    @Autowired
    DireccionService dService;

    @GetMapping(value = "/list")
    public ModelAndView list(Model model) {
        List<Usuario> usuarios = uService.findAll();

        for (Usuario usuario : usuarios) {
            usuario.setDirecciones(dService.findByUsuario(usuario));
        }

        ModelAndView modelAndView = new ModelAndView("usuarios/list");
        modelAndView.addObject("usuarios", usuarios);
        return modelAndView;
    }

    @RequestMapping(value = { "/modificar" })
    public ModelAndView edit(@RequestParam(name = "id", required = true) int id) {
        ModelAndView modelAndView = new ModelAndView();

        Usuario u = uService.findById(id);
        u.setDirecciones(dService.findByUsuario(u));

        List<Tipo> tipos = tService.findAll();
        for (Tipo t : tipos) {
            if (u.getTipo().contains(t)) {
                t.setPerteneceUsuario(true);
            }
        }

        modelAndView.addObject("usuario", u);
        modelAndView.addObject("materias", mService.findAll());
        modelAndView.addObject("tipos", tipos);
        modelAndView.setViewName("usuarios/edit");
        return modelAndView;
    }

    @PostMapping(value = "/update")
    public ModelAndView update(Usuario usuario, @RequestParam(value = "ck_tipos") int[] ck_tipos,
            @RequestParam(value = "materia_profe") int materia_id, @RequestParam("file") MultipartFile imagen)
            throws IOException {

        ModelAndView modelAndView = new ModelAndView();

        Usuario u = uService.findById(usuario.getId());
        usuario.setUsername(u.getUsername());
        usuario.setPassword(u.getPassword());
        usuario.setFotoPerfil(imagen.getBytes());

        List<Tipo> tipos = usuario.getTipo();
        if (tipos == null) {
            tipos = new ArrayList<Tipo>();
        }

        for (int i : ck_tipos) {
            Tipo t = new Tipo(i);
            tipos.add(t);
        }

        usuario.setTipo(tipos);
        if (materia_id != 0) {
            Materia m = new Materia(materia_id);
            usuario.setMateria(m);
        }
        
        uService.update(usuario);

        modelAndView.setViewName("redirect:modificar?id=" + usuario.getId());
        return modelAndView;
    }
}
