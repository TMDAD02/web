package com.chatapp.web.controllers;

import com.chatapp.web.services.ServicioGrupos;
import com.chatapp.web.services.ServicioUsuarios;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static com.chatapp.web.controllers.ControladorGrupos.PARAMETROS_NOMBRE;
import static com.chatapp.web.controllers.ControladorGrupos.RESULTADO_RESPUESTA_NOMBRE;

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
        try {
            if(servicioUsuarios.existeUsuario(to)) {
                return "chat";
            } else {
                JSONObject respuesta = servicioGrupos.obtenerTodosGrupos(userDetails.getUsername());
                if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("OBTENER_TODOS_GRUPOS_CORRECTO")) {
                    JSONArray grupos = respuesta.getJSONObject(PARAMETROS_NOMBRE).getJSONArray("listaGrupos");
                    for (int i = 0; i < grupos.length(); i++) {
                        if (grupos.get(i).equals(to)) {
                            return "chat";
                        }
                    }
                }
            }
            return "error";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}

