import java.util.Random;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter ID: ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String input = scanner.nextLine().trim();
            if (input.equals("quit")) {
                break;
            }
            if (input.isEmpty()) {
                System.out.println("Please enter an ID.");
                continue;
            }
            if (input.length() > 9) {
                System.out.println("ID must be at most 9 digits.");
                continue;
            }
            if (!input.matches("\\d+")) {
                System.out.println("ID must contain only digits.");
                continue;
            }
            while (input.length() < 9) {
                input = "0" + input;
            }
            if (isValidIsraeliID(input)) {
                System.out.println("ID is valid.");
            } else {
                System.out.println("ID is not valid.");
            }
        }
    }
    public static boolean isValidIsraeliID(String id) {
        int sum = 0;
        for (int i = 0; i < id.length(); i++) {
            int digit = id.charAt(i) - '0';
            if (i % 2 == 1) {
                digit = digit * 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum = sum + digit;
        }
        return sum % 10 == 0;
    }
}

class WordleGame {
    public static void main(String[] args) {
        String[] wordArray = {"apple", "grape", "peach", "mango", "berry", "lemon", "melon", "olive", "plumb", "guava"};
        Random random = new Random();
        String secretWord = wordArray[random.nextInt(wordArray.length)];
        Scanner scanner = new Scanner(System.in);
        System.out.println("You are now starting a game of Wordle! First step is guessing a 5 letter word.");
        System.out.println("As we are starting the game, you now have 6 guesses, Good luck!");
        int attempts = 0;
        while (attempts < 6) {
            System.out.print("Enter your guess: ");
            String guess = scanner.nextLine().toLowerCase();
            if (guess.length() != 5) {
                System.out.println("The guess is not a 5 letter word, please revisit your guess and try again.");
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
                System.out.println("That word is not in the dictionary. Please try a known 5-letter word.");
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