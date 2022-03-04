package com.chatapp.web.controllers;

import com.chatapp.web.configuration.JWT;
import com.chatapp.web.configuration.JWTTokenUtil;
import com.chatapp.web.services.ServicioUsuarios;
import org.json.JSONArray;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static com.chatapp.web.services.ServicioUsuarios.MASTER_PASSWORD;


@RestController
public class ControladorUsuarios {


    @Autowired
    private ServicioUsuarios servicioUsuarios;
    public static final String RESULTADO_RESPUESTA_NOMBRE = "RESULTADO_PETICION";
    public static final String PARAMETROS_NOMBRE = "PARAMETROS";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @PostMapping(path = "/usuario", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registro(@RequestParam String nombre, @RequestParam String contrasena, @RequestParam String email) {
        try {
            System.out.println("Hello world");
            JSONObject respuesta = servicioUsuarios.registrar(nombre, contrasena, email, "USER");
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("REGISTRO_CORRECTO")){
                return new ResponseEntity<>( "{}", HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Throwable throwable) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(path = "/autenticar", produces = "application/json")
    public ResponseEntity<?> iniciarSesion(@RequestParam String nombre, HttpServletResponse response) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nombre, MASTER_PASSWORD));
            SecurityContextHolder.getContext().setAuthentication(auth);
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            response.addCookie(new Cookie("token", token));
            return ResponseEntity.ok(new JWT(token));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/usuarios")
    public ResponseEntity<?> obtenerUsuarios() {
        try {
            UserDetails ud = (UserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
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




    @GetMapping(path = "/normal")
    public ResponseEntity<?> hola() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

/*
    @GetMapping(path = "/notificaciones", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> obtenerNotificaciones(@RequestParam String id_usuario){
        try {
            if (servicioUsuarios.existeUsuario(id_usuario, false)){
                return new ResponseEntity<>(servicioUsuarios.obtenerNotificaciones(id_usuario).toString(), HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Throwable e){
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */
}
