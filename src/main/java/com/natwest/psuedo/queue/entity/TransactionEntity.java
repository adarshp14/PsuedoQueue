package com.natwest.psuedo.queue.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TRANSACTION")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @Column(name = "TRANSACTION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @Column(name = "ACCOUNT_NUMBER")
    private String AccountNumber;

    @Column(name = "TYPE")
    private String Type;

    @Column(name = "AMOUNT")
    private String Amount;

    @Column(name = "CURRENCY")
    private String Currency;

    @Column(name = "ACCOUNT_FROM")
    private String AccountFrom;

}
