package hu.ponte.hr.services.keyreader;

import hu.ponte.hr.exception.UnableToLoadKeyException;
import hu.ponte.hr.services.ImageStoreImpl;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

@Setter
public class KeyReaderImpl implements KeyReader{

    private final Logger logger = LoggerFactory.getLogger(KeyReaderImpl.class);
    private String algorithm;

    /**
     * algorithm have to be set before using this method
     */
    @Override
    public PublicKey loadPublicKey(String filePath) {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Unable to load public key");
            throw new UnableToLoadKeyException(e.getMessage());
        }
    }

    /**
     * algorithm have to be set before using this method
     */
    @Override
    public PrivateKey loadPrivateKey(String filePath) {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            return kf.generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Unable to load private key");
            throw new UnableToLoadKeyException(e.getMessage());
        }
    }
}
