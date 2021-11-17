package com.bahriddin.appclickupcom.controller;

import com.bahriddin.appclickupcom.entity.Workspace;
import com.bahriddin.appclickupcom.payload.ApiResponse;
import com.bahriddin.appclickupcom.payload.WorkspaceRoleDTO;
import com.bahriddin.appclickupcom.service.WorkspaceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workSpaceRole")
public class WorkSpaceRoleController {

    @Autowired
    WorkspaceRoleService workspaceRoleService;

    @PostMapping
    public HttpEntity<?> addRole(@RequestBody WorkspaceRoleDTO workspaceDTO) {
        ApiResponse apiResponse = workspaceRoleService.addRoleWorkspace(workspaceDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);

    }
}
