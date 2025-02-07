package Networking;

import java.io.*;

public class FileReceiver {
    private static final int BUFFER_SIZE = 4 * 1024 * 1024;

    public static void receiveFile(
        InputStream inputStream, File outputFile,TransferListener listener
    )throws IOException{
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))){
            byte[] buffer = new byte[BUFFER_SIZE];
            long byteReceived = 0;
            int bytesRead;

            while((bytesRead = inputStream.read(buffer)) != -1){
                bos.write(buffer, 0, bytesRead);
                byteReceived += bytesRead;

                int progress = (int)((byteReceived * 100)/ outputFile.length());
                listener.onProgress(progress);
            }
            listener.onComplete("File received successfully:" + outputFile.getName());
        } catch (IOException e){
            listener.onError("Error received file:" + e.getMessage());
            throw e;
        }
    }
}
