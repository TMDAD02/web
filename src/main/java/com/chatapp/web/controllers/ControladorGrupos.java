package com.chatapp.web.controllers;


import com.chatapp.web.services.ServicioGrupos;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.chatapp.web.controllers.ControladorUsuarios.PARAMETROS_NOMBRE;
import static com.chatapp.web.controllers.ControladorUsuarios.RESULTADO_RESPUESTA_NOMBRE;

@RestController()
public class ControladorGrupos {

    @Autowired
    private ServicioGrupos servicioGrupos;
    public static final String RESULTADO_RESPUESTA_NOMBRE = "RESULTADO_PETICION";
    public static final String PARAMETROS_NOMBRE = "PARAMETROS";

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(path = "/grupos/obtener")
    public ResponseEntity<?> obtenerGrupos(@AuthenticationPrincipal final UserDetails ud) {
        try {
            //UserDetails ud = (UserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            JSONObject respuesta = servicioGrupos.obtenerGrupos(ud.getUsername());
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("OBTENER_TODOS_GRUPOS_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (Throwable e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/grupos/crear")
    public ResponseEntity<?> crearGrupo(@RequestParam String nombreGrupo, @AuthenticationPrincipal final UserDetails ud) {
        try {
            JSONObject respuesta = servicioGrupos.crearGrupo(ud.getUsername(), nombreGrupo);
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("GRUPO_CREADO_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (Throwable e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/grupos/mensajes", produces = "application/json")
    public ResponseEntity<?> obtenerMensajesGrupo(@RequestParam String destinatario) {
        try {
            JSONObject respuesta = servicioGrupos.obtenerMensajesGrupos(destinatario);
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("OBTENER_MENSAJES_GRUPOS_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (Throwable e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/grupos/eliminar")
    public ResponseEntity<?> eliminarGrupo(@RequestParam String nombreGrupo, @AuthenticationPrincipal final UserDetails ud) {
        try {
            JSONObject respuesta = servicioGrupos.eliminarGrupo(ud.getUsername(), nombreGrupo);
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("GRUPO_ELIMINADO_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (Throwable e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/grupos/anadir_usuario")
    public ResponseEntity<?> anadirUsuarioGrupo(@RequestParam String nombreUsuario, @RequestParam String nombreGrupo, @AuthenticationPrincipal final UserDetails ud) {
        try {

            JSONObject respuesta = servicioGrupos.anadirUsuarioGrupo(ud.getUsername(), nombreUsuario, nombreGrupo);
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("ANADIR_USUARIO_GRUPO_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (Throwable e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/grupos/eliminar_usuario")
    public ResponseEntity<?> eliminarUsuarioGrupo(@RequestParam String nombreUsuario, @RequestParam String nombreGrupo, @AuthenticationPrincipal final UserDetails ud) {
        try {

            JSONObject respuesta = servicioGrupos.eliminarUsuarioGrupo(ud.getUsername(), nombreUsuario, nombreGrupo);
            if(respuesta.getString(RESULTADO_RESPUESTA_NOMBRE).equals("ELIMINAR_USUARIO_GRUPO_CORRECTO")) {
                JSONObject parametros = respuesta.getJSONObject(PARAMETROS_NOMBRE);
                return new ResponseEntity<>(parametros.toString(), HttpStatus.OK);
            }

        } catch (Throwable e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
