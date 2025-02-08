package Networking;

import java.io.*;

public class FileSender {

    private static final int BUFFER_SIZE = 4 * 1024 * 1024;// 4MB buffer

    public static void sendFile(
        File file, OutputStream outputStream, TransferListener listener
        )throws IOException{ 
            try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){
                byte [] buffer = new byte[BUFFER_SIZE];
                long totalBytes = file.length();
                long bytesSent = 0;
                int bytesRead;

                while((bytesRead = bis.read(buffer)) != -1){
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                    bytesSent += bytesRead;

                    int progress = (int)((bytesSent * 100) / totalBytes);
                    listener.onProgress(progress);
                }
                listener.onComplete("File sent successfully:" + file.getName());
            } catch(IOException e){
                listener.onError("Error sending file:" + e.getMessage());
                throw e;
            }

        }
    
}
