package Networking;

public interface TransferListener {
    void onProgress(int percentage);
    void onComplete(String message);
    void onError(String errorMessage);
}
