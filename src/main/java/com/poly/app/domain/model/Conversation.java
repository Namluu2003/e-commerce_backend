package com.poly.app.domain.model;

import com.poly.app.domain.client.response.LastMesage;
import com.poly.app.domain.model.base.PrimaryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "conversation")
public class Conversation extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;
    @Transient // Không lưu vào database
    private LastMesage lastMessage; // Tin nhắn gần nhất (tùy chọn)

}