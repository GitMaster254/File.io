package Models;


public class FileMessage extends Message {
    private String fileName;
    private String filePath;

    public FileMessage(String sender, String fileName, String filePath) {
        super(sender, "[File: " + fileName + "]");
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }
}
