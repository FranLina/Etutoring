package com.flb.etutoring.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.flb.etutoring.models.Calendario;
import com.flb.etutoring.models.Clase;
import com.flb.etutoring.models.Dia;
import com.flb.etutoring.models.Mes;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.CalendarioService;
import com.flb.etutoring.services.ClaseService;
import com.flb.etutoring.services.UsuarioService;

@Controller
@RequestMapping("calendario")
public class CalendarioController {

    @Autowired
    UsuarioService uService;

    @Autowired
    CalendarioService cService;

    @Autowired
    ClaseService clService;

    @GetMapping(value = "/profesor")
    public ModelAndView tabla(@RequestParam(name = "id") int profesor_id,
            @RequestParam(name = "year", defaultValue = "-1") int year,
            @RequestParam(name = "month", defaultValue = "-1") int month,
            @RequestParam(name = "err", required = false, defaultValue = "0") String err, ModelMap model) {

        if (year == -1 && month == -1) {
            LocalDate today = LocalDate.now();
            year = today.getYear();
            month = today.getMonthValue();
        }

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

        LocalDate diaActual = LocalDate.now();
        List<Calendario> calendario = cService.findByProfesorAndGreaterThanEqualFecha(profesor,
                Date.from(diaActual.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
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

        modelAndView.addObject("err", err);
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

        Usuario profesor = uService.findById(profesor_id);
        LocalDate dia = LocalDate.of(year, month, day);

        List<Calendario> c = cService.findByProfesorAndFecha(profesor, Date
                .from(dia.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

        ModelAndView modelAndView = new ModelAndView("calendarios/reservar");
        modelAndView.addObject("calendar", c);
        modelAndView.addObject("profesor", profesor);
        return modelAndView;
    }

    @GetMapping(value = "/reserva")
    public ModelAndView reservaClase(@RequestParam(name = "id") int clase_id,
            ModelMap model, HttpSession session) {

        Calendario cal = cService.findById(clase_id);
        String err = "0";

        try {
            Usuario alumno = new Usuario(Integer.parseInt(session.getAttribute("usuario_id").toString()));
            Clase c = clService.findByFechaAndHorariosAndAlumno(cal.getFecha(), cal.getHorarios(), alumno);
            if (c == null) {
                Clase claseSeleccionada = new Clase();
                claseSeleccionada.setFecha(cal.getFecha());
                claseSeleccionada.setHorarios(cal.getHorarios());
                claseSeleccionada.setProfesor(cal.getProfesor());
                claseSeleccionada.setAlumno(alumno);
                claseSeleccionada.setOnline(false);
                clService.save(claseSeleccionada);

                cal.setReservado(true);
                cService.update(cal);
                err = "2";
            } else {
                err = "1";
            }
        } catch (Exception e) {
            err = "3";
        }

        ModelAndView modelAndView = new ModelAndView("redirect:profesor?id=" + cal.getProfesor().getId());
        modelAndView.addObject("err", err);
        return modelAndView;
    }

    @RequestMapping(value = { "/generarCalendario" })
    public ModelAndView edit(@RequestParam(name = "id", required = true) int id,
            @RequestParam(name = "err", required = false, defaultValue = "0") String err) {

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

        modelAndView.addObject("err", err);
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
            @RequestParam(value = "fecha_final", required = false) String fechaFi, Model model) {

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

        List<Calendario> calendarioExistente = cService.findByProfesor(profesor);

        List<Calendario> calendariosGenerados = new ArrayList<>();
        List<Calendario> calendariosAux = new ArrayList<>();

        String err = "0";

        try {

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
                                            .from(fechaActual.atStartOfDay().atZone(ZoneId.systemDefault())
                                                    .toInstant()));
                            calendario.setHorarios(horario);
                            calendario.setProfesor(profesor);
                            calendario.setReservado(false);
                            calendariosGenerados.add(calendario);
                            // cService.save(calendario);
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
                                calendario.setReservado(false);
                                calendariosGenerados.add(calendario);
                                // cService.save(calendario);
                            }

                        }
                        fechaActual = fechaActual.plusDays(1);
                    }
                }

            }

            for (Calendario c : calendariosGenerados) {
                if (!(calendarioExistente.contains(c))) {
                    calendariosAux.add(c);
                }
            }

            for (Calendario c : calendariosAux) {
                cService.save(c);
            }
            err = "2";

        } catch (Exception e) {
            err = "1";
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("err", err);
        modelAndView.setViewName("redirect:generarCalendario?id=" + profe_id);
        return modelAndView;
    }
}
