package zgoo.app.util;

import java.security.MessageDigest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptionUtils {
     public static String encryptSHA256(String s) {
        return encrypt(s, "SHA-256");
    }

    public static String encrypt(String s, String messageDigest) {
        try {
            MessageDigest md = MessageDigest.getInstance(messageDigest);
            byte[] passBytes = s.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toString((digested[i]&0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            // log.error("encrypt error", e);
            e.printStackTrace();
            return s;
        }
    }
}
