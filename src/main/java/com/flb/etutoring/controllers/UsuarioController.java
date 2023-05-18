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
import com.flb.etutoring.models.Materia;
import com.flb.etutoring.models.Tipo;
import com.flb.etutoring.models.Usuario;
import com.flb.etutoring.services.CalendarioService;
import com.flb.etutoring.services.ClaseService;
import com.flb.etutoring.services.DireccionService;
import com.flb.etutoring.services.MateriaService;
import com.flb.etutoring.services.TipoService;
import com.flb.etutoring.services.UsuarioService;
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

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/list")
    public ModelAndView list(Model model, HttpSession session) {
        List<Usuario> usuarios = uService.findAll();

        for (Usuario usuario : usuarios) {
            usuario.setDirecciones(dService.findByUsuario(usuario));
        }

        ModelAndView modelAndView = new ModelAndView("usuarios/list");
        modelAndView.addObject("usuarios", usuarios);
        return modelAndView;
    }

    @GetMapping(value = "/listProfesor")
    public ModelAndView listProfesores(Model model) {
        List<Usuario> usuarios = uService.findAll();
        LocalDate fechaInicio = LocalDate.now();
        List<CustomObject> combinedList = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            if (usuario.getMateria() != null) {
                List<Clase> listadoClases = clService.findByProfesor(usuario);
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
        modelAndView.addObject("fecha", fechaInicio);
        return modelAndView;
    }

    @RequestMapping(value = { "/modificar" })
    public ModelAndView edit(@RequestParam(name = "id", required = true) int id,
            @RequestParam(name = "err", required = false, defaultValue = "0") String err) {
        ModelAndView modelAndView = new ModelAndView();

        Usuario u = uService.findById(id);
        u.setDirecciones(dService.findByUsuario(u));

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
        modelAndView.setViewName("usuarios/edit");
        return modelAndView;
    }

    @PostMapping(value = "/update")
    public ModelAndView update(Usuario usuario,
            @RequestParam(value = "ck_tipos", required = false) int[] ck_tipos,
            @RequestParam(value = "materia_profe", required = false, defaultValue = "0") int materia_id,
            @RequestParam("file") MultipartFile imagen)
            throws IOException {

        ModelAndView modelAndView = new ModelAndView();

        String err = "0";
        try {
            Usuario u = uService.findById(usuario.getId());
            usuario.setUsername(u.getUsername());
            usuario.setPassword(u.getPassword());
            usuario.setFotoPerfil(imagen.getBytes());

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

        modelAndView.addObject("err", err);
        modelAndView.setViewName("redirect:modificar?id=" + usuario.getId());
        return modelAndView;
    }
}
