package com.poly.app.domain.model;


import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "role")
public class Role extends PrimaryEntity implements Serializable {
    private  String roleName;

    Status status;
}
