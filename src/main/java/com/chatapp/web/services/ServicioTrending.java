package com.chatapp.web.services;

import com.chatapp.web.rabbit.Rabbit;
import io.cucumber.java.sl.In;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.chatapp.web.services.ServicioGrupos.NOMBRE_COMANDO;
import static com.chatapp.web.services.ServicioGrupos.PARAMETROS;

@Service
public class ServicioTrending {

    private Map<String, Integer> words = new HashMap<>();
    private String[] sustantivos;

    @Autowired
    Rabbit rabbit;

    public void actualizarLista(String contenido) {
        StringTokenizer st = new StringTokenizer(contenido, " ");
        while (st.hasMoreTokens()) {
            String token = st.nextToken().toLowerCase();
            if (stringBinarySearch(sustantivos, token) >= 0) {
                if (words.containsKey(token)) {
                    int count = words.get(token);
                    count++;
                    words.put(token, count);
                } else {
                    words.put(token, 1);
                }
            }

        }
    }
/*
    public Map<String, Integer> sorting() {
        trends = words.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return trends;
    }*/

    public void iniciar() {
        List<String> list = new ArrayList<>(62500);
        File file = new File("sustantivos.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line.toLowerCase());
            }
            sustantivos = list.toArray(new String[list.size()]);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int stringBinarySearch(String[] sustantivos, String token) {
        int min = 0;
        int max = sustantivos.length - 1;
        int mid;
        while (min <= max) {
            mid = (min + max) / 2;
            if (sustantivos[mid].compareTo(token) < 0) {
                min = mid + 1;
            } else if (sustantivos[mid].compareTo(token) > 0) {
                max = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // No la mejor solución, pero funciona...
    private JSONObject mapToJson(Map<String, Integer> map) throws JSONException {
        JSONObject out = new JSONObject();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            out.put(key, value);
        }
        return out;
    }

    public void actualizarTrending() throws Throwable {
        JSONObject solicitud = new JSONObject();
        solicitud.put(NOMBRE_COMANDO, "ACTUALIZAR_TRENDING");
        JSONObject parametros = new JSONObject();
        parametros.put("listaTrending", mapToJson(words));
        solicitud.put(PARAMETROS, parametros);
        rabbit.enviaryRecibirMensaje(solicitud);
    }
}