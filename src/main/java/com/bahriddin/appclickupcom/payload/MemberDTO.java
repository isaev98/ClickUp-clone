package com.bahriddin.appclickupcom.payload;

import com.bahriddin.appclickupcom.entity.enums.AddType;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class MemberDTO {
    private UUID id;

    private String fullName;

    private String email;

    private String roleName;

    private Timestamp lastActive;

    private UUID roleId;

    private AddType addType;//ADD, EDIT, REMOVE
}
