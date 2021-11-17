package com.bahriddin.appclickupcom.repository;

import com.bahriddin.appclickupcom.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}
