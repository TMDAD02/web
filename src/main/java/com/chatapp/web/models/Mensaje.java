package com.chatapp.web.models;

public class Mensaje {

    private String fuente;
    private String contenido;
    private String destino;

    public Mensaje(String from, String message, String to) {
        this.fuente = from;
        this.contenido = message;
        this.destino = to;
    }

    public Mensaje() {

    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + fuente + '\'' +
                ", message='" + contenido + '\'' +
                ", to='" + destino + '\'' +
                '}';
    }
}