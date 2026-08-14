package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.MessageStatus;
import com.poly.app.infrastructure.constant.SenderType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "messages")
public class Message extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(name = "sender_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    @Column(name = "sender_id", nullable = false)
    private Integer senderId;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;



}