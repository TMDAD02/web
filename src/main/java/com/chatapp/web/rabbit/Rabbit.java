package com.chatapp.web.rabbit;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Rabbit {
    public static final String COLA_PETICIONES = "peticiones";

    private final RabbitTemplate rabbitTemplate;

    public Rabbit(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Bean
    public Queue peticionQueue() {
        return new Queue(COLA_PETICIONES);
    } // Auto Durable


    public JSONObject enviaryRecibirMensaje(JSONObject mensaje) throws JSONException {
        long t0 = System.currentTimeMillis();
        String respuesta = String.valueOf(rabbitTemplate.convertSendAndReceive(Rabbit.COLA_PETICIONES, mensaje.toString()));
        long t1 = System.currentTimeMillis();
        long tiempo = t1-t0;
        System.out.println("Tiempo de respuesta APP/Rabbit: " + tiempo);
        return new JSONObject(respuesta);
    }

    // No usar
    public void enviarMensaje(JSONObject mensaje) {
        rabbitTemplate.convertAndSend(Rabbit.COLA_PETICIONES, mensaje.toString());
    }



}
