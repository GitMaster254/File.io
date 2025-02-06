package Service;

import Models.FileMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileService {

    // Directory where shared files will be stored
    private static final String SHARE_DIRECTORY = "shared_files/";

    // Method to share a file and return a FileMessage
    public static FileMessage shareFile(File file, String sender) {
        if (file == null || !file.exists() || sender == null) {
            System.out.println("Invalid file or sender.");
            return null;
        }

        try {
            // Ensure the shared directory exists
            File dir = new File(SHARE_DIRECTORY);
            if (!dir.exists()) {
                dir.mkdir(); // Create the directory if it doesn't exist
            } 

            // Define the path where the file will be stored
            Path targetPath = Path.of(SHARE_DIRECTORY, file.getName());

            // Copy the file to the shared directory
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File shared: " + targetPath);

            // Return a FileMessage object for use in chat
            return new FileMessage(sender, file.getName(), targetPath.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Failed to share the file
        }
    }

    // Method to retrieve a file for download
    public static File getFile(String fileName) {
        File file = new File(SHARE_DIRECTORY + fileName);
        return file.exists() ? file : null;
    }
}
