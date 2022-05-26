package com.chatapp.web.scheduled;

import com.chatapp.web.services.ServicioChat;
import com.chatapp.web.services.ServicioTrending;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Metricas {
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private ServicioTrending servicioTrending;

    @Autowired
    private ServicioChat servicioChat;

    private final Counter bytesCounter;
    private final AtomicInteger currentUserCounter;
    private final AtomicInteger currentChatCounter;
    private final Counter messagesCounter;

    public Metricas(MeterRegistry meterRegistry) {
        bytesCounter = meterRegistry.counter("bytes");
        messagesCounter = meterRegistry.counter("messages");
        currentUserCounter = meterRegistry.gauge("current_users", new AtomicInteger(0));
        currentChatCounter = meterRegistry.gauge("current_chats", new AtomicInteger(0));
    }

    @Scheduled(fixedRateString = "5000", initialDelayString = "0")
    private void establecerUsuariosActivos() {
        currentUserCounter.set(simpUserRegistry.getUserCount());
    }

    @Scheduled(fixedRateString = "5000", initialDelayString = "0")
    private void establecerChatsActivos() {
        try {
            int chatsActivos = servicioChat.obtenerChatsActivos();
            System.out.println("ChatsActivos " + chatsActivos);
            currentChatCounter.set(chatsActivos);
        } catch (JSONException e) {
            System.out.println("Error al actualizar chats: " + e);
        }
    }

    @Scheduled(fixedRateString = "60000", initialDelayString = "0")
    private void updateTrending() {
        try {
            servicioTrending.actualizarTrending();
        } catch (JSONException e) {
            System.out.println("Error al actualizar trending: " + e);
        }
    }

    public void incrementBytes(double bytes) {
        bytesCounter.increment(bytes);
    }

    public void incrementMessages() {
        messagesCounter.increment();
    }
}