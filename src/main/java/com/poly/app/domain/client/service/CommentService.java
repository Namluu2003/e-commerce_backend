package com.poly.app.domain.client.service;

import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.domain.client.repository.CommentDTO;
import com.poly.app.domain.client.response.CommentMessage;
import com.poly.app.domain.model.*;
import com.poly.app.domain.repository.CommentsRepository;
import com.poly.app.domain.repository.CustomerRepository;
import com.poly.app.domain.repository.ProductRepository;
import com.poly.app.infrastructure.constant.DiscountType;
import com.poly.app.infrastructure.constant.VoucherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void saveComment(Integer productId, Integer customerId, String commentText, Double rate, Integer parentId, String action) {
        CommentDTO commentDTO =null;
        if (!action.equalsIgnoreCase("update")) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

            // Kiểm tra nếu là bình luận gốc, chỉ cho phép một bình luận trên sản phẩm
            if (parentId == null) {
                boolean hasCommented = commentsRepository.existsByProductIdAndCustomerId(productId, customerId);
                if (hasCommented) {
                    throw new RuntimeException("Bạn chỉ được bình luận một lần trên sản phẩm này!");
                }
            }

            Comments comment = new Comments();
            comment.setProduct(product);
            comment.setCustomer(customer);
            comment.setComment(commentText);
            comment.setRate(rate != null ? rate : 5.0);
            comment.setCreatedAt(System.currentTimeMillis());
            comment.setUpdatedAt(null); // Khi tạo mới, `updatedAt` phải là null
            comment.setParentId(parentId); // Lưu parentId dưới dạng Integer
            comment.setAdminReply(null);

            Comments savedComment = commentsRepository.save(comment);

             commentDTO = new CommentDTO(
                    savedComment.getId(),
                    customer.getId(),
                    customer.getFullName(),
                    commentText,
                    savedComment.getCreatedAt(),
                    savedComment.getUpdatedAt(),
                    customer.getEmail(),
                    product.getProductName(),
                    product.getId(),
                    savedComment.getRate(),
                    customer.getAvatar(),
                    null, // Admin chưa phản hồi
                    parentId // Trả về parentId nếu có
            );
        }else{
            commentDTO= updateComment(productId,
                    customerId,
                    parentId,
                    commentText,
                    rate);
        }


        messagingTemplate.convertAndSend("/topic/comments/" + productId, commentDTO);
        messagingTemplate.convertAndSend("/topic/admin/noticomments", "thong bao");


    }

    // Cập nhật bình luận
    public CommentDTO updateComment(Integer productId, Integer customerId, Integer commentId, String commentText, Double rate) {
        // Tìm bình luận hiện có
        Comments existingComment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Bình luận không tồn tại"));

        // Kiểm tra quyền chỉnh sửa
        if (!existingComment.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Bạn không thể chỉnh sửa bình luận của người khác!");
        }

        // Kiểm tra productId có khớp không
        if (!existingComment.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Product ID không khớp với bình luận!");
        }

        // Kiểm tra nội dung bình luận
        if (commentText == null || commentText.trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung bình luận không được để trống!");
        }

        // Cập nhật thông tin
        existingComment.setComment(commentText);
        existingComment.setRate(rate != null ? rate : existingComment.getRate());
        existingComment.setUpdatedAt(System.currentTimeMillis());
        System.out.println("existingComment2" + existingComment);
        // Lưu vào cơ sở dữ liệu
        Comments updatedComment = commentsRepository.save(existingComment);

        // Tạo DTO để gửi qua WebSocket
        CommentDTO commentDTO = new CommentDTO(
                updatedComment.getId(),
                customerId,
                existingComment.getCustomer().getFullName(),
                commentText,
                updatedComment.getCreatedAt(),
                updatedComment.getUpdatedAt(),
                existingComment.getCustomer().getEmail(),
                existingComment.getProduct().getProductName(),
                existingComment.getProduct().getId(),
                updatedComment.getRate(),
                existingComment.getCustomer().getAvatar(),
                updatedComment.getAdminReply(), // Giữ nguyên adminReply hiện tại
                updatedComment.getParentId()
        );

        // Gửi thông điệp WebSocket
        try {
            messagingTemplate.convertAndSend("/topic/comments/" + productId, commentDTO);
        } catch (Exception e) {
            System.err.println("Failed to send WebSocket message: " + e.getMessage());
            // Có thể thêm logic rollback nếu cần
        }

        return commentDTO;
    }


    // Trả lời bình luận (Admin)
    public CommentDTO replyToComment(CommentMessage message) {
        // Kiểm tra dữ liệu đầu vào
        if (message == null || message.getParentId() == null || message.getComment() == null || message.getComment().trim().isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ hoặc thiếu thông tin!");
        }

        // Tìm bình luận cha
        Comments parentComment = commentsRepository.findById(message.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("Bình luận không tồn tại với ID: " + message.getParentId()));

        // Cập nhật phản hồi admin
        parentComment.setAdminReply(message.getComment());
        parentComment.setUpdatedAt(System.currentTimeMillis());

        // Lưu vào cơ sở dữ liệu
        Comments updatedComment = commentsRepository.save(parentComment);

        // Tạo DTO cho phản hồi
        CommentDTO replyDTO = new CommentDTO(
                updatedComment.getId(),
                updatedComment.getCustomer() != null ? updatedComment.getCustomer().getId() : null,
                updatedComment.getCustomer() != null ? updatedComment.getCustomer().getFullName() : "Khách hàng",
                updatedComment.getComment(),
                updatedComment.getCreatedAt(),
                updatedComment.getUpdatedAt(),
                updatedComment.getCustomer() != null ? updatedComment.getCustomer().getEmail() : "admin@poly.app",
                updatedComment.getProduct().getProductName(),
                updatedComment.getProduct().getId(),
                updatedComment.getRate(),
                updatedComment.getCustomer() != null ? updatedComment.getCustomer().getAvatar() : null,
                message.getComment(), // Phản hồi admin
                updatedComment.getParentId() != null ? updatedComment.getParentId() : updatedComment.getId()
        );

        // Gửi thông điệp WebSocket
        try {
            messagingTemplate.convertAndSend("/topic/comments/" + updatedComment.getProduct().getId(), replyDTO);
        } catch (Exception e) {
            System.err.println("Failed to send WebSocket message: " + e.getMessage());
            // Có thể thêm logic thông báo lỗi nếu cần
        }

        return replyDTO;
    }


    // Xóa bình luận (Admin)
    public void deleteCommentByAdmin(Integer commentId) {
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Bình luận không tồn tại"));

        Integer productId = comment.getProduct() != null ? comment.getProduct().getId() : null;

        // Xóa tất cả câu trả lời liên quan đến bình luận này (nếu có)
        List<Comments> replies = commentsRepository.findAllByParentId(commentId);
        commentsRepository.deleteAll(replies);

        // Xóa bình luận chính
        commentsRepository.delete(comment);

        // Gửi thông báo qua WebSocket để xóa bình luận trên Client
        if (productId != null) {
            messagingTemplate.convertAndSend("/topic/comments/" + productId, "deleted:" + commentId);
        }
    }

    // Lấy tất cả bình luận
//    public List<CommentDTO> getAllComments() {
//        return commentsRepository.findAllByOrderByCreatedAtAsc()
//                .stream()
//                .map(comment -> new CommentDTO(
//                        comment.getId(),
//                        comment.getCustomer() != null ? comment.getCustomer().getId() : null,
//                        comment.getCustomer() != null ? comment.getCustomer().getFullName() : "Trả lời từ người bán",
//                        comment.getComment(),
//                        comment.getCreatedAt(),
//                        comment.getUpdatedAt(),
//                        comment.getCustomer() != null ? comment.getCustomer().getEmail() : "admin@poly.app",
//                        comment.getProduct() != null ? comment.getProduct().getProductName() : "Unknown Product",
//                        comment.getProduct() != null ? comment.getProduct().getId() : null,
//                        comment.getRate(),
//                        comment.getCustomer() != null ? comment.getCustomer().getAvatar() : null,
//                        comment.getAdminReply(),
//                        comment.getParentId()
//                ))
//                .collect(Collectors.toList());
//    }
    public Page<CommentDTO> getAllComments(Pageable pageable) {
        return commentsRepository.findAll(pageable)
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getCustomer() != null ? comment.getCustomer().getId() : null,
                        comment.getCustomer() != null ? comment.getCustomer().getFullName() : "Trả lời từ người bán",
                        comment.getComment(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt(),
                        comment.getCustomer() != null ? comment.getCustomer().getEmail() : "admin@poly.app",
                        comment.getProduct() != null ? comment.getProduct().getProductName() : "Unknown Product",
                        comment.getProduct() != null ? comment.getProduct().getId() : null,
                        comment.getRate(),
                        comment.getCustomer() != null ? comment.getCustomer().getAvatar() : null,
                        comment.getAdminReply(),
                        comment.getParentId()
                ));
    }


    //Tìm kiếm
//    public List<CommentDTO> searchCommentsByProductNameAndCreatedAt(String productName, Long createdAt) {
//        Long fromDate = null;
//        Long toDate = null;
//
//        if (createdAt != null) {
//            LocalDate date = Instant.ofEpochMilli(createdAt)
//                    .atZone(ZoneId.systemDefault()) // Chuyển sang múi giờ hiện tại
//                    .toLocalDate();
//
//            fromDate = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
//            toDate = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;
//        }
//
//        List<Comments> comments = commentsRepository.findByProductNameAndCreatedAtBetween(productName, fromDate, toDate);
//
//        return comments.stream().map(comment -> new CommentDTO(
//                comment.getId(),
//                comment.getCustomer() != null ? comment.getCustomer().getId() : null,
//                comment.getCustomer() != null ? comment.getCustomer().getFullName() : "Trả lời từ người bán",
//                comment.getComment(),
//                comment.getCreatedAt(),
//                comment.getUpdatedAt(),
//                comment.getCustomer() != null ? comment.getCustomer().getEmail() : "admin@poly.app",
//                comment.getProduct() != null ? comment.getProduct().getProductName() : "Unknown Product",
//                comment.getProduct() != null ? comment.getProduct().getId() : null,
//                comment.getRate(),
//                comment.getCustomer() != null ? comment.getCustomer().getAvatar() : null,
//                comment.getAdminReply(),
//                comment.getParentId()
//        )).collect(Collectors.toList());
//    }
    public Page<CommentDTO> searchCommentsByProductName(String productName, String search, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Comments> spec = Specification.where(null);

        if (productName != null && !productName.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("product").get("productName")), "%" + productName.toLowerCase() + "%"));
        }

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("comment")), "%" + search.toLowerCase() + "%"));
        }

        Page<Comments> comments = commentsRepository.findAll(spec, pageable);

        return comments.map(comment -> new CommentDTO(
                comment.getId(),
                comment.getCustomer() != null ? comment.getCustomer().getId() : null,
                comment.getCustomer() != null ? comment.getCustomer().getFullName() : "Trả lời từ người bán",
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getCustomer() != null ? comment.getCustomer().getEmail() : "admin@poly.app",
                comment.getProduct() != null ? comment.getProduct().getProductName() : "Unknown Product",
                comment.getProduct() != null ? comment.getProduct().getId() : null,
                comment.getRate(),
                comment.getCustomer() != null ? comment.getCustomer().getAvatar() : null,
                comment.getAdminReply(),
                comment.getParentId()
        ));
    }




    public List<String> getProductNames() {
        return commentsRepository.findAllByOrderByCreatedAtAsc()
                .stream()
                .filter(comment -> comment.getProduct() != null)
                .map(comment -> comment.getProduct().getProductName())
                .distinct() // Remove duplicates
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getAllComments(Integer productId) {
        return commentsRepository.findAllByProductIdOrderByCreatedAtAsc(productId)
                .stream()
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getCustomer() != null ? comment.getCustomer().getId() : null,
                        comment.getCustomer() != null ? comment.getCustomer().getFullName() : "Trả lời từ người bán",
                        comment.getComment(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt(),
                        comment.getCustomer() != null ? comment.getCustomer().getEmail() : "admin@poly.app",
                        comment.getProduct() != null ? comment.getProduct().getProductName() : "Unknown Product",
                        comment.getProduct() != null ? comment.getProduct().getId() : null,
                        comment.getRate(),
                        comment.getCustomer() != null ? comment.getCustomer().getAvatar() : null,
                        comment.getAdminReply(),
                        comment.getParentId()
                ))
                .collect(Collectors.toList());
    }

    public List<String> getProductNames(Integer productId) {
        return commentsRepository.findAllByProductIdOrderByCreatedAtAsc(productId)
                .stream()
                .filter(comment -> comment.getProduct() != null)
                .map(comment -> comment.getProduct().getProductName())
                .distinct() // Remove duplicates
                .collect(Collectors.toList());
    }


}
