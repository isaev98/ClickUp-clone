package com.bahriddin.appclickupcom.repository;

import com.bahriddin.appclickupcom.entity.WorkspaceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceUserRepository extends JpaRepository<WorkspaceUser, UUID> {
    Optional<WorkspaceUser> findByWorkspaceIdAndUserId(Long workspace_id, UUID user_id);
    Optional<WorkspaceUser> findByWorkspaceId(Long workspace_id);
    List<WorkspaceUser> findAllByWorkspaceId(Long workspace_id);

    List<WorkspaceUser> findAllByUserId(UUID user_id);

    @Transactional
    @Modifying
    void deleteByWorkspaceIdAndUserId(Long workspace_id, UUID user_id);
}
