package com.poly.app.infrastructure.listener;


import com.poly.app.domain.common.Helpers;
import com.poly.app.domain.model.base.AuditEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.experimental.Helper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Calendar;

public class AuditEntityListener {

    @PrePersist
    private void onCreate(AuditEntity entity) {
        entity.setCreatedAt(getLongDate());
        entity.setUpdatedAt(getLongDate());
        entity.setCode(Helpers.genCodeUUID());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            entity.setUpdatedBy(authentication.getPrincipal().toString());
            entity.setCreatedBy(authentication.getPrincipal().toString());
        }
    }


    @PreUpdate
    private void onUpdate(AuditEntity entity) {
        entity.setUpdatedAt(getLongDate());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String updatedBy = authentication.getPrincipal().toString();
            entity.setUpdatedBy(updatedBy);
        }
    }


    private Long getLongDate() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
