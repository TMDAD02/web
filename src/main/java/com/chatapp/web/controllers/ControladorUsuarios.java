package com.chatapp.web.controllers;

import com.chatapp.web.services.ServicioUsuarios;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static com.chatapp.web.services.ServicioUsuarios.MASTER_PASSWORD;


@RestController
public class ControladorUsuarios {

    @Autowired
    private ServicioUsuarios servicioUsuarios;
    public static final String RESULTADO_RESPUESTA_NOMBRE = "RESULTADO_PETICION";
    public static final String PARAMETROS_NOMBRE = "PARAMETROS";

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(path = "/miusuario")
    public ResponseEntity<?> obtenerUsuario(@AuthenticationPrincipal final UserDetails ud, HttpServletResponse response) throws JSONException {
        JSONObject parametros = new JSONObject();
        parametros.put("usuario", ud.getUsername());
        return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
    }


    @GetMapping(path = "/usuarios")
    public ResponseEntity<?> obtenerUsuarios(@AuthenticationPrincipal final UserDetails ud) {
        try {
            //UserDetails ud = (UserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            JSONObject respuesta = servicioUsuarios.obtenerTodosUsuarios(ud.getUsername());
            System.out.println(respuesta);
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("OBTENER_TODOS_USUARIOS_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (JSONException e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
