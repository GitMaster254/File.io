package Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionService {

    // Generate a random salt
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 16-byte salt
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hash password using SHA-256 and salt
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8)); // Add salt to hash
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8)); // Hash password

            return Base64.getEncoder().encodeToString(hashedBytes); // Convert to readable format
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle error properly in real applications
        }
    }

    // Verify if a password matches the stored hash
    public static boolean verifyPassword(String enteredPassword, String storedHash, String storedSalt) {
        String enteredHash = hashPassword(enteredPassword, storedSalt);

        //debug logs
        System.out.println("stored hash" + storedHash);
        System.out.println("entered hash" + enteredHash);
        System.out.println("stored salt" + storedSalt);
        
        return storedHash.equals(enteredHash);
    }
}
