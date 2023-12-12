package com.bilalkristiania.AMQPOrder;

import lombok.Value;

import jakarta.validation.constraints.*;
// not in use right now, but makes it so these fields have constraits on them.
// can also maybe make this a record
// the DTO exists so i can do validation
@Value
public class OrderDTO {
    @NotNull
    Long id;
    @FutureOrPresent
    String date;
    //might also want to make status boolean, then we can do asserttrue or assertfalse if needed
    @NotBlank
    String status;
    @NotBlank
    String name;
    @NotBlank
    String amount;
}
