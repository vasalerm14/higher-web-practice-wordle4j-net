package ru.yandex.practicum.exceptions;

public class WordNotFoundInDictionary extends GameException {
    public WordNotFoundInDictionary(String m) {
        super(m);
    }
}
