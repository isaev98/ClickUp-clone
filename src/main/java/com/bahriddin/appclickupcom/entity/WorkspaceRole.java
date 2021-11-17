package com.bahriddin.appclickupcom.entity;


import com.bahriddin.appclickupcom.entity.enums.WorkspaceRoleName;
import com.bahriddin.appclickupcom.entity.template.AbsUUIDEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkspaceRole extends AbsUUIDEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Workspace workspace;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private WorkspaceRoleName extendsRole;
}
