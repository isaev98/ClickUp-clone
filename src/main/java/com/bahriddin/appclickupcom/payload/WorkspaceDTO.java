package com.bahriddin.appclickupcom.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class WorkspaceDTO {

    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String color;

    private UUID avatarId;

    private String initialLatter;

}
