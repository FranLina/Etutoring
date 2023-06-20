package com.flb.etutoring.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.flb.etutoring.models.Clase;
import com.flb.etutoring.models.Direccion;
import com.flb.etutoring.models.Materia;
import com.flb.etutoring.models.Municipios;
import com.flb.etutoring.models.Provincias;
import com.flb.etutoring.models.Tipo;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.models.Valoracion;
import com.flb.etutoring.models.Calendario;
import com.flb.etutoring.models.Ccaa;
import com.flb.etutoring.services.CalendarioService;
import com.flb.etutoring.services.ClaseService;
import com.flb.etutoring.services.DireccionService;
import com.flb.etutoring.services.MateriaService;
import com.flb.etutoring.services.MunicipiosService;
import com.flb.etutoring.services.ProvinciasService;
import com.flb.etutoring.services.TipoService;
import com.flb.etutoring.services.UsuarioService;
import com.flb.etutoring.services.ValoracionService;
import com.flb.etutoring.utils.CustomObject;

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

    @Autowired
    CalendarioService cService;

    @Autowired
    ClaseService clService;

    @Autowired
    ProvinciasService pService;

    @Autowired
    MunicipiosService muniService;

    @Autowired
    ValoracionService vService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/list")
    public ModelAndView list(Model model,
            @RequestParam(name = "err", required = false, defaultValue = "0") String err,
            HttpSession session) {
        List<Usuario> usuarios = uService.findAll();

        for (Usuario usuario : usuarios) {
            usuario.setDirecciones(dService.findByUsuario(usuario));
        }

        ModelAndView modelAndView = new ModelAndView("usuarios/list");
        modelAndView.addObject("err", err);
        modelAndView.addObject("usuarios", usuarios);
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ALUMNO')")
    @GetMapping(value = "/listProfesor")
    public ModelAndView listProfesores(
            @RequestParam(name = "materia_id", required = false, defaultValue = "-1") int materia_id,
            @RequestParam(name = "provincia_id", required = false, defaultValue = "-1") int provincia_id,
            @RequestParam(name = "municipios_id", required = false, defaultValue = "-1") int municipios_id,
            Model model) {
        List<Usuario> usuarios = new ArrayList<>();
        List<Usuario> usuariosAux = new ArrayList<>();
        if (materia_id == -1) {
            usuarios = uService.findAll();
        } else {
            usuarios = uService.findByMateria(materia_id);
        }
        if (municipios_id != -1) {
            for (Usuario u : usuarios) {
                if (u.getMunicipio() != null)
                    if (u.getMunicipio().getIdMunicipio() == municipios_id) {
                        usuariosAux.add(u);
                    }
            }
            usuarios = usuariosAux;
        }

        List<Provincias> provincias = new ArrayList<>();
        provincias.add(new Provincias(-1, new Ccaa(), "Sin Seleccionar"));
        provincias.addAll(pService.findAll());

        List<Municipios> municipios = new ArrayList<>();
        // municipios.addAll(muniService.findByProvincia(new Provincias(23, null,
        // null)));

        List<Materia> materias = new ArrayList<>();
        materias.add(new Materia(-1, "Todos"));
        materias.addAll(mService.findAll());
        LocalDate fechaInicio = LocalDate.now();
        List<CustomObject> combinedList = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            usuario.setDirecciones(dService.findByUsuario(usuario));
            if (usuario.getMateria() != null) {
                List<Clase> listadoClases = clService.findByProfesorAndValoracionNotNull(usuario);
                double sum = 0, total = 0;
                if (listadoClases.size() > 0) {
                    for (Clase c : listadoClases) {
                        sum += c.getValoracion().getPuntuacion();
                    }
                    total = sum / listadoClases.size();
                }
                combinedList.add(new CustomObject(usuario, total));
            }
        }

        ModelAndView modelAndView = new ModelAndView("usuarios/listProfesor");
        modelAndView.addObject("combinedList", combinedList);
        modelAndView.addObject("materias", materias);
        modelAndView.addObject("provincias", provincias);
        modelAndView.addObject("municipios", municipios);
        modelAndView.addObject("materia_id", materia_id);
        modelAndView.addObject("provincia_id", provincia_id);
        modelAndView.addObject("municipios_id", municipios_id);
        modelAndView.addObject("fecha", fechaInicio);
        return modelAndView;
    }

    @RequestMapping(value = { "/modificar" })
    public ModelAndView edit(@RequestParam(name = "id", required = true) int id,
            @RequestParam(name = "err", required = false, defaultValue = "0") String err) {
        ModelAndView modelAndView = new ModelAndView();

        Usuario u = uService.findById(id);
        u.setDirecciones(dService.findByUsuario(u));

        List<Provincias> provincias = new ArrayList<>();
        provincias.add(new Provincias(-1, new Ccaa(), "Sin Seleccionar"));
        provincias.addAll(pService.findAll());

        List<Municipios> municipios = new ArrayList<>();
        if (u.getMunicipio() != null)
            municipios.addAll(muniService.findByProvincia(u.getMunicipio().getProvincia()));

        List<Tipo> tipos = tService.findAll();
        for (Tipo t : tipos) {
            if (u.getTipo().contains(t)) {
                t.setPerteneceUsuario(true);
            }
        }

        String isProfesor = "";
        for (Tipo ti : u.getTipo()) {
            if (ti.getNombre().equals("PROFESOR")) {
                isProfesor = "1";
                break;
            }
        }

        modelAndView.addObject("err", err);
        modelAndView.addObject("usuario", u);
        modelAndView.addObject("isProfesor", isProfesor);
        modelAndView.addObject("materias", mService.findAll());
        modelAndView.addObject("tipos", tipos);
        modelAndView.addObject("provincias", provincias);
        modelAndView.addObject("municipios", municipios);
        modelAndView.setViewName("usuarios/edit");
        return modelAndView;
    }

    @PostMapping(value = "/update")
    public ModelAndView update(Usuario usuario, Direccion direccion,
            @RequestParam(value = "ck_tipos", required = false) int[] ck_tipos,
            @RequestParam(value = "usuario_id", required = false, defaultValue = "0") int usuario_id,
            @RequestParam(value = "municipio_id", required = false, defaultValue = "0") int municipio_id,
            @RequestParam(value = "materia_profe", required = false, defaultValue = "0") int materia_id,
            @RequestParam("file") MultipartFile imagen)
            throws IOException {

        ModelAndView modelAndView = new ModelAndView();

        String err = "0";
        try {
            Usuario u = uService.findById(usuario_id);
            usuario.setId(u.getId());
            usuario.setUsername(u.getUsername());
            usuario.setPassword(u.getPassword());
            usuario.setFotoPerfil(imagen.getBytes());
            if (municipio_id != 0)
                usuario.setMunicipio(new Municipios(municipio_id));

            if (ck_tipos != null) {
                List<Tipo> tipos = usuario.getTipo();
                if (tipos == null) {
                    tipos = new ArrayList<Tipo>();
                }

                for (int i : ck_tipos) {
                    Tipo t = new Tipo(i);
                    tipos.add(t);
                }

                usuario.setTipo(tipos);
            } else {
                usuario.setTipo(u.getTipo());
            }

            if (materia_id != 0) {
                Materia m = new Materia(materia_id);
                usuario.setMateria(m);
            }

            uService.update(usuario);

            err = "2";
        } catch (Exception e) {
            err = "1";
        }
        try {
            if (direccion != null) {
                direccion.setUsuario(usuario);
                dService.update(direccion);
            }
        } catch (Exception e) {
            err = "3";
        }

        modelAndView.addObject("err", err);
        modelAndView.setViewName("redirect:modificar?id=" + usuario.getId());
        return modelAndView;
    }

    @RequestMapping(value = { "/borrar" })
    public ModelAndView delete(@RequestParam(name = "id", required = true) int id,
            @RequestParam(name = "err", required = false, defaultValue = "0") String err) {
        ModelAndView modelAndView = new ModelAndView();

        Usuario u = uService.findById(id);
        err = "0";
        try {
            List<Direccion> listaDirecciones = dService.findByUsuario(u);
            for (Direccion d : listaDirecciones) {
                dService.deleteById(d.getId());
            }

            List<Clase> listaClases = new ArrayList<>();
            List<Calendario> listaCalendarios = new ArrayList<>();

            for (Tipo t : u.getTipo()) {
                if (t.getNombre().equals("ALUMNO")) {
                    listaClases = clService.findByALumno(u);
                } else if (t.getNombre().equals("PROFESOR")) {
                    listaClases = clService.findByProfesor(u);
                    listaCalendarios = cService.findByProfesor(u);
                }
            }

            if (listaClases.size() > 0) {
                for (Clase cl : listaClases) {
                    clService.deleteById(cl.getId());
                    /*
                     * if (cl.getValoracion() != null) {
                     * vService.deleteById(cl.getValoracion().getId());
                     * }
                     */
                }
            }

            if (listaCalendarios.size() > 0) {
                for (Calendario c : listaCalendarios) {
                    cService.deleteById(c.getId());
                }
            }

            uService.deleteById(id);
            err = "2";

        } catch (Exception e) {
            err = "1";
            System.out.println(e);
        }

        modelAndView.addObject("err", err);
        modelAndView.setViewName("redirect:list");
        return modelAndView;
    }
}
