package com.chatapp.web.controllers;

import com.chatapp.web.models.Mensaje;
import com.chatapp.web.scheduled.Metricas;
import com.chatapp.web.services.ServicioChat;
import com.chatapp.web.services.ServicioTrending;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.stream.Collectors;

import static com.chatapp.web.models.Mensaje.TAMANIO_MAXIMO;


@Controller
public class ControladorWebSocket {

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ServicioChat servicioChat;

    @Autowired
    private Metricas metricas;

    @Autowired
    private ServicioTrending servicioTrending;


    @MessageMapping("/chat/{to}")
    public void tratarChatPuntoPunto(@DestinationVariable String to, Mensaje mensaje) throws Exception {
        System.out.println("Mensaje: " + mensaje);
        String text = mensaje.getContenido();
        if (text.length() < TAMANIO_MAXIMO) {
            metricas.incrementMessages();
            metricas.incrementBytes(text.getBytes().length);
            boolean destinatarioConectado = esUsuarioConectado(to);
            simpMessagingTemplate.convertAndSend("/topic/" + to, mensaje);
            servicioChat.guardarMensaje(mensaje, destinatarioConectado);
            servicioTrending.update(text);
        }
    }


    public void enviarMensajeFichero(Mensaje mensaje) throws JSONException {
        System.out.println("Fichero: " + mensaje);
        boolean destinatarioConectado = esUsuarioConectado(mensaje.getDestino());
        metricas.incrementMessages();
        //metricas.incrementBytes(mensaje.getContenido().getBytes().length);
        simpMessagingTemplate.convertAndSend("/topic/" + mensaje.getDestino(), mensaje);
        servicioChat.guardarMensaje(mensaje, destinatarioConectado);

    }


    @EventListener(SessionConnectEvent.class)
    public void handleWebsocketConnectListner(SessionConnectEvent event) {
       System.out.println("Received a new websocket connection. Welcome " + event.getUser().getName());
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListner(SessionDisconnectEvent event) {
        System.out.println("Goodbye: " + event.getUser());
    }

    private boolean esUsuarioConectado(String user) {
        return  this.simpUserRegistry
                .getUsers()
                .stream()
                .map(SimpUser::getName)
                .collect(Collectors.toList()).contains(user);
    }


}