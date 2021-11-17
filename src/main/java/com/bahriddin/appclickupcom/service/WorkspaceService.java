package com.bahriddin.appclickupcom.service;


import com.bahriddin.appclickupcom.entity.User;
import com.bahriddin.appclickupcom.payload.*;

import java.util.List;
import java.util.UUID;


public interface WorkspaceService {

    ApiResponse addWorkspace(WorkspaceDTO workspaceDTO, User user);

    ApiResponse editWorkspace(Long id,WorkspaceDTO workspaceDTO, User user);

    ApiResponse changeOwnerWorkspace(Long id, UUID ownerId,UUID newOwnerID, OwnerDTO ownerDTO);

    ApiResponse deleteWorkspace(Long id);

    ApiResponse addOrEditOrRemoveWorkspace(Long id, MemberDTO memberDTO);

    ApiResponse joinToWorkspace(Long id, User user);


    List<MemberDTO> getMemberAndGuest(Long id);

    List<WorkspaceDTO> workSpaceList(User user);

    ApiResponse addOrRemovePermissionToRole(WorkspaceRoleDTO workspaceRoleDTO);
}
