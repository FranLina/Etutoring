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
import com.flb.etutoring.models.Direccion;
import com.flb.etutoring.models.Tipo;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.CalendarioService;
import com.flb.etutoring.services.DireccionService;
import com.flb.etutoring.services.TipoService;
import com.flb.etutoring.services.UsuarioService;

@SpringBootTest
class EtutoringApplicationTests {

	@Autowired
	UsuarioService uService;

	@Autowired
	TipoService tService;

	@Autowired
	CalendarioService cService;

	@Autowired
	DireccionService dService;

	@Autowired
	PasswordEncoder encoder;

	@Test
	void crearUsuarioAdminYPermisos() {
		Usuario u = new Usuario();
		u.setId(1);
		u.setNombre("admin");
		u.setCorreo("admin@gmail.com");
		u.setUsername("admin");
		u.setPassword(encoder.encode("1234"));

		Tipo t1 = new Tipo();
		t1.setId(1);
		t1.setNombre("ADMIN");
		tService.save(t1);

		Tipo t2 = new Tipo();
		t2.setId(2);
		t2.setNombre("PROFESOR");
		tService.save(t2);

		Tipo t3 = new Tipo();
		t3.setId(3);
		t3.setNombre("ALUMNO");
		tService.save(t3);

		Direccion d = new Direccion();
		d.setId(1);
		d.setAltitud(-3);
		d.setLatitud(27);

		List<Tipo> listaPermisos = new ArrayList<>();
		listaPermisos.add(t1);
		listaPermisos.add(t2);
		listaPermisos.add(t3);
		u.setTipo(listaPermisos);
		Usuario aux = uService.save(u);
		d.setUsuario(aux);
		dService.save(d);
	}

	@Test
	void crearUsuario() {
		Usuario u = new Usuario();
		u.setId(30);
		u.setNombre("Maria");
		u.setApellidos("Lopéz Arance");
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
