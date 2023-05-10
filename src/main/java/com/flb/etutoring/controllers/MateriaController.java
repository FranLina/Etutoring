package com.flb.etutoring.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.flb.etutoring.models.Materia;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.MateriaService;
import com.flb.etutoring.services.UsuarioService;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN')")
@RequestMapping("materias")
public class MateriaController {

    @Autowired
    UsuarioService uService;

    @Autowired
    MateriaService mService;

    @GetMapping(value = "/list")
    public ModelAndView list(Model model) {
        List<Materia> materias = mService.findAll();

        ModelAndView modelAndView = new ModelAndView("materias/list");
        modelAndView.addObject("materias", materias);

        return modelAndView;
    }

    @RequestMapping(value = { "/modificar" })
    public ModelAndView edit(@RequestParam(name = "id", required = true) int id) {
        ModelAndView modelAndView = new ModelAndView();

        Materia ma = mService.findById(id);

        modelAndView.addObject("materia", ma);
        modelAndView.setViewName("materias/edit");
        return modelAndView;
    }

    @PostMapping(value = "/update")
    public ModelAndView update(Materia materia) {

        ModelAndView modelAndView = new ModelAndView();
        mService.update(materia);

        modelAndView.setViewName("redirect:/materias/list");
        return modelAndView;
    }

    @RequestMapping(value = { "/nuevo" })
    public ModelAndView nuevo() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("materias/new");
        return modelAndView;
    }

    @PostMapping(value = "/save")
    public ModelAndView save(Materia materia) {

        ModelAndView modelAndView = new ModelAndView();
        mService.save(materia);

        modelAndView.setViewName("redirect:/materias/list");
        return modelAndView;
    }

    @RequestMapping(value = "/borrar")
    public ModelAndView delete(@RequestParam(name = "id", required = true) int id) {
        List<Usuario> usuariosMateria = uService.findByMateria(id);
        for (Usuario usuario : usuariosMateria) {
            usuario.setMateria(null);
            uService.update(usuario);
        }
        mService.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:/materias/list");
        return modelAndView;
    }
}
