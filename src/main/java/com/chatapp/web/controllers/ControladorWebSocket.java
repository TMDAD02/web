package com.chatapp.web.controllers;

import com.chatapp.web.models.Mensaje;
import com.chatapp.web.services.ServicioChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.stream.Collectors;


@Controller
public class ControladorWebSocket {

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ServicioChat servicioChat;


    @MessageMapping("/chat/{to}")
    public void tratarChatPuntoPunto(@DestinationVariable String to, Mensaje mensaje) throws Exception {
        System.out.println("Mensaje: " + mensaje);
        boolean destinatarioConectado = esUsuarioConectado(to);
        if(destinatarioConectado) {
            simpMessagingTemplate.convertAndSend("/topic/messages/" + to, mensaje);
        }
        servicioChat.guardarMensaje(mensaje, destinatarioConectado);

    }

    @MessageMapping("/chatroom/{to}")
    public void chatroomHandler(@DestinationVariable String to, Mensaje message) throws Exception {
        System.out.println("Current users: " + this.simpUserRegistry
                .getUsers()
                .stream()
                .map(SimpUser::getName)
                .collect(Collectors.toList()));
        if(esUsuarioConectado(to)) {
            simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
        }

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