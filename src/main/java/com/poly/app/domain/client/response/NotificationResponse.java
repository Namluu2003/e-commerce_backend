package com.poly.app.domain.client.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String content;
    private Date createdAt;
    private Boolean isRead;


}