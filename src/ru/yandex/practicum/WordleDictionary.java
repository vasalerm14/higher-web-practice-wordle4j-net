package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleDictionary {
    private final List<String> words = new ArrayList<>();
    private final Random random = new Random();

    public WordleDictionary(List<String> source) {
        for (String s : source) {
            String w = normalize(s);
            if (isValid(w)) words.add(w);
        }
    }

    public static String normalize(String s) {
        return s.toLowerCase().replace('ё', 'е').trim();
    }

    public static boolean isValid(String s) {
        if (s.length() != 5) return false;
        for (char c : s.toCharArray())
            if (c < 'а' || c > 'я') return false;
        return true;
    }

    public boolean contains(String s) {
        return words.contains(normalize(s));
    }

    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    public List<String> getWords() {
        return words;
    }

    public static String analyze(String guess, String answer) {
        char[] r = {'-', '-', '-', '-', '-'};
        boolean[] used = new boolean[5];
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                r[i] = '+';
                used[i] = true;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (r[i] == '+') continue;
            for (int j = 0; j < 5; j++) {
                if (!used[j] && guess.charAt(i) == answer.charAt(j)) {
                    r[i] = '^';
                    used[j] = true;
                    break;
                }
            }
        }
        return new String(r);
    }
}
