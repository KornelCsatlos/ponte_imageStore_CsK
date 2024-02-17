package hu.ponte.hr.services.keyreader;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyReader {

    PublicKey loadPublicKey(String filePath);
    PrivateKey loadPrivateKey(String filePath);
}
