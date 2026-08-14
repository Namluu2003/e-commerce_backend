package com.poly.app.domain.client.request;

import lombok.Data;

@Data
public class ConversationRequest {
    private Integer customerId;
    private Integer staffId;
}