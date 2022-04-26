package com.chatapp.web.scheduled;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Metricas {
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    private final Counter bytesCounter;
    private final AtomicInteger currentUserCounter;
    private final Counter messagesCounter;

    public Metricas(MeterRegistry meterRegistry) {
        bytesCounter = meterRegistry.counter("bytes");
        messagesCounter = meterRegistry.counter("messages");
        currentUserCounter = meterRegistry.gauge("current_users", new AtomicInteger(0));
    }

    @Scheduled(fixedRateString = "5000", initialDelayString = "0")
    private void setCurrentUsers() {
        currentUserCounter.set(simpUserRegistry.getUserCount());
    }

    public void incrementBytes(double bytes) {
        bytesCounter.increment(bytes);
    }

    public void incrementMessages() {
        messagesCounter.increment();
    }
}