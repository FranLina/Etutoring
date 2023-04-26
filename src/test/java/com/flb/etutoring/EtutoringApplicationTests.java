package com.flb.etutoring;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flb.etutoring.models.Calendario;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.CalendarioService;
import com.flb.etutoring.services.UsuarioService;

@SpringBootTest
class EtutoringApplicationTests {

	@Autowired
	UsuarioService uService;

	@Autowired
	CalendarioService cService;

	@Autowired
	PasswordEncoder encoder;

	@Test
	void crearUsuario() {
		Usuario u = new Usuario();
		u.setId(30);
		u.setNombre("Maria");
		u.setApellidos("Lopéz Arance");
		u.setCiudad("Bailén");
		u.setCorreo("maria@gmail.com");
		u.setUsername("maria");
		u.setPassword(encoder.encode("1234"));

		uService.save(u);
	}

	@Test
	void crearCalendario() {
		List<DayOfWeek> dias = new ArrayList<DayOfWeek>();
		dias.add(DayOfWeek.MONDAY);
		dias.add(DayOfWeek.THURSDAY);
		dias.add(DayOfWeek.FRIDAY);

		HashMap<DayOfWeek, Integer> diasSemana = new HashMap<>();
		diasSemana.put(DayOfWeek.MONDAY, 0);
		diasSemana.put(DayOfWeek.TUESDAY, 1);
		diasSemana.put(DayOfWeek.WEDNESDAY, 2);
		diasSemana.put(DayOfWeek.THURSDAY, 3);
		diasSemana.put(DayOfWeek.FRIDAY, 4);
		diasSemana.put(DayOfWeek.SATURDAY, 5);
		diasSemana.put(DayOfWeek.SUNDAY, 6);

		List<String> horarios = new ArrayList<String>();

		horarios.add("");
		horarios.add("18:00-19:00");
		horarios.add("15:00-16:00");
		horarios.add("");
		horarios.add("");
		horarios.add("20:00-21:30");
		horarios.add("19:00-20:30");

		Usuario profesor = uService.findById(1);

		LocalDate fechaInicio = LocalDate.now();
		LocalDate fechaFinal = LocalDate.of(2023, 4, 30);
		LocalDate fechaActual = fechaInicio;
		while (fechaActual.isBefore(fechaFinal)) {
			if (!dias.contains(fechaActual.getDayOfWeek())) {
				Calendario calendario = new Calendario();
				calendario.setFecha(Date.from(fechaActual.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
				calendario.setHorarios(horarios.get(diasSemana.get(fechaActual.getDayOfWeek())));
				calendario.setProfesor(profesor);
				cService.save(calendario);
			}
			fechaActual = fechaActual.plusDays(1);
		}

	}

}
