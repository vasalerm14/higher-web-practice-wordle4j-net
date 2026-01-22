package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    @Test
    void normalizeLowercaseAndYo() {
        assertEquals("елка", WordleDictionary.normalize("ЁЛКА"));
        assertEquals("герой", WordleDictionary.normalize("ГЕРОЙ"));
    }

    @Test
    void validWordCheck() {
        assertTrue(WordleDictionary.isValid("герой"));
        assertFalse(WordleDictionary.isValid("дом"));
        assertFalse(WordleDictionary.isValid("abcde"));
    }

    @Test
    void containsWordFromDictionary() {
        WordleDictionary d = new WordleDictionary(
                List.of("герой", "город")
        );

        assertTrue(d.contains("ГЕРОЙ"));
        assertFalse(d.contains("домик"));
    }

    @Test
    void analyzeExampleFromTask() {
        String result = WordleDictionary.analyze("гонец", "герой");
        assertEquals("+^-^-", result);
    }
}
