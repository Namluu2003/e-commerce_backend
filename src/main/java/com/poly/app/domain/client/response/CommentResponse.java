package com.poly.app.domain.client.response;

import com.poly.app.domain.model.Comments;
import com.poly.app.infrastructure.constant.CommentsStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CommentResponse {
    private Integer id;
    private Integer productId;
    private Integer customerId;
    private String comments;
    private Double rate;
    private CommentsStatus status;
    private Integer parentId;
    private String createdBy;
    private LocalDateTime createdAt;
    private String productName;
    private String customerName;


}

