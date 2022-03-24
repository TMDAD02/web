package com.chatapp.web.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;



@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private Environment env;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app"); // Mensajes que envía el usuario
        //config.enableSimpleBroker("/topic");

        config.enableStompBrokerRelay("/topic")
                .setRelayHost("rat-01.rmq2.cloudamqp.com")
                .setRelayPort(61613)
                .setClientLogin("mudxwkic")
                .setClientPasscode("RanoXwGQ-JCPHuh8JRSVWuerlj6KtOv3")
                .setSystemLogin("mudxwkic")
                .setSystemPasscode("RanoXwGQ-JCPHuh8JRSVWuerlj6KtOv3")
                .setVirtualHost("mudxwkic");


    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        // TODO Auto-generated method stub

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // TODO Auto-generated method stub

    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        // TODO Auto-generated method stub
        return false;
    }

}