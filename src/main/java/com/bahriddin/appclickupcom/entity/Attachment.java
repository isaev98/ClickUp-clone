package com.bahriddin.appclickupcom.entity;


import com.bahriddin.appclickupcom.entity.template.AbsUUIDEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Attachment extends AbsUUIDEntity {

    private String name;

    private String originalName;

    private Long size;

    private String contentType;

}
