package com.chatapp.web.services;

import com.chatapp.web.models.Mensaje;
import com.chatapp.web.rabbit.Rabbit;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.chatapp.web.services.ServicioUsuarios.NOMBRE_COMANDO;
import static com.chatapp.web.services.ServicioUsuarios.PARAMETROS;

@Service
public class ServicioChat {

    @Autowired
    Rabbit rabbit;

    public List<String> obtenerIntegrantesSala(String sala) {
        return null;
    }

    public void guardarMensaje(Mensaje mensaje, boolean leido) throws JSONException {
        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "GUARDAR_MENSAJE");
        JSONObject parametros = new JSONObject();
        parametros.put("fuente", mensaje.getFuente());
        parametros.put("destino", mensaje.getDestino());
        parametros.put("contenido", mensaje.getContenido());
        parametros.put("leido", leido);
        solicitud.put(PARAMETROS, parametros);

        rabbit.enviaryRecibirMensaje(solicitud);

    }

    public JSONObject obtenerMensajes(String fuente, String destinatario) throws JSONException {
        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "OBTENER_MENSAJES");

        JSONObject parametros = new JSONObject();
        parametros.put("fuente", fuente);
        parametros.put("destino", destinatario);
        solicitud.put(PARAMETROS, parametros);
        return rabbit.enviaryRecibirMensaje(solicitud);
    }

    public JSONObject obtenerMensajesNoLeidos(String usuario) throws JSONException {
        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "OBTENER_MENSAJES_NO_LEIDOS");

        JSONObject parametros = new JSONObject();
        parametros.put("usuario", usuario);
        solicitud.put(PARAMETROS, parametros);
        return rabbit.enviaryRecibirMensaje(solicitud);
    }


}
