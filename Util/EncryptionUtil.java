package Util;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.*;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String KEYSTORE_FILE ="mykeystore.jks";
    private static final String KEY_ALIAS = "chatappkey";
    private static final String STORE_PASSWORD = "strongpassword";
    private static final int BUFFER_SIZE = 4 * 1024 * 1024;//4 MB buffer

    //generate AES key(run once to create a key file)
    public static void generateKey()throws Exception{
        KeyStore keyStore = KeyStore.getInstance("JKS");
        File KsFile = new File(KEYSTORE_FILE);
        if(KsFile.exists()){
            try(FileInputStream fis = new FileInputStream(KEYSTORE_FILE)){
                keyStore.load(fis, STORE_PASSWORD.toCharArray());
            }
        }else{
            keyStore.load(null, STORE_PASSWORD.toCharArray());
        }

        //generate AES Key
        KeyGenerator KeyGen = KeyGenerator.getInstance(ALGORITHM);
        KeyGen.init(256);
        SecretKey secretKey = KeyGen.generateKey();

        //Files.write(Path.of(KEY_FILE), secretKey.getEncoded());
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter param = new KeyStore.PasswordProtection(STORE_PASSWORD.toCharArray());
        keyStore.setEntry(KEY_ALIAS, entry, param);

        //save KeyStore
        try(FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)){
            keyStore.store(fos, STORE_PASSWORD.toCharArray());

        }
    }

    //load AES key from KeyStore
    private static SecretKey getKey() throws Exception{

        KeyStore keyStore = KeyStore.getInstance("JKS");

        try(FileInputStream fis = new FileInputStream(KEYSTORE_FILE)){
            keyStore.load(fis, STORE_PASSWORD.toCharArray());
        }

        KeyStore.ProtectionParameter param = new KeyStore.PasswordProtection(STORE_PASSWORD.toCharArray());
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry)
        keyStore.getEntry(KEY_ALIAS, param);
        return entry.getSecretKey();   
      
    }

    //encypt large file in chunks
    public static void encryptFile(File inputFile, File outputFile)throws Exception{

        SecretKey secretKey = getKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
        CipherOutputStream cos = new CipherOutputStream(bos, cipher)
        ){
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                cos.write(buffer, 0 , bytesRead);
                
            }
        }
    }

    //decrypt file in chunks
    public static void decryptFile(File inputFile, File outputFile)throws Exception{
        SecretKey secretKey = getKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
        CipherInputStream cis = new CipherInputStream(bis, cipher);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))
        ){
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                
            }
        }
    }
    
}
