package ru.yandex.practicum;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class WordleServer {
    private static final String FILE = "stats.json";

    public static void main(String[] args) throws Exception {
        WordleServerStatisticLoader loader =
                new WordleServerStatisticLoader(FILE);
        Map<String, Integer> stats = loader.load();
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/stats", e -> {
            try {
                if ("POST".equalsIgnoreCase(e.getRequestMethod())) {
                    String body = new String(e.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    String name = body.split("\"")[3];
                    stats.put(name, stats.getOrDefault(name, 0) + 1);
                    loader.save(stats);
                    e.sendResponseHeaders(200, -1);
                } else if ("GET".equalsIgnoreCase(e.getRequestMethod())) {
                    String json = buildTopJson(stats);
                    byte[] out = json.getBytes(StandardCharsets.UTF_8);
                    e.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
                    e.sendResponseHeaders(200, out.length);
                    e.getResponseBody().write(out);
                } else {
                    e.sendResponseHeaders(405, -1);
                }
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

    private static String buildTopJson(Map<String, Integer> stats) {
        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(stats.entrySet());
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
