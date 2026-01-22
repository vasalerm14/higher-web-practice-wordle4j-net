package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WordleServerStatisticLoader {
    private final String file;

    public WordleServerStatisticLoader(String file) {
        this.file = file;
    }

    public Map<String, Integer> load() {
        Map<String, Integer> stats = new HashMap<>();
        try (BufferedReader r = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = r.readLine();
            if (line == null || line.isBlank()) return stats;
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
        return stats;
    }

    public void save(Map<String, Integer> stats) {
        try (FileWriter w = new FileWriter(file, StandardCharsets.UTF_8)) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            int i = 0;
            for (var e : stats.entrySet()) {
                if (i++ > 0) sb.append(",");
                sb.append("\"").append(e.getKey()).append("\":").append(e.getValue());
            }
            sb.append("}");
            w.write(sb.toString());
        } catch (Exception ignored) {
        }
    }
}
