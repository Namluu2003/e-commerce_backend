package com.poly.app.domain.client.repository;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Integer id;
    private Integer customerId; // Để FE kiểm tra quyền chỉnh sửa
    private String fullName;
    private String comment;
    private Long createdAt;
    private Long updatedAt; // Để FE hiển thị thời gian cập nhật
    private String customerEmail;
    private String productName;
    private Integer productId; // Lưu cả product ID
    private Double rate;
    private String avatar; // Giữ trường avatar
    private String adminReply; // Thêm trường phản hồi của admin
    private Integer parentId; // Thêm trường parentId để quản lý trả lời
}
