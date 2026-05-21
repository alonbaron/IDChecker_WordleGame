import java.util.Random;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter ID number for validation: ");
            String ID = scanner.nextLine();
            boolean isNumbersOnly = true;
            for (char c : ID.toCharArray()) {
                if (c < '0' || c > '9') {
                    isNumbersOnly = false;
                    break;
                }
            }
            if (!isNumbersOnly) {
                System.out.println("What you entered is invalid, please enter only numbers. ");
                continue;
            }
            while (ID.length() < 9) {
                ID = "0" + ID;
            }
            if (isValidIsraeliID(ID)) {
                System.out.println("ID is valid. ");
            } else {
                System.out.println("ID is not valid. ");
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
        char[] feedback = {'_', '_', '_', '_', '_'};
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == secretWord.charAt(i)) {
                feedback[i] = 'G';
            } else if (secretWord.contains(String.valueOf(guess.charAt(i)))) {
                feedback[i] = 'Y';
            }
        }
        return new String(feedback);
    }
}