package com.natwest.psuedo.queue.controller;

import com.natwest.psuedo.queue.constants.PsuedoQueueConstants;
import com.natwest.psuedo.queue.entity.TransactionEntity;
import com.natwest.psuedo.queue.model.Transaction;
import com.natwest.psuedo.queue.service.PsuedoQueueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/psuedo/queue")
@Validated
public class PsuedoQueueController {

    @Autowired
    private PsuedoQueueHandler psuedoQueueHandler;

    @PostMapping("/receive/data")
    public ResponseEntity<?> receivePayload(@RequestBody String base64encoded) {
        try {
            Transaction transaction = psuedoQueueHandler.getDecryptedData(base64encoded);

            TransactionEntity transactionEntity = psuedoQueueHandler.insertTransactionObject(transaction);

            return new ResponseEntity<>(transactionEntity.getTransactionId(), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/send/data")
    public ResponseEntity<?> sendPayload(@Valid @RequestBody Transaction transaction) {
        try {
            psuedoQueueHandler.encryptData(transaction);
            return new ResponseEntity<>(PsuedoQueueConstants.SUCCESS_MSG, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}