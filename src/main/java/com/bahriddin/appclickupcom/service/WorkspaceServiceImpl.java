package com.bahriddin.appclickupcom.service;

import com.bahriddin.appclickupcom.entity.*;
import com.bahriddin.appclickupcom.entity.enums.AddType;
import com.bahriddin.appclickupcom.entity.enums.WorkspacePermissionName;
import com.bahriddin.appclickupcom.entity.enums.WorkspaceRoleName;
import com.bahriddin.appclickupcom.payload.*;
import com.bahriddin.appclickupcom.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    WorkspaceUserRepository workspaceUserRepository;
    @Autowired
    WorkspaceRoleRepository workspaceRoleRepository;
    @Autowired
    WorkspacePermissionRepository workspacePermissionRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse addWorkspace(WorkspaceDTO workspaceDTO, User user) {
        //WORKSPACE OCHDIK
        if (workspaceRepository.existsByOwnerIdAndName(user.getId(), workspaceDTO.getName()))
            return new ApiResponse("Sizda bunday nomli ishxona mavjud", false);
        Workspace workspace = new Workspace(
                workspaceDTO.getName(),
                workspaceDTO.getColor(),
                user,
                workspaceDTO.getAvatarId() == null ? null : attachmentRepository.findById(workspaceDTO.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("attachment"))
        );
        workspaceRepository.save(workspace);

        //WORKSPACE ROLE OCHDIK
        WorkspaceRole ownerRole = workspaceRoleRepository.save(new WorkspaceRole(
                workspace,
                WorkspaceRoleName.ROLE_OWNER.name(),
                null
        ));

        WorkspaceRole adminRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_ADMIN.name(), null));
        WorkspaceRole memberRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_MEMBER.name(), null));
        WorkspaceRole guestRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_GUEST.name(), null));


        //OWERGA HUQUQLARNI BERYAPAMIZ
        WorkspacePermissionName[] workspacePermissionNames = WorkspacePermissionName.values();
        List<WorkspacePermission> workspacePermissions = new ArrayList<>();

        for (WorkspacePermissionName workspacePermissionName : workspacePermissionNames) {
            WorkspacePermission workspacePermission = new WorkspacePermission(
                    ownerRole,
                    workspacePermissionName);
            workspacePermissions.add(workspacePermission);

            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_ADMIN)) {
                workspacePermissions.add(new WorkspacePermission(
                        adminRole,
                        workspacePermissionName));
            }
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_MEMBER)) {
                workspacePermissions.add(new WorkspacePermission(
                        memberRole,
                        workspacePermissionName));
            }
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_GUEST)) {
                workspacePermissions.add(new WorkspacePermission(
                        guestRole,
                        workspacePermissionName));
            }

        }
        workspacePermissionRepository.saveAll(workspacePermissions);

        //WORKSPACE USER OCHDIK
        workspaceUserRepository.save(new WorkspaceUser(
                workspace,
                user,
                ownerRole,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())

        ));

        return new ApiResponse("WorkSpace saved", true);
    }

    @Override
    public ApiResponse editWorkspace(Long id, WorkspaceDTO workspaceDTO, User user) {
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(id);
        if (!optionalWorkspace.isPresent()) return new ApiResponse("Workspace not found", false);

        if (workspaceRepository.existsByOwnerIdAndName(user.getId(), workspaceDTO.getName()))
            return new ApiResponse("Sizda bunday nomli ishxona mavjud", false);
        Workspace workspace = optionalWorkspace.get();
        workspace.setName(workspaceDTO.getName());
        workspace.setColor(workspaceDTO.getColor());
        workspace.setOwner(user);
        workspace.setAvatar(workspaceDTO.getAvatarId() == null ? null : attachmentRepository.findById(workspaceDTO.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("attachment")));
        workspaceRepository.save(workspace);

        return new ApiResponse("WorkSpace edited", true);
    }

    @Override
    public ApiResponse changeOwnerWorkspace(Long id, UUID ownerId, UUID newOwnerId, OwnerDTO ownerDTO) {
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(id);
        if (!optionalWorkspace.isPresent()) return new ApiResponse("Workspace not found", false);
        Workspace workspace = optionalWorkspace.get();
        User owner = optionalWorkspace.get().getOwner();
        owner.setId(ownerDTO.getUuid());
        owner.setFullName(ownerDTO.getFullName());
        owner.setEmail(ownerDTO.getEmail());
        owner.setPassword(passwordEncoder.encode(ownerDTO.getPassword()));
        owner.setColor(ownerDTO.getColor());
        owner.setEnabled(true);
        workspace.setOwner(owner);
        return new ApiResponse("Owner changed", true);
    }

    @Override
    public ApiResponse deleteWorkspace(Long id) {
        try {
            workspaceRepository.deleteById(id);
            return new ApiResponse("Deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    @Override
    public ApiResponse addOrEditOrRemoveWorkspace(Long id, MemberDTO memberDTO) {
        if (memberDTO.getAddType().equals(AddType.ADD)) {
            WorkspaceUser workspaceUser = new WorkspaceUser(
                    workspaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id")),
                    userRepository.findById(memberDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                    workspaceRoleRepository.findById(memberDTO.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                    new Timestamp(System.currentTimeMillis()),
                    null
            );
            workspaceUserRepository.save(workspaceUser);

            //TODO EMAILGA INVITE XABAR YUBORISH
        } else if (memberDTO.getAddType().equals(AddType.EDIT)) {
            WorkspaceUser workspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, memberDTO.getId()).orElseGet(WorkspaceUser::new);
            workspaceUser.setWorkspaceRole(workspaceRoleRepository.findById(memberDTO.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")));
            workspaceUserRepository.save(workspaceUser);
        } else if (memberDTO.getAddType().equals(AddType.REMOVE)) {
            workspaceUserRepository.deleteByWorkspaceIdAndUserId(id, memberDTO.getId());
        }
        return new ApiResponse("Success", true);
    }

    @Override
    public ApiResponse joinToWorkspace(Long id, User user) {
        Optional<WorkspaceUser> optionalWorkspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, user.getId());
        if (optionalWorkspaceUser.isPresent()) {
            WorkspaceUser workspaceUser = optionalWorkspaceUser.get();
            workspaceUser.setDateJoined(new Timestamp(System.currentTimeMillis()));
            workspaceUserRepository.save(workspaceUser);
            return new ApiResponse("Success", true);
        }
        return new ApiResponse("Error", false);
    }


    @Override
    public List<MemberDTO> getMemberAndGuest(Long id) {
        List<WorkspaceUser> workspaceUser = workspaceUserRepository.findAllByWorkspaceId(id);
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (WorkspaceUser user : workspaceUser) {
            memberDTOList.add(mapWorkspaceUserToMember(user));
        }

        return memberDTOList;
    }

    @Override
    public List<WorkspaceDTO> workSpaceList(User user) {
        List<WorkspaceUser> byUserId = workspaceUserRepository.findAllByUserId(user.getId());
        return byUserId.stream().map(workspaceUser -> mapWorkspaceDTOUser(workspaceUser.getWorkspace())).collect(Collectors.toList());
    }

    @Override
    public ApiResponse addOrRemovePermissionToRole(WorkspaceRoleDTO workspaceRoleDTO) {
        WorkspaceRole workspaceRole = workspaceRoleRepository.findById(workspaceRoleDTO.getWorkspaceId()).orElseThrow(() -> new ResourceNotFoundException("worlspaceRole"));
        Optional<WorkspacePermission> optionalWorkspacePermission = workspacePermissionRepository.findByWorkspaceRoleIdAndPermission(workspaceRole.getId(), workspaceRoleDTO.getPermissionName());

        if (workspaceRoleDTO.getAddType().equals(AddType.ADD)) {
            if (optionalWorkspacePermission.isPresent()) {
                return new ApiResponse("Already exist", false);
            }
            WorkspacePermission workspacePermission = new WorkspacePermission(workspaceRole, workspaceRoleDTO.getPermissionName());
            workspacePermissionRepository.save(workspacePermission);
            return new ApiResponse("Success", true);
        } else if (workspaceRoleDTO.getAddType().equals(AddType.REMOVE)) {
            if (optionalWorkspacePermission.isPresent()) {
                workspacePermissionRepository.delete(optionalWorkspacePermission.get());
                return new ApiResponse("Success", true);
            }
            return new ApiResponse("Not found", false);
            

        }
        return null;
    }

    public WorkspaceDTO mapWorkspaceDTOUser(Workspace workspace) {
        WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setId(workspace.getId());
        workspaceDTO.setColor(workspace.getColor());
        workspaceDTO.setAvatarId(workspace.getAvatar() == null ? null : workspace.getAvatar().getId());
        workspaceDTO.setName(workspace.getName());
        workspaceDTO.setInitialLatter(workspace.getInitialLetter());
        return workspaceDTO;
    }

    public MemberDTO mapWorkspaceUserToMember(WorkspaceUser workspaceUser) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(workspaceUser.getUser().getId());
        memberDTO.setFullName(workspaceUser.getUser().getFullName());
        memberDTO.setEmail(workspaceUser.getUser().getEmail());
        memberDTO.setRoleName(workspaceUser.getWorkspaceRole().getName());
        memberDTO.setLastActive(workspaceUser.getUser().getLastActive());
        return memberDTO;
    }
}
