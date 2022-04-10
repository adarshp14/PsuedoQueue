package com.natwest.psuedo.queue.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Configuration
public class PsuedoQueueConfig {

    private static final String AES = "AES";

    private static final String PBKDF2 = "PBKDF2WithHmacSHA256";

    @Value("${key.salt.value}")
    private String salt;

    @Value("${key.password.value}")
    private String password;

    @Value("${key.iteration.value}")
    private int iterationCount;

    @Value("${key.length.value}")
    private int keyLength;

    public Cipher initializeCipher(SecretKey key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher;
    }

    public SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterationCount, keyLength);
        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), AES);
        return key;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
