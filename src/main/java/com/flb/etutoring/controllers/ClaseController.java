package com.flb.etutoring.controllers;

import java.io.Console;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.flb.etutoring.models.Calendario;
import com.flb.etutoring.models.Clase;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.models.Valoracion;
import com.flb.etutoring.services.CalendarioService;
import com.flb.etutoring.services.ClaseService;
import com.flb.etutoring.services.UsuarioService;
import com.flb.etutoring.services.ValoracionService;

@Controller
@RequestMapping("clases")
public class ClaseController {
    @Autowired
    UsuarioService uService;

    @Autowired
    CalendarioService cService;

    @Autowired
    ClaseService clService;

    @Autowired
    ValoracionService vService;

    @GetMapping(value = "/usuario")
    public ModelAndView tabla(@RequestParam(name = "id") int usuario_id,
            @RequestParam(name = "year", defaultValue = "-1") int year,
            @RequestParam(name = "month", defaultValue = "-1") int month, ModelMap model,
            @RequestParam(name = "err", required = false, defaultValue = "0") String err) {

        if (year == -1 && month == -1) {
            LocalDate today = LocalDate.now();
            year = today.getYear();
            month = today.getMonthValue();
        }

        String[] meses = { "", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                "Octubre", "Noviembre", "Diciembre" };
        if (month == 0) {
            month = 12;
            year -= 1;
        } else if (month == 13) {
            month = 1;
            year += 1;
        }
        Usuario usuario = uService.findById(usuario_id);

        List<Clase> clases = clService.findByALumno(usuario);
        LocalDate now = LocalDate.of(year, month, 1);
        int monthLength = now.lengthOfMonth();
        int firstDay = now.withDayOfMonth(1).getDayOfWeek().getValue();

        List<List<Integer>> weeks = new ArrayList<>();
        List<Integer> days = new ArrayList<>();
        for (int i = 1; i < firstDay; i++) {
            days.add(null);
        }
        for (int i = 1; i <= monthLength; i++) {
            days.add(i);
            if (days.size() == 7) {
                weeks.add(days);
                days = new ArrayList<>();
            }
        }
        if (!days.isEmpty()) {
            while (days.size() < 7) {
                days.add(null);
            }
            weeks.add(days);
        }

        ModelAndView modelAndView = new ModelAndView("clases/tabla");
        modelAndView.addObject("year", year);
        modelAndView.addObject("date", meses[now.getMonthValue()]);
        modelAndView.addObject("month", month);
        modelAndView.addObject("weeks", weeks);
        modelAndView.addObject("clases", clases);
        modelAndView.addObject("usuario", usuario);
        modelAndView.addObject("err", err);
        return modelAndView;
    }

    @GetMapping(value = "/modificar")
    public ModelAndView modificarClase(@RequestParam(name = "id") int clase_id,
            @RequestParam(name = "err", required = false, defaultValue = "0") String err) {
        int[] estrellas = { 5, 4, 3, 2, 1 };
        Clase c = clService.findById(clase_id);
        Usuario profesor = uService.findById(c.getProfesor().getId());
        ModelAndView modelAndView = new ModelAndView("clases/modificar");
        modelAndView.addObject("err", err);
        modelAndView.addObject("clase", c);
        modelAndView.addObject("valoracion", c.getValoracion());
        modelAndView.addObject("profesor", profesor);
        modelAndView.addObject("estrellas", estrellas);
        return modelAndView;
    }

    @PostMapping(value = "/update")
    public ModelAndView update(@RequestParam(value = "textAreaComentario", required = false) String comentario,
            @RequestParam(value = "estrellas", required = false) int[] estrellas,
            @RequestParam(value = "clase_id", required = false) int clase_id,
            @RequestParam(value = "valoracion_id", required = false, defaultValue = "0") int valoracion_id,
            HttpSession session) {

        Valoracion v = new Valoracion();
        Clase c = clService.findById(clase_id);
        LocalDate fechaActual = LocalDate.now();

        String err = "0";
        try {
            v.setId(valoracion_id);
            v.setPuntuacion(estrellas[0]);
            v.setComentario(comentario);
            v.setUsuarioValorador(new Usuario((int) session.getAttribute("usuario_id")));
            v.setFechaValoracion(Date.from(fechaActual.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            Valoracion vAux = vService.save(v);

            c.setValoracion(v);
            clService.update(c);
            err = "2";

        } catch (Exception e) {
            err = "1";
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("err", err);
        modelAndView.setViewName("redirect:modificar?id=" + clase_id);
        return modelAndView;
    }

    @RequestMapping(value = "/cancelar")
    public ModelAndView cancelar(@RequestParam(name = "id", required = true) int id) {
        Clase clase = clService.findById(id);
        String err = "0";
        try {
            Calendario claseSeleccionada = cService.findByFechaAndHorariosAndProfesor(clase.getFecha(),
                    clase.getHorarios(), clase.getProfesor());
            clService.deleteById(id);
            claseSeleccionada.setReservado(false);
            cService.update(claseSeleccionada);
            err = "2";
        } catch (Exception e) {
            err = "1";
            System.out.println(e);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("err", err);
        modelAndView.setViewName("redirect:usuario?id=" + clase.getAlumno().getId());
        return modelAndView;
    }
}
