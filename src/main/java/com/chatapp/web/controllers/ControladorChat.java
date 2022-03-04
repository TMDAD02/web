package com.chatapp.web.controllers;

import com.chatapp.web.configuration.JWT;
import com.chatapp.web.services.ServicioChat;
import com.chatapp.web.services.ServicioUsuarios;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static com.chatapp.web.controllers.ControladorUsuarios.PARAMETROS_NOMBRE;
import static com.chatapp.web.controllers.ControladorUsuarios.RESULTADO_RESPUESTA_NOMBRE;
import static com.chatapp.web.services.ServicioUsuarios.MASTER_PASSWORD;

@RestController
public class ControladorChat {

    @Autowired
    private ServicioChat servicioChat;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping(path = "/mensajes", produces = "application/json")
    public ResponseEntity<?> obtenerMensajes(@RequestParam String destinatario) {
        try {
            UserDetails ud = (UserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            JSONObject respuesta = servicioChat.obtenerMensajes(ud.getUsername(), destinatario);
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("OBTENER_MENSAJES_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (JSONException e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/noleidos", produces = "application/json")
    public ResponseEntity<?> obtenerMensajesNoLeidos() {
        try {
            UserDetails ud = (UserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            JSONObject respuesta = servicioChat.obtenerMensajesNoLeidos(ud.getUsername());
            System.out.println(respuesta);
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("OBTENER_MENSAJES_NO_LEIDOS_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (JSONException e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
