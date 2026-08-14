package com.poly.app.domain.model;

import com.poly.app.domain.model.base.PrimaryEntity;
import com.poly.app.infrastructure.constant.CommentsStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comments extends PrimaryEntity implements Serializable {
    Integer id;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Đổi sang product
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true) // trường hợp quản lý trang trả lời
    private Customer customer;
    @Column(columnDefinition = "TEXT")
    private String comment;
    // max 5
    // lấy luôn trung bình của rate neeus chưa có rate thì là 5
    private Double rate;
    // Có parentId có nghĩ là tin nhắn này là trả lời
    // Comments nào có parentId  có nghĩa là của amdin trả lời thì email người trả lời trong created_by
    private Integer parentId;
    // Admin cần phải trả lời
    @Column(columnDefinition = "TEXT") // Hỗ trợ lưu phản hồi dài
    private String adminReply; // Thêm trường lưu phản hồi của admin

}