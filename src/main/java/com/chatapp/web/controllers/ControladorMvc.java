package com.chatapp.web.controllers;

import com.chatapp.web.services.ServicioGrupos;
import com.chatapp.web.services.ServicioUsuarios;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static com.chatapp.web.controllers.ControladorGrupos.PARAMETROS_NOMBRE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Controller
public class ControladorMvc {

    @Autowired
    private ServicioUsuarios servicioUsuarios;

    @Autowired
    private ServicioGrupos servicioGrupos;

    @GetMapping("/")
    public String setRootPath(Model model) throws IOException {
        return "redirect:/chat?to=mario";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String to, @AuthenticationPrincipal UserDetails userDetails) throws Throwable {
        if(servicioUsuarios.existeUsuario(to) || to.equals("anuncios")) {
            return "chat";
        } else {
            JSONObject respuesta = servicioGrupos.obtenerGrupos(userDetails.getUsername());
            JSONArray grupos = ((JSONObject)  respuesta.get(PARAMETROS_NOMBRE)).getJSONArray("listaGrupos");
            if (grupos.toString().contains(to)) {
                return "chat";
            }

            throw new ResponseStatusException(UNAUTHORIZED, "");
        }
    }

}

