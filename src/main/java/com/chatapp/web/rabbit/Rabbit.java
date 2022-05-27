package com.chatapp.web.rabbit;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.chatapp.web.services.ServicioUsuarios.NOMBRE_COMANDO;

@Component
public class Rabbit {
    public static final String COLA_PETICIONES = "peticiones";

    private final RabbitTemplate rabbitTemplate;

    public Rabbit(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setReplyTimeout(60000);
    }


    @Bean
    public Queue peticionQueue() {
        return new Queue(COLA_PETICIONES);
    } // Auto Durable


    public JSONObject enviaryRecibirMensaje(JSONObject mensaje) throws Exception {
        long t0 = System.currentTimeMillis();
        try {
            String respuesta =  String.valueOf(rabbitTemplate.convertSendAndReceive(Rabbit.COLA_PETICIONES, mensaje.toString()));
            long tiempo = System.currentTimeMillis() - t0;
            System.out.println("{" + tiempo + "} - " + respuesta);
            return new JSONObject(respuesta);
        } catch (Exception e) {
            System.out.println("La petición <" +  mensaje.getString(NOMBRE_COMANDO) + "> no se ha podido procesar");
            throw new JSONException("---");
        }
    }






}
