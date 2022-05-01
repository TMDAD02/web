package com.chatapp.web.services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicioTrending {

    private Map<String, Integer> words = new HashMap<>();
    private String[] sustantivos;
    private List<String> trends;

    public void update(String contenido) {
        StringTokenizer st = new StringTokenizer(contenido, " ");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
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

    public List<String> sorting() {
        trends = words.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(e -> e.getKey())
                .collect(Collectors.toList());
        //trends.forEach(System.out::println);
        return trends;
    }

    public void init() {
        List<String> list = new ArrayList<>(62500);
        File file = new File("Sustantivos.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int i = 0;
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
}