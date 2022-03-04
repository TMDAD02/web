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
public class ServicioUsuarios implements UserDetailsService {
    public static final String PARAMETROS = "PARAMETROS";
    public static final String NOMBRE_COMANDO = "NOMBRE_COMANDO";
    public static final String MASTER_PASSWORD = "0000"; // User authentication does NOT need password for this project

    @Autowired
    Rabbit rabbit;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

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
            System.out.println(respuesta);
            if (respuesta.get("RESULTADO_PETICION").equals("USUARIO_EXISTE")) {
                return User.builder()
                        .username(nombre)
                        .password(new BCryptPasswordEncoder().encode(MASTER_PASSWORD))
                        .accountExpired(false)
                        .credentialsExpired(false)
                        .disabled(false)
                        .accountLocked(false)
                        .roles("USER")
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

    public JSONObject obtenerTodosUsuarios(String miUsuario) throws JSONException {
        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "OBTENER_TODOS_USUARIOS");

        JSONObject parametros = new JSONObject();
        parametros.put("miusuario", miUsuario);
        solicitud.put(PARAMETROS, parametros);
        return rabbit.enviaryRecibirMensaje(solicitud);
    }


    public JSONObject registrar(String nombre, String contrasena, String email, String rol) throws Throwable {
        JSONObject usuario = new JSONObject();
        usuario.put("nombre", nombre);
        usuario.put("contrasenaCifrada", cifrarContrasena(contrasena));
        usuario.put("email", email);
        usuario.put("rol", rol);

        JSONObject mensaje = new JSONObject();
        mensaje.put(NOMBRE_COMANDO, "REGISTRO");
        mensaje.put(PARAMETROS, usuario);
        return rabbit.enviaryRecibirMensaje(mensaje);
    }

    public JSONObject iniciarSesion(String email, String contrasena) throws Throwable {
        JSONObject usuario = new JSONObject();
        usuario.put("email", email);
        usuario.put("contrasenaCifrada", cifrarContrasena(contrasena));

        JSONObject mensaje = new JSONObject();
        mensaje.put(NOMBRE_COMANDO, "LOGIN");
        mensaje.put(PARAMETROS, usuario);

        return rabbit.enviaryRecibirMensaje(mensaje);
    }

    public boolean existeUsuario(String id, boolean gerente) throws Exception {
        JSONObject usuario = new JSONObject();
        usuario.put("id", id);
        JSONObject mensaje = new JSONObject();
        mensaje.put(NOMBRE_COMANDO, "EXISTE_USUARIO");
        mensaje.put(PARAMETROS, usuario);
        JSONObject respuesta = rabbit.enviaryRecibirMensaje(mensaje);
        if(respuesta.getBoolean("existe") && !gerente){
            return true;
        }else if(respuesta.getBoolean("existe") && gerente && respuesta.getString("rol").equals("GERENTE")){
            return true;
        }
        return false;
    }

    /**
     * Cifra la contrasena del usuario usando el cifrado bsáico en base64
     * @param contrasena contraseña a cifrar
     * @return una cadena de texto con la contraseña cifrada
     */
    private String cifrarContrasena(String contrasena) {
        return new String(Base64.getEncoder().encode(contrasena.getBytes()));
    }

    public JSONObject informarEntradaSalida(String idEspacio, String idUsuario, String accion) throws Exception {
        JSONObject informe = new JSONObject();
        informe.put("idEspacio", idEspacio);
        informe.put("idUsuario", idUsuario);
        if (accion.equals("entrada")){
            JSONObject mensaje = new JSONObject();
            mensaje.put(NOMBRE_COMANDO, "INFORMAR_ENTRADA");
            mensaje.put(PARAMETROS, informe);
            return rabbit.enviaryRecibirMensaje(mensaje);
        } else if(accion.equals("salida")) {
            JSONObject mensaje = new JSONObject();
            mensaje.put(NOMBRE_COMANDO, "INFORMAR_SALIDA");
            mensaje.put(PARAMETROS, informe);
            return rabbit.enviaryRecibirMensaje(mensaje);
        }
        throw new Exception();
    }



}
