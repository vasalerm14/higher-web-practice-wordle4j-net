package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.ProgramException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary load(String file) throws ProgramException {
        List<String> list = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) list.add(line);
        } catch (Exception e) {
            e.printStackTrace(log);
            throw new ProgramException("Ошибка чтения словаря");
        }
        if (list.isEmpty()) throw new ProgramException("Словарь пуст");
        return new WordleDictionary(list);
    }
}
