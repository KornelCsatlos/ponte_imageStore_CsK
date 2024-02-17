package hu.ponte.hr.services.keyreader;

import hu.ponte.hr.exception.UnableToLoadKeyException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Keyreader {

    public static PublicKey loadPublicKey(String filename, String algorithm) {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new UnableToLoadKeyException(e);
        }
    }

    public static PrivateKey loadPrivateKey(String filename, String algorithm) {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            return kf.generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new UnableToLoadKeyException(e);
        }
    }
}
