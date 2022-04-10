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
    private String AccountNumber;

    @NotNull(message = "Type cannot be null")
    private String Type;

    @NotNull(message = "Amount cannot be null")
    private String Amount;

    @NotNull(message = "Currency cannot be null")
    private String Currency;

    @NotNull(message = "Account from cannot be null")
    private String AccountFrom;
}
