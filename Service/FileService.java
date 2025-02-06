package Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileService {

    // Define the directory where shared files will be saved
    private static final String SHARE_DIRECTORY = "shared_files/";

    // Method to share the file
    public static boolean shareFile(File file) {
        if (file != null && file.exists()) {
            try {
                // Ensure the shared directory exists
                File dir = new File(SHARE_DIRECTORY);
                if (!dir.exists()) {
                    dir.mkdir();  // Create the directory if it doesn't exist
                }

                // Define the path where the file will be shared
                Path targetPath = Path.of(SHARE_DIRECTORY, file.getName());

                // Copy the file to the shared directory
                Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("File shared: " + targetPath);
                return true;  // Successfully shared the file
            } catch (IOException e) {
                e.printStackTrace();
                return false;  // Failed to share the file
            }
        }
        return false;
    }
}
