package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "type")
//loai
public class Type extends PrimaryEntity implements Serializable {


    String typeName;

    Status status;

}
