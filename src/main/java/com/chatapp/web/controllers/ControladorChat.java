package com.chatapp.web.controllers;

import com.chatapp.web.services.ServicioChat;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.chatapp.web.controllers.ControladorUsuarios.PARAMETROS_NOMBRE;
import static com.chatapp.web.controllers.ControladorUsuarios.RESULTADO_RESPUESTA_NOMBRE;

@RestController
public class ControladorChat {

    @Autowired
    private ServicioChat servicioChat;


    @GetMapping(path = "/mensajes", produces = "application/json")
    public ResponseEntity<?> obtenerMensajesUsuario(@RequestParam String destinatario, @AuthenticationPrincipal UserDetails ud) {
        try {
            JSONObject respuesta = servicioChat.obtenerMensajesUsuarios(ud.getUsername(), destinatario);
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("OBTENER_MENSAJES_USUARIOS_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (Throwable e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @GetMapping(path = "/noleidos", produces = "application/json")
    public ResponseEntity<?> obtenerMensajesNoLeidos() {
        try {
            UserDetails ud = (UserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            JSONObject respuesta = servicioChat.obtenerMensajesNoLeidos(ud.getUsername());
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("OBTENER_MENSAJES_NO_LEIDOS_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (Throwable e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
