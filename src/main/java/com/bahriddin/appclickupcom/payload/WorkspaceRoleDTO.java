package com.bahriddin.appclickupcom.payload;

import com.bahriddin.appclickupcom.entity.WorkspacePermission;
import com.bahriddin.appclickupcom.entity.enums.AddType;
import com.bahriddin.appclickupcom.entity.enums.WorkspacePermissionName;
import com.bahriddin.appclickupcom.entity.enums.WorkspaceRoleName;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WorkspaceRoleDTO {

    @NotNull
    private Long workspaceId;

    @NotNull
    private String name;

    @NotNull
    private WorkspaceRoleName workspaceRoleName;

    private WorkspacePermissionName permissionName;

    private AddType addType;
}
