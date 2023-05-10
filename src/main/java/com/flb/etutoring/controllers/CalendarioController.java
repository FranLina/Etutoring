package com.flb.etutoring.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.flb.etutoring.models.Calendario;
import com.flb.etutoring.models.Dia;
import com.flb.etutoring.models.Mes;
import com.flb.etutoring.models.Notificacion;
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
            @RequestParam(name = "month", defaultValue = "5") int month, ModelMap model) {
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

    @GetMapping(value = "/reservar")
    public ModelAndView reservar(@RequestParam(name = "id") int profesor_id,
            @RequestParam(name = "year") int year,
            @RequestParam(name = "month") int month,
            @RequestParam(name = "day") int day,
            ModelMap model) {

        ModelAndView modelAndView = new ModelAndView("calendarios/reservar");
        return modelAndView;
    }

    @RequestMapping(value = { "/generarCalendario" })
    public ModelAndView edit(@RequestParam(name = "id", required = true) int id,
            @RequestParam(name = "notificacion", required = false) Notificacion noti,
            ModelMap model) {

        String[] dias = { "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo" };
        String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septimebre",
                "Octubre", "Noviembre", "Diciembre" };
        List<Dia> mapDias = new ArrayList<>();
        int contador = 0;
        for (String dia : dias) {
            mapDias.add(new Dia(contador, dia));
            contador++;
        }
        List<Mes> mapMes = new ArrayList<>();
        contador = 0;
        for (String mes : meses) {
            mapMes.add(new Mes(contador, mes));
            contador++;
        }

        ModelAndView modelAndView = new ModelAndView();

        Usuario u = uService.findById(id);

        modelAndView.addObject("notificacion", noti);
        modelAndView.addObject("dias", mapDias);
        modelAndView.addObject("meses", mapMes);
        modelAndView.addObject("profesor", u);
        modelAndView.setViewName("calendarios/generar");
        return modelAndView;
    }

    @PostMapping(value = "/save")
    public ModelAndView save(@RequestParam(value = "id") int profe_id,
            @RequestParam(value = "ck_dias") int[] ck_dias,
            @RequestParam(value = "ck_mes", required = false) int[] ck_meses,
            @RequestParam(value = "ck_horas") int[] ck_horas,
            @RequestParam(value = "fecha_inicio", required = false) String fechaIn,
            @RequestParam(value = "fecha_final", required = false) String fechaFi, ModelMap model) {

        LocalDate fechaHoy = LocalDate.now();

        HashMap<DayOfWeek, Integer> diasSemana = new HashMap<>();
        diasSemana.put(DayOfWeek.MONDAY, 0);
        diasSemana.put(DayOfWeek.TUESDAY, 1);
        diasSemana.put(DayOfWeek.WEDNESDAY, 2);
        diasSemana.put(DayOfWeek.THURSDAY, 3);
        diasSemana.put(DayOfWeek.FRIDAY, 4);
        diasSemana.put(DayOfWeek.SATURDAY, 5);
        diasSemana.put(DayOfWeek.SUNDAY, 6);

        HashMap<Integer, DayOfWeek> diasSemanaInverso = new HashMap<>();
        diasSemanaInverso.put(0, DayOfWeek.MONDAY);
        diasSemanaInverso.put(1, DayOfWeek.TUESDAY);
        diasSemanaInverso.put(2, DayOfWeek.WEDNESDAY);
        diasSemanaInverso.put(3, DayOfWeek.THURSDAY);
        diasSemanaInverso.put(4, DayOfWeek.FRIDAY);
        diasSemanaInverso.put(5, DayOfWeek.SATURDAY);
        diasSemanaInverso.put(6, DayOfWeek.SUNDAY);

        List<String> dias = new ArrayList<String>();
        for (int dia : ck_dias) {
            dias.add(diasSemanaInverso.get(dia).name());
        }

        Usuario profesor = uService.findById(profe_id);

        if (fechaIn != null && fechaFi != null) {
            String[] fechaI = fechaIn.split("-");
            String[] fechaF = fechaFi.split("-");

            LocalDate fechaInicio = LocalDate.of(Integer.parseInt(fechaI[0]), Integer.parseInt(fechaI[1]),
                    Integer.parseInt(fechaI[2]));
            LocalDate fechaFinal = LocalDate.of(Integer.parseInt(fechaF[0]), Integer.parseInt(fechaF[1]),
                    Integer.parseInt(fechaF[2]));

            LocalDate fechaActual = fechaInicio;

            while (fechaActual.isBefore(fechaFinal.plusDays(1))) {
                if (dias.contains(fechaActual.getDayOfWeek().name())) {
                    for (int hora : ck_horas) {
                        String horario = "";
                        if (hora < 10)
                            horario += "0";
                        horario += hora + ":00-";
                        if (hora + 1 < 10)
                            horario += "0";
                        horario += (hora + 1) + ":00";

                        Calendario calendario = new Calendario();
                        calendario
                                .setFecha(Date
                                        .from(fechaActual.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                        calendario.setHorarios(horario);
                        calendario.setProfesor(profesor);
                        calendario.setReservado(false);
                        cService.save(calendario);
                    }

                }
                fechaActual = fechaActual.plusDays(1);
            }
        } else if (ck_meses != null) {

            for (int mesActual : ck_meses) {
                LocalDate fechaActual = LocalDate.of(fechaHoy.getYear(), mesActual + 1, 1);
                LocalDate fechaFinal = fechaActual.with(TemporalAdjusters.lastDayOfMonth());
                while (fechaActual.isBefore(fechaFinal.plusDays(1))) {
                    if (dias.contains(fechaActual.getDayOfWeek().name())) {
                        for (int hora : ck_horas) {
                            String horario = "";
                            if (hora < 10)
                                horario += "0";
                            horario += hora + ":00-";
                            if (hora + 1 < 10)
                                horario += "0";
                            horario += (hora + 1) + ":00";

                            Calendario calendario = new Calendario();
                            calendario
                                    .setFecha(Date
                                            .from(fechaActual.atStartOfDay().atZone(ZoneId.systemDefault())
                                                    .toInstant()));
                            calendario.setHorarios(horario);
                            calendario.setProfesor(profesor);
                            cService.save(calendario);
                        }

                    }
                    fechaActual = fechaActual.plusDays(1);
                }
            }

        }

        String error = "Ha ocurrido un error al generar el calendario, compruebe que ninguna hora coincida con algun calendario existente.";
        String correcto = "El calendario ha sido generado con exito";

        Notificacion noti = new Notificacion(2, correcto);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("notificacion", noti);
        modelAndView.setViewName("redirect:generarCalendario?id=" + profe_id);
        return modelAndView;

        /*
         * modelAndView.setViewName("redirect:profesor?id=" + profe_id + "&year=" +
         * fechaHoy.getYear() + "&month="
         * + fechaHoy.getMonthValue());
         */
    }
}
