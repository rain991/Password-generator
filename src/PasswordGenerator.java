import java.util.Random;

public class PasswordGenerator {
    static String special = "!@#$%^&*+<>?";

    static String generatePassword(int length, boolean onlyDigits, boolean onlyLetters, boolean specialChar) {
        Random random = new Random();
        String digits = "0123456789";
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String characterPool = "";
        if (onlyDigits) {
            characterPool = digits;
        } else if (onlyLetters) {
            characterPool = letters;
        } else {
            characterPool = digits + letters;
            if (specialChar) {
                characterPool += special;
            }
        }

        StringBuilder password = new StringBuilder();
        while (password.length() < length) {
            int index = random.nextInt(characterPool.length());
            char selectedChar = characterPool.charAt(index);
            password.append(selectedChar);
        }

        if (specialChar && !containsSpecialCharacter(password.toString())) {
            int indexOfSpecialChar = random.nextInt(special.length());
            char specialCh = special.charAt(indexOfSpecialChar);
            int position = random.nextInt(password.length());
            password.setCharAt(position, specialCh);
        }

        return password.toString();
    }

    static private boolean containsSpecialCharacter(String password) {
        for (char ch : password.toCharArray()) {
            if (special.indexOf(ch) >= 0) {
                return true;
            }
        }
        return false;
    }
}
