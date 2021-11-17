package com.bahriddin.appclickupcom.controller;

import com.bahriddin.appclickupcom.entity.User;
import com.bahriddin.appclickupcom.entity.Workspace;
import com.bahriddin.appclickupcom.payload.*;
import com.bahriddin.appclickupcom.security.CurrentUser;
import com.bahriddin.appclickupcom.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {
    @Autowired
    WorkspaceService workspaceService;

    @PostMapping
    public HttpEntity<?> addWorkspace(@Valid @RequestBody WorkspaceDTO workspaceDTO, @CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.addWorkspace(workspaceDTO, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * NAME, COLOR, AVATAR O'ZGARAISHI MUMKIN
     *
     * @param id
     * @param workspaceDTO
     * @return
     */
    @PutMapping("/{id}")
    public HttpEntity<?> editWorkspace(@PathVariable Long id, @RequestBody WorkspaceDTO workspaceDTO, @CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.editWorkspace(id, workspaceDTO, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * @param id
     * @param ownerId
     * @return
     */
    @PutMapping("/changeOwner/{id}")
    public HttpEntity<?> changeOwnerWorkspace(@PathVariable Long id,
                                              @RequestParam UUID ownerId, @RequestParam UUID newOwnerID, @RequestBody OwnerDTO ownerDTO) {
        ApiResponse apiResponse = workspaceService.changeOwnerWorkspace(id, ownerId, newOwnerID, ownerDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    /**
     * ISHXONANI O'CHIRISH
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteWorkspace(@PathVariable Long id) {
        ApiResponse apiResponse = workspaceService.deleteWorkspace(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/addOrEditOrRemove/{id}")
    public HttpEntity<?> addOrEditOrRemoveWorkspace(@PathVariable Long id,
                                                    @RequestBody MemberDTO memberDTO) {
        ApiResponse apiResponse = workspaceService.addOrEditOrRemoveWorkspace(id, memberDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/join")
    public HttpEntity<?> joinToWorkspace(@RequestParam Long id,
                                         @CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.joinToWorkspace(id, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    private HttpEntity<?> getAllWorkSpace(@CurrentUser User user) {
        List<WorkspaceDTO> list = workspaceService.workSpaceList(user);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/member/{id}")
    public HttpEntity<?> getMemberAndGuest(@PathVariable Long id) {
        List<MemberDTO> memberS = workspaceService.getMemberAndGuest(id);
        return ResponseEntity.ok(memberS);
    }

    @PutMapping("/addOrRemovePermission")
    private HttpEntity<?> addOrRemovePermissionToRole(@RequestBody WorkspaceRoleDTO workspaceRoleDTO) {

        ApiResponse list = workspaceService.addOrRemovePermissionToRole(workspaceRoleDTO);
        return ResponseEntity.status(list.isSuccess() ? 200 : 409).body(list);
    }


}
