package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameException;
import ru.yandex.practicum.exceptions.InvalidWordException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WordleGame {
    private static final int MAX_STEPS = 6;
    private final WordleDictionary dictionary;
    private final PrintWriter log;
    private final String answer;
    private int steps = MAX_STEPS;
    private boolean usedHints;
    private final List<String> guesses = new ArrayList<>();
    private final List<String> hints = new ArrayList<>();

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.log = log;
        this.answer = dictionary.getRandomWord();
    }

    public WordleGame(WordleDictionary dictionary) {
        this(dictionary, new PrintWriter(System.out));
    }

    public WordleGame(WordleDictionary dictionary, String answer) {
        this.dictionary = dictionary;
        this.log = new PrintWriter(System.out);
        this.answer = answer;
    }

    public String makeMove(String input) throws GameException {
        String word = WordleDictionary.normalize(input);

        if (!WordleDictionary.isValid(word)) {
            throw new InvalidWordException("Некорректное слово");
        }

        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionary("Слова нет в словаре");
        }

        String hint = WordleDictionary.analyze(word, answer);
        guesses.add(word);
        hints.add(hint);
        steps--;

        log.println(word + " " + hint);
        log.flush();

        return hint;
    }

    public String getHint() throws GameException {
        usedHints = true;
        for (String w : dictionary.getWords()) {
            boolean ok = true;
            for (int i = 0; i < guesses.size(); i++) {
                if (!WordleDictionary.analyze(guesses.get(i), w).equals(hints.get(i))) {
                    ok = false;
                    break;
                }
            }
            if (ok) return w;
        }
        throw new GameException("Нет подходящих слов");
    }

    public boolean isFinished() {
        return steps == 0 || isWin();
    }

    public boolean isWin() {
        return !guesses.isEmpty() && guesses.get(guesses.size() - 1).equals(answer);
    }

    public int getSteps() {
        return steps;
    }

    public int getMovesCount() {
        return guesses.size();
    }

    public boolean usedHints() {
        return usedHints;
    }

    public String getAnswer() {
        return answer;
    }
}
