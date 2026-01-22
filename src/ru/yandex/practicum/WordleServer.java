package ru.yandex.practicum;

import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordleServer {

    private static final String FILE = "stats.json";
    private static final Map<String, Integer> stats = new HashMap<>();

    public static void main(String[] args) throws Exception {
        load();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/result", e -> {
            try {
                String body = new String(e.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String name = body.split("\"")[3];
                stats.put(name, stats.getOrDefault(name, 0) + 1);
                save();
                e.sendResponseHeaders(200, -1);
            } catch (Exception ex) {
                try {
                    e.sendResponseHeaders(500, -1);
                } catch (Exception ignored) {
                }
            } finally {
                e.close();
            }
        });

        server.createContext("/top", e -> {
            try {
                String json = buildTopJson();
                byte[] out = json.getBytes(StandardCharsets.UTF_8);
                e.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
                e.sendResponseHeaders(200, out.length);
                e.getResponseBody().write(out);
            } catch (Exception ex) {
                try {
                    e.sendResponseHeaders(500, -1);
                } catch (Exception ignored) {
                }
            } finally {
                e.close();
            }
        });

        server.start();
    }

    private static void load() {
        try (BufferedReader r = new BufferedReader(new FileReader(FILE, StandardCharsets.UTF_8))) {
            String line = r.readLine();
            if (line == null || line.isBlank()) return;

            line = line.replace("{", "").replace("}", "");
            String[] parts = line.split(",");

            for (String p : parts) {
                String[] kv = p.split(":");
                if (kv.length == 2) {
                    String name = kv[0].replace("\"", "").trim();
                    int wins = Integer.parseInt(kv[1].trim());
                    stats.put(name, wins);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static void save() {
        try (FileWriter w = new FileWriter(FILE, StandardCharsets.UTF_8)) {
            w.write(buildStatsJson());
        } catch (Exception ignored) {
        }
    }

    private static String buildStatsJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int i = 0;
        for (var e : stats.entrySet()) {
            if (i++ > 0) sb.append(",");
            sb.append("\"").append(e.getKey()).append("\":").append(e.getValue());
        }
        sb.append("}");
        return sb.toString();
    }

    private static String buildTopJson() {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(stats.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry<String, Integer>::getValue).reversed());

        StringBuilder sb = new StringBuilder();
        sb.append("{\"top\":[");

        for (int i = 0; i < list.size() && i < 10; i++) {
            if (i > 0) sb.append(",");
            sb.append("{\"nickname\":\"")
                    .append(list.get(i).getKey())
                    .append("\",\"wins\":")
                    .append(list.get(i).getValue())
                    .append("}");
        }

        sb.append("]}");
        return sb.toString();
    }
}
