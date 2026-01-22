package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.GameException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private WordleGame game;

    @BeforeEach
    void setUp() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("герой")
        );
        game = new WordleGame(dictionary);
    }

    @Test
    void winOnCorrectWord() throws Exception {
        String hint = game.makeMove("герой");

        assertEquals("+++++", hint);
        assertTrue(game.isWin());
        assertTrue(game.isFinished());
    }

    @Test
    void stepsDecreaseAfterMove() throws Exception {
        int before = game.getSteps();
        game.makeMove("герой");
        int after = game.getSteps();

        assertEquals(before - 1, after);
    }

    @Test
    void wrongLengthWordThrowsGameException() {
        assertThrows(GameException.class,
                () -> game.makeMove("дом"));
    }

    @Test
    void nonDictionaryWordThrowsGameException() {
        assertThrows(GameException.class,
                () -> game.makeMove("город"));
    }
}
