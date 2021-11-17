package com.bahriddin.appclickupcom.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class OwnerDTO {

    @NotNull
    private UUID uuid;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String fullName;

    @NotNull
    private String color;
}
