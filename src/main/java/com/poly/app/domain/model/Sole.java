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
@Table(name = "sole")
//đế giày
public class Sole extends PrimaryEntity implements Serializable {


    String soleName;

    Status status;

}
