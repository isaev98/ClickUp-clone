package com.bahriddin.appclickupcom.repository;

import com.bahriddin.appclickupcom.entity.WorkspaceRole;
import com.bahriddin.appclickupcom.entity.enums.WorkspaceRoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRoleRepository extends JpaRepository<WorkspaceRole, UUID> {
    boolean existsByExtendsRole(WorkspaceRoleName extendsRole);

    Optional<WorkspaceRole> findById(Long workspaceId);
}
