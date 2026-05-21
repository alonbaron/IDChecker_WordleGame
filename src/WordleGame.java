import java.util.Random;
import java.util.Scanner;

public class WordleGame {
    private static final int WORD_LENGTH = 5;
    private static final int MAX_ATTEMPTS = 6;

    public static void main(String[] args) {
        String[] wordArray = {"apple", "grape", "peach", "mango", "berry", "lemon", "melon", "olive", "plumb", "guava"};
        Random random = new Random();
        String secretWord = wordArray[random.nextInt(wordArray.length)];
        Scanner scanner = new Scanner(System.in);
        System.out.println("You are now starting a game of Wordle! First step is guessing a " + WORD_LENGTH + " letter word.");
        System.out.println("As we are starting the game, you now have " + MAX_ATTEMPTS + " guesses, Good luck!");
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter your guess: ");
            if (!scanner.hasNextLine()) {
                return;  // EOF — silent exit, parity with IsraeliIDValidator
            }
            String guess = scanner.nextLine().toLowerCase();
            if (guess.length() != WORD_LENGTH) {
                System.out.println("The guess is not a " + WORD_LENGTH + " letter word, please revisit your guess and try again.");
                continue;
            }
            boolean found = false;
            for (String word : wordArray) {
                if (guess.equals(word)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("That word is not in the dictionary. Please try a known " + WORD_LENGTH + "-letter word.");
                continue;
            }
            attempts++;
            String feedback = getFeedback(secretWord, guess);
            System.out.println("Feedback: " + feedback);
            if (guess.equals(secretWord)) {
                System.out.println("Congratulations! You guessed the word in " + attempts + " guesses.");
                return;
            }
        }
        System.out.println("You've run out of guesses. The word was: " + secretWord);
    }

    public static String getFeedback(String secretWord, String guess) {
        char[] result = new char[guess.length()];
        char[] pool   = secretWord.toCharArray();

        // Pass 1: greens. Consume matched secret letters from the pool.
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == pool[i]) {
                result[i] = 'G';
                pool[i]   = ' ';   // consumed sentinel — no real word has spaces
            }
        }

        // Pass 2: yellows for non-green positions, consuming from the pool.
        for (int i = 0; i < guess.length(); i++) {
            if (result[i] == 'G') continue;
            char c = guess.charAt(i);
            result[i] = '_';
            for (int j = 0; j < pool.length; j++) {
                if (pool[j] == c) {
                    result[i] = 'Y';
                    pool[j]   = ' ';
                    break;
                }
            }
        }
        return new String(result);
    }
}
