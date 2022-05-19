package com.chatapp.web.services;

import com.chatapp.web.rabbit.Rabbit;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class ServicioGrupos implements UserDetailsService{
    public static final String PARAMETROS = "PARAMETROS";
    public static final String NOMBRE_COMANDO = "NOMBRE_COMANDO";
    public static final String MASTER_PASSWORD = "0000"; // User authentication



    @Autowired
    Rabbit rabbit;

    /**
     * Spring Security authentication
     *
     * @param nombre
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        //System.out.println("Hello, Spring Security! let me check: " + nombre);
        try {
            JSONObject respuesta = autenticarUsuario(nombre);
            if (respuesta.get("RESULTADO_PETICION").equals("USUARIO_EXISTE")) {
                return User.builder()
                        .username(nombre)
                        .password(new BCryptPasswordEncoder().encode(MASTER_PASSWORD))
                        .accountExpired(false)
                        .credentialsExpired(false)
                        .disabled(false)
                        .accountLocked(false)
                        .roles("USUARIO")
                        .build();
            }
        } catch (Throwable e) {
            throw new UsernameNotFoundException("User or password invalid");
        }
        throw new UsernameNotFoundException("User or password invalid");
    }

    public JSONObject autenticarUsuario(String nombre) throws Throwable {
        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "AUTENTICAR_USUARIO");

        JSONObject parametros = new JSONObject();
        parametros.put("nombre", nombre);
        solicitud.put(PARAMETROS, parametros);
        return rabbit.enviaryRecibirMensaje(solicitud);
    }

    public JSONObject obtenerTodosGrupos(String miUsuario) throws JSONException {
        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "OBTENER_TODOS_GRUPOS");

        JSONObject parametros = new JSONObject();
        parametros.put("miusuario", miUsuario);
        solicitud.put(PARAMETROS, parametros);
        return rabbit.enviaryRecibirMensaje(solicitud);
    }

    public JSONObject crearGrupo(String miUsuario, String nombreGrupo) throws JSONException {
        //System.out.println("hemos solicitado crear grupo!!!!!!!!!!!!!!!! " );
        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "CREAR_GRUPO");

        JSONObject parametros = new JSONObject();
        parametros.put("miusuario", miUsuario);
        parametros.put("nombregrupo", nombreGrupo);
        solicitud.put(PARAMETROS, parametros);
        return rabbit.enviaryRecibirMensaje(solicitud);
    }

    public JSONObject eliminarGrupo(String miUsuario, String nombreGrupo) throws JSONException {
        //System.out.println("hemos solicitado eiminar grupo!!!!!!!!!!!!!!!! " );
        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "ELIMINAR_GRUPO");

        JSONObject parametros = new JSONObject();
        parametros.put("miusuario", miUsuario);
        parametros.put("nombregrupo", nombreGrupo);
        solicitud.put(PARAMETROS, parametros);
        return rabbit.enviaryRecibirMensaje(solicitud);
    }

    public JSONObject anadirUsuarioGrupo(String miUsuario, String nombreUsuario, String nombreGrupo) throws JSONException {

        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "ANADIR_USUARIO_GRUPO");

        //String[] splitted = nombreUsuarioGrupo.split(",");
        //System.out.println("usuario: "+splitted[0]);
        //System.out.println("grupo: "+splitted[1]);
        System.out.println("Añadir usario grupo");
        System.out.println("nombre usuario es: "+ nombreUsuario);

        JSONObject parametros = new JSONObject();
        parametros.put("miusuario", miUsuario);
        parametros.put("nombreusuario", nombreUsuario);
        parametros.put("nombregrupo", nombreGrupo);
        solicitud.put(PARAMETROS, parametros);
        return rabbit.enviaryRecibirMensaje(solicitud);
    }

    public JSONObject eliminarUsuarioGrupo(String miUsuario, String nombreUsuario, String nombreGrupo) throws JSONException {

        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "ELIMINAR_USUARIO_GRUPO");

        //String[] splitted = nombreUsuarioGrupo.split(",");
        //System.out.println("usuario: "+splitted[0]);
        //System.out.println("grupo: "+splitted[1]);
        System.out.println("ELIMINAR usario grupo");
        System.out.println("nombre usuario es: "+ nombreUsuario);

        JSONObject parametros = new JSONObject();
        parametros.put("miusuario", miUsuario);
        parametros.put("nombreusuario", nombreUsuario);
        parametros.put("nombregrupo", nombreGrupo);
        solicitud.put(PARAMETROS, parametros);
        return rabbit.enviaryRecibirMensaje(solicitud);
    }

}