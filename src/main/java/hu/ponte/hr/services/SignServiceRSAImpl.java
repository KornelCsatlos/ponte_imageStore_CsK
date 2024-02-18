package hu.ponte.hr.services;

import hu.ponte.hr.exception.UnableToCreateSignedHashException;
import hu.ponte.hr.exception.VerificationFailedException;
import hu.ponte.hr.services.keyreader.KeyReader;
import hu.ponte.hr.services.keyreader.KeyReaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;


@Service
public class SignServiceRSAImpl implements SignService {
    private final Logger logger = LoggerFactory.getLogger(SignServiceRSAImpl.class);
    public static final String ALGORITHM = "RSA";
    public static final String PRIVATE_KEY_PATH = "src/main/resources/config/keys/key.private";
    public static final String PUBLIC_KEY_PATH = "src/main/resources/config/keys/key.pub";
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public SignServiceRSAImpl() {
        KeyReader keyReader = new KeyReaderImpl();
        keyReader.setAlgorithm(ALGORITHM);
        privateKey = keyReader.loadPrivateKey(PRIVATE_KEY_PATH);
        publicKey = keyReader.loadPublicKey(PUBLIC_KEY_PATH);
    }

    /**
     * @return SHA256withRSA algorithm created signature encoded with Base64
     */
    @Override
    public String sign(String fileName) {
        return Base64.getEncoder().encodeToString(useAlgorithmSHA256WithRSA(fileName));
    }

    private byte[] useAlgorithmSHA256WithRSA(String fileName){
        try {
            //Hash the data
            byte[] hashedData = hashTheData(fileName);
            //Sign the hash
            Signature signature = getSignature(privateKey, hashedData);
            byte[] signedHash = signature.sign();
            //Verify the signature
            signature.initVerify(publicKey);
            signature.update(hashedData);
            if(!signature.verify(signedHash)){
                logger.warn("Signature verification failed");
                throw new VerificationFailedException("Signature verification failed");
            }
            logger.info("signed hash created successfully");
            return signedHash;
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            logger.warn("Unable to create signed hash for digital signature creation");
            throw new UnableToCreateSignedHashException(e.getMessage());
        }
    }

    private byte[] hashTheData(String fileName) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(fileName.getBytes(StandardCharsets.UTF_8));
    }

    private Signature getSignature(PrivateKey privateKey, byte[] hashedData) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(hashedData);
        return signature;
    }
}
