package com.bahriddin.appclickupcom.repository;

import com.bahriddin.appclickupcom.entity.WorkspacePermission;
import com.bahriddin.appclickupcom.entity.enums.WorkspacePermissionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkspacePermissionRepository extends JpaRepository<WorkspacePermission, UUID> {
    Optional<WorkspacePermission> findByWorkspaceRoleIdAndPermission(UUID workspaceRole_id, WorkspacePermissionName permission);
}
