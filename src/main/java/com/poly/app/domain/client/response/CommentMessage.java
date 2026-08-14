package com.poly.app.domain.client.response;

import lombok.Data;

@Data
public class CommentMessage {
    private Integer customerId;
    private String comment;
    private Integer parentId;
    private Double rate;
    private String action;
}

