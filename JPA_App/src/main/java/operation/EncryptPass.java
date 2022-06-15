package operation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptPass {

    public static String encryptPassword(String passphrase) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(passphrase.getBytes());
            byte[] passByte = md.digest();

            StringBuilder sb = new StringBuilder();

            for (byte b : passByte){
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);     // this should never happen though
        }
    }

}
