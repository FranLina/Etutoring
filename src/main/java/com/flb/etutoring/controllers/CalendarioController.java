package com.flb.etutoring.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.flb.etutoring.models.Calendario;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.CalendarioService;
import com.flb.etutoring.services.UsuarioService;

@Controller
@RequestMapping("calendario")
public class CalendarioController {

    @Autowired
    UsuarioService uService;

    @Autowired
    CalendarioService cService;

    @GetMapping(value = "/profesor")
    public ModelAndView tabla(@RequestParam(name = "id") int profesor_id,
            @RequestParam(name = "year", defaultValue = "2023") int year,
            @RequestParam(name = "month", defaultValue = "4") int month, Model model) {
        ModelAndView modelAndView = new ModelAndView("calendarios/tabla");

        String[] meses = { "", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                "Octubre", "Noviembre", "Diciembre" };
        if (month == 0) {
            month = 12;
            year -= 1;
        } else if (month == 13) {
            month = 1;
            year += 1;
        }
        Usuario profesor = uService.findById(profesor_id);
        List<Calendario> calendario = cService.findByProfesor(profesor);
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

        modelAndView.addObject("year", year);
        modelAndView.addObject("date", meses[now.getMonthValue()]);
        modelAndView.addObject("month", month);
        modelAndView.addObject("weeks", weeks);
        modelAndView.addObject("calendar", calendario);
        modelAndView.addObject("profesor", profesor);
        return modelAndView;
    }
}
