package com.natwest.psuedo.queue.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TRANSACTION")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class transactionEntity {

    @Id
    @Column(name = "TRANSACTION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "ACCOUNT_FROM")
    private String accountFrom;

}
