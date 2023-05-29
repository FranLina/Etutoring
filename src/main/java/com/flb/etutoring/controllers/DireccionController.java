package com.flb.etutoring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.flb.etutoring.models.Direccion;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.DireccionService;

@Controller
@RequestMapping("direcciones")
public class DireccionController {

    @Autowired
    DireccionService dService;

    @RequestMapping(value = { "/modificar" })
    public ModelAndView edit(@RequestParam(name = "id", required = true) int id) {
        ModelAndView modelAndView = new ModelAndView();

        Direccion dir = dService.findById(id);

        modelAndView.addObject("direccion", dir);
        modelAndView.setViewName("direcciones/edit");
        return modelAndView;
    }

    @PostMapping(value = "/update")
    public ModelAndView update(Direccion direccion) {
        ModelAndView modelAndView = new ModelAndView();
        String err = "0";
        try {
            Direccion dir = dService.findById(direccion.getId());
            direccion.setUsuario(new Usuario(dir.getUsuario().getId()));
            dService.update(direccion);
            err = "4";
        } catch (Exception e) {
            err = "3";
        }
        modelAndView.addObject("err", err);
        modelAndView.setViewName("redirect:/usuarios/modificar?id=" + direccion.getUsuario().getId());
        return modelAndView;
    }

    @RequestMapping(value = { "/nuevo" })
    public ModelAndView nuevo(@RequestParam(name = "id", required = true) int usuario_id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("usuario_id", usuario_id);
        modelAndView.setViewName("direcciones/new");
        return modelAndView;
    }

    @PostMapping(value = "/save")
    public ModelAndView save(Direccion direccion, @RequestParam(value = "id_usuario") int id_usuario) {

        ModelAndView modelAndView = new ModelAndView();
        String err = "0";
        Direccion dir = new Direccion();
        try {
            direccion.setUsuario(new Usuario(id_usuario));
            dir = dService.save(direccion);
            err = "4";
        } catch (Exception e) {
            err = "3";
        }
        modelAndView.addObject("err", err);
        modelAndView.setViewName("redirect:/usuarios/modificar?id=" + dir.getUsuario().getId());
        return modelAndView;
    }

    @RequestMapping(value = "/borrar")
    public ModelAndView delete(@RequestParam(name = "id", required = true) int id) {
        Direccion dir = dService.findById(id);

        dService.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:/usuarios/modificar?id=" + dir.getUsuario().getId());
        return modelAndView;
    }

}
