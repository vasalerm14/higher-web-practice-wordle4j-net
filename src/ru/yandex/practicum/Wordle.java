package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameException;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


//Добрый день, не разобрался куда делать pull request, как раньше сделал dev в main, а у вас ничего нет, пришлось ZIP
//отправить, извините
public class Wordle {
    public static void main(String[] args) {
        try (
                PrintWriter log = new PrintWriter(new FileWriter("wordle.log", StandardCharsets.UTF_8));
                Scanner scanner = new Scanner(System.in)
        ) {
            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
            WordleDictionary dictionary = loader.load("words_ru.txt");
            WordleGame game = new WordleGame(dictionary, log);
            while (!game.isFinished()) {
                System.out.print("> ");
                String input = scanner.nextLine();
                if (input.isBlank()) {
                    System.out.println(game.getHint());
                    continue;
                }
                try {
                    String result = game.makeMove(input);
                    System.out.println(input);
                    System.out.println(result);
                } catch (GameException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Загаданное слово: " + game.getAnswer());
            if (!game.isWin()) return;
            System.out.print("Никнейм: ");
            String nickname = scanner.nextLine().trim();
            sendResult(nickname, game.getMovesCount(), game.usedHints());
            System.out.println(getTop(nickname));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendResult(String nickname, int steps, boolean hints) throws Exception {
        URL url = new URL("http://localhost:8080/result");
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("POST");
        c.setDoOutput(true);
        String json = "{\"nickname\":\"" + nickname + "\",\"steps\":" + steps + ",\"usedHints\":" + hints + "}";
        c.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        c.getInputStream().close();
    }

    private static String getTop(String nickname) throws Exception {
        URL url = new URL("http://localhost:8080/top?nickname=" + nickname);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        return new String(c.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
