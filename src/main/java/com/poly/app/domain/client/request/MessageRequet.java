package com.poly.app.domain.client.request;

import com.poly.app.infrastructure.constant.MessageStatus;
import com.poly.app.infrastructure.constant.SenderType;
import lombok.Data;

@Data

public class MessageRequet {
    private Integer conversationId;
    private Integer senderId;
    private String senderType;
    private Integer customerId;
    private String content;
    private String status;


}