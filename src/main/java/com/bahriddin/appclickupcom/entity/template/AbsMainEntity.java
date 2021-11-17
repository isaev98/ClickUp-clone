package com.bahriddin.appclickupcom.entity.template;

import com.bahriddin.appclickupcom.entity.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbsMainEntity {

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @JoinColumn(updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private User updatedBy;
}
