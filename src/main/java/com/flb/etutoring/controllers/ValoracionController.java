package com.flb.etutoring.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.flb.etutoring.models.Clase;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.models.Valoracion;
import com.flb.etutoring.services.UsuarioService;
import com.flb.etutoring.services.ValoracionService;
import com.flb.etutoring.utils.CustomObjectOpciones;
import com.flb.etutoring.utils.CustomObjectValAlum;
import com.flb.etutoring.services.ClaseService;;

@Controller
@RequestMapping("valoraciones")
public class ValoracionController {

    @Autowired
    UsuarioService uService;

    @Autowired
    ValoracionService vService;

    @Autowired
    ClaseService clService;

    @RequestMapping(value = { "/profesor" })
    public ModelAndView edit(@RequestParam(name = "id", required = true) int id,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "0") int sortOrder) {
        ModelAndView modelAndView = new ModelAndView();

        Usuario profesor = uService.findById(id);
        List<Clase> listadoClases = clService.findByProfesor(profesor);
        List<CustomObjectValAlum> combinedList = new ArrayList<>();
        for (Clase c : listadoClases) {
            combinedList.add(new CustomObjectValAlum(c.getValoracion(), c.getAlumno()));
        }

        Comparator<CustomObjectValAlum> comparator = (p1, p2) -> {
            if (sortOrder == 0) {
                // Orden reciente
                return Long.compare(p1.getValoracion().getDias(), p2.getValoracion().getDias());
            } else if (sortOrder == 1) {
                // Orden antiguo
                return Long.compare(p2.getValoracion().getDias(), p1.getValoracion().getDias());
            } else if (sortOrder == 2) {
                // Mayor estrellas
                return Long.compare(p2.getValoracion().getPuntuacion(), p1.getValoracion().getPuntuacion());
            } else {
                // Menor estrellas
                return Long.compare(p1.getValoracion().getPuntuacion(), p2.getValoracion().getPuntuacion());
            }
        };

        // Ordenar la lista utilizando el comparador dinámico

        Collections.sort(combinedList, comparator);

        List<CustomObjectOpciones> opciones = new ArrayList<>();
        opciones.add(new CustomObjectOpciones(0, "Recientes"));
        opciones.add(new CustomObjectOpciones(1, "Antiguas"));
        opciones.add(new CustomObjectOpciones(2, "Positivas"));
        opciones.add(new CustomObjectOpciones(3, "Negativas"));

        modelAndView.addObject("profesor_id", id);
        modelAndView.addObject("sortOrder", sortOrder);
        modelAndView.addObject("opciones", opciones);
        modelAndView.addObject("combinedList", combinedList);
        modelAndView.setViewName("valoraciones/list");
        return modelAndView;
    }
}
