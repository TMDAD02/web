package com.chatapp.web.rabbit;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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
        System.out.println(mensaje);
        String respuesta = String.valueOf(rabbitTemplate.convertSendAndReceive(Rabbit.COLA_PETICIONES, mensaje.toString()));
        return new JSONObject(respuesta);
    }

    // No usar
    public void enviarMensaje(JSONObject mensaje) {
        rabbitTemplate.convertAndSend(Rabbit.COLA_PETICIONES, mensaje.toString());
    }



}
