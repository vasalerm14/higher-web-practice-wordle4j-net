package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleHintTest {

    @Test
    void hintReturnsWordFromDictionary() throws Exception {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("герой", "город", "гонец")
        );

        WordleGame game = new WordleGame(dictionary);
        String hint = game.getHint();

        assertNotNull(hint);
        assertEquals(5, hint.length());
        assertTrue(dictionary.contains(hint));
    }

    @Test
    void hintRespectsPreviousMoves() throws Exception {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("герой", "гонец")
        );

        WordleGame game = new WordleGame(dictionary);
        game.makeMove("гонец");

        String hint = game.getHint();
        assertEquals("герой", hint);
    }
}
