import java.util.Scanner;

public class IsraeliIDValidator {
    private static final int ID_LENGTH = 9;

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
            if (input.length() > ID_LENGTH) {
                System.out.println("ID must be at most " + ID_LENGTH + " digits.");
                continue;
            }
            if (!input.matches("\\d+")) {
                System.out.println("ID must contain only digits.");
                continue;
            }
            while (input.length() < ID_LENGTH) {
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
