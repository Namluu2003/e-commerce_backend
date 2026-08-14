package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import jakarta.persistence.*;
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
@Table(name = "announcement")
//Thông báo
public class Announcement extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn
    Customer customer;

    String announcementCode;
//    nội dung thông báo
    String announcementContent;
    Boolean isRead = false;

}
