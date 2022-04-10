package com.natwest.psuedo.queue.model;


import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Account number cannot be null")
    private String accountNumber;

    @NotNull(message = "Type cannot be null")
    private String type;

    @NotNull(message = "Amount cannot be null")
    private Long amount;

    @NotNull(message = "Currency cannot be null")
    private String currency;

    @NotNull(message = "Account from cannot be null")
    private String accountFrom;
}
