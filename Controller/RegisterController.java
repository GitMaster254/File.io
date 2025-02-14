package Controller;

import Service.EncryptionService;
import Database.UserDAO;

public class RegisterController {
    public void register(String username, String password, String confirmPassword, String email) {
        // Validate input
        if (username == null || username.isEmpty() || password == null || password.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty() || email == null || email.isEmpty()) {
            System.out.println("All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        // Generate salt and hash the password
        String salt = EncryptionService.generateSalt();
        String hashedPassword = EncryptionService.hashPassword(password, salt);

        // Register user in the database
        boolean success = UserDAO.registerUser(username, hashedPassword, salt, email);

        if (success) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Error registering user.");
        }
    }
}
