package com.natwest.psuedo.queue.service;

import com.natwest.psuedo.queue.config.PsuedoQueueConfig;
import com.natwest.psuedo.queue.entity.TransactionEntity;
import com.natwest.psuedo.queue.model.Transaction;
import com.natwest.psuedo.queue.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Service
@Component
public class PsuedoQueueHandler {

    @Autowired
    private TransactionRepository transRepo;

    @Autowired
    private PsuedoQueueConfig aesConfig;


    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.url.receive.data.host}")
    private String receiveDataHost;

    public Transaction getDecryptedData(String base64Encoded)
            throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        SecretKey key = aesConfig.generateKey();

        Cipher cipher = aesConfig.initializeCipher(key);
        ByteArrayInputStream input
                = new ByteArrayInputStream(Base64.getDecoder().decode(base64Encoded));
        CipherInputStream cipherInputStream = new CipherInputStream(input, cipher);
        ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
        SealedObject sealedObject = (SealedObject) inputStream.readObject();
        cipherInputStream.close();
        return (Transaction) sealedObject.getObject(cipher);
    }

    public TransactionEntity insertTransactionObject(Transaction transaction) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccountNumber(transaction.getAccountNumber());
        transactionEntity.setType(transaction.getType());
        transactionEntity.setAmount(transaction.getAmount());
        transactionEntity.setCurrency(transaction.getCurrency());
        transactionEntity.setAccountFrom(transaction.getAccountFrom());
        return transRepo.save(transactionEntity);
    }

    @Async
    public void encryptData(Transaction transaction)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException,
            IOException, IllegalBlockSizeException {

        SecretKey key = aesConfig.generateKey();

        Cipher cipher = aesConfig.initializeCipher(key);

        SealedObject encrypted = new SealedObject((Serializable) transaction, cipher);

        sendToReceiver(encrypted, cipher);

    }

    private void sendToReceiver(SealedObject encrypted, Cipher cipher) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(cipherOutputStream);
        objectOutputStream.writeObject(encrypted);
        cipherOutputStream.close();

        byte[] values = outputStream.toByteArray();

        String base64encoded = Base64.getEncoder().encodeToString(values);

        HttpEntity<String> request = new HttpEntity<>(base64encoded, new HttpHeaders());

        restTemplate.postForObject(receiveDataHost, request, Integer.class);

    }
}
