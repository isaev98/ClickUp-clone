package com.bahriddin.appclickupcom.service;

import com.bahriddin.appclickupcom.entity.Workspace;
import com.bahriddin.appclickupcom.entity.WorkspaceRole;
import com.bahriddin.appclickupcom.entity.enums.WorkspaceRoleName;
import com.bahriddin.appclickupcom.payload.ApiResponse;
import com.bahriddin.appclickupcom.payload.WorkspaceRoleDTO;
import com.bahriddin.appclickupcom.repository.WorkspaceRepository;
import com.bahriddin.appclickupcom.repository.WorkspaceRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkspaceRoleService {
    @Autowired
    WorkspaceRoleRepository workspaceRoleRepository;

    @Autowired
    WorkspaceRepository workspaceRepository;

    public ApiResponse addRoleWorkspace(WorkspaceRoleDTO workspaceRoleDTO) {
        boolean b = workspaceRoleRepository.existsByExtendsRole(workspaceRoleDTO.getWorkspaceRoleName());
        if (b) return new ApiResponse("Role name already exist", false);

        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(workspaceRoleDTO.getWorkspaceId());
        if (!optionalWorkspace.isPresent()) return new ApiResponse("Workspace not found", false);

        WorkspaceRole workspaceRole = new WorkspaceRole();
        workspaceRole.setWorkspace(optionalWorkspace.get());
        workspaceRole.setName(workspaceRoleDTO.getName());
        workspaceRole.setExtendsRole(workspaceRoleDTO.getWorkspaceRoleName());

        workspaceRoleRepository.save(workspaceRole);
        return new ApiResponse("Saved", true);

    }
}
