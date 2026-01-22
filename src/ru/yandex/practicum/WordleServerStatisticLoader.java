package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WordleServerStatisticLoader {

    private final File file;

    public WordleServerStatisticLoader(String fileName) {
        this.file = new File(fileName);
    }

    public Map<String, Integer> load() throws Exception {
        Map<String, Integer> stats = new HashMap<>();
        if (!file.exists()) return stats;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
        )) {
            String json = reader.readLine();
            if (json == null || json.isBlank()) return stats;

            json = json.replace("{", "").replace("}", "");
            for (String entry : json.split(",")) {
                String[] pair = entry.split(":");
                stats.put(pair[0], Integer.parseInt(pair[1]));
            }
        }
        return stats;
    }

    public void save(Map<String, Integer> stats) throws Exception {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)
        )) {
            writer.print("{");
            boolean first = true;
            for (var e : stats.entrySet()) {
                if (!first) writer.print(",");
                writer.print(e.getKey() + ":" + e.getValue());
                first = false;
            }
            writer.print("}");
        }
    }
}
