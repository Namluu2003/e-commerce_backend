//package com.poly.app.domain.client.service;
//
//import com.poly.app.domain.admin.voucher.response.VoucherReponse;
//import com.poly.app.domain.client.repository.CommentDTO;
//import com.poly.app.domain.model.Comments;
//import com.poly.app.domain.model.Customer;
//import com.poly.app.domain.model.Product;
//import com.poly.app.infrastructure.constant.CommentsStatus;
//import com.poly.app.domain.repository.CommentsRepository;
//import com.poly.app.domain.repository.CustomerRepository;
//import com.poly.app.domain.repository.ProductDetailRepository;
//import com.poly.app.domain.repository.ProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class CommentService {
//    @Autowired
//    ProductRepository productRepository;
//
//    @Autowired
//    private CommentsRepository commentRepository;
//
//    @Autowired
//    private ProductDetailRepository productDetailRepository;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    // Lưu bình luận mới từ khách hàng
//    public Comments saveComment(Integer productId, Integer customerId, String commentText, Double rate) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        Comments comment = new Comments();
//        comment.setProduct(product);
//        comment.setCustomer(customer);
//        comment.setComment(commentText);
//        comment.setRate(rate != null ? rate : 5.0); // Mặc định là 5 sao nếu không có
//        comment.setStatus(CommentsStatus.CHO_DUYET); // Mặc định là chờ duyệt
//        comment.setCreatedAt(Calendar.getInstance().getTimeInMillis());
//        comment.setCreatedBy(customer.getEmail()); // Thông tin người tạo
//
//        Comments savedComment = commentRepository.save(comment);
//
//        // Gửi bình luận mới qua WebSocket
//        CommentDTO commentDTO = new CommentDTO();
//        commentDTO.setFullName(customer.getFullName());
//        commentDTO.setComment(commentText);
//        commentDTO.setRate(rate);
//        commentDTO.setCreatedAt(savedComment.getCreatedAt());
//        messagingTemplate.convertAndSend("/topic/comments/" + productId, commentDTO);
//
//        return savedComment;
//    }
//
//    // Lấy danh sách bình luận của sản phẩm
//    public List<CommentDTO> getCommentsByProductId(Integer productId) {
//        return commentRepository.findByProductIdOrderByCreatedAtAsc(productId)
//                .stream()
//                .map(comment -> {
//                    CommentDTO dto = new CommentDTO();
//                    dto.setFullName(comment.getCustomer().getFullName());
//                    dto.setComment(comment.getComment());
//                    dto.setRate(comment.getRate());
//                    dto.setCreatedAt(comment.getCreatedAt());
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }
//
//    // Lấy danh sách bình luận với bộ lọc cho trang admin
//    public List<Map<String, Object>> getCommentsWithFilter(String status, Double rate, Integer productId, LocalDate fromDate) {
//        List<Comments> comments = commentRepository.findAll();
//
//        // Áp dụng các bộ lọc
//        if (status != null && !status.isEmpty()) {
//            CommentsStatus statusEnum = CommentsStatus.valueOf(status);
//            comments = comments.stream()
//                    .filter(c -> c.getStatus() == statusEnum)
//                    .collect(Collectors.toList());
//        }
//
//        if (rate != null) {
//            comments = comments.stream()
//                    .filter(c -> Objects.equals(c.getRate(), rate))
//                    .collect(Collectors.toList());
//        }
//
//        if (productId != null) {
//            comments = comments.stream()
//                    .filter(c -> c.getProduct().getId().equals(productId))
//                    .collect(Collectors.toList());
//        }
//
//        if (fromDate != null) {
//            long fromTimestamp = fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
//            comments = comments.stream()
//                    .filter(c -> c.getCreatedAt() >= fromTimestamp)
//                    .collect(Collectors.toList());
//        }
//
//        // Chuyển đổi thành định dạng phù hợp với frontend
//        return comments.stream().map(this::convertToFrontendFormat).collect(Collectors.toList());
//    }
//
//    // Chuyển đổi entity thành map cho frontend
//    private Map<String, Object> convertToFrontendFormat(Comments comment) {
//        Map<String, Object> result = new HashMap<>();
//        result.put("id", comment.getId());
//        result.put("comment", comment.getComment());
//        result.put("productName", comment.getProduct().getProductName());
//        result.put("productId", comment.getProduct().getId());
//        result.put("commentDate", comment.getCreatedAt());
//        result.put("rate", comment.getRate());
//        result.put("status", comment.getStatus().name());
//
//        // Nếu có bình luận phản hồi (parent)
//        if (comment.getParentId() != null) {
//            Comments parent = commentRepository.findById(comment.getParentId()).orElse(null);
//            if (parent != null) {
//                result.put("parentComment", parent.getComment());
//            }
//        }
//
//        // Nếu có bình luận con (phản hồi của admin)
//        List<Comments> replies = commentRepository.findByParentId(comment.getId());
//        if (!replies.isEmpty()) {
//            result.put("userReply", replies.get(0).getComment());
//        }
//
//        return result;
//    }
//
//    // Duyệt nhiều bình luận
//    @Transactional
//    public String approveMultipleComments(List<Integer> commentIds) {
//        for (Integer id : commentIds) {
//            Comments comment = commentRepository.findById(id)
//                    .orElseThrow(() -> new RuntimeException("Comment not found: " + id));
//
//            comment.setStatus(CommentsStatus.DA_DUYET);
//            commentRepository.save(comment);
//        }
//        return "Đã duyệt " + commentIds.size() + " bình luận";
//    }
//
//    // Duyệt một bình luận
//    @Transactional
//    public String approveSingleComment(Integer id) {
//        Comments comment = commentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Comment not found"));
//
//        comment.setStatus(CommentsStatus.DA_DUYET);
//        commentRepository.save(comment);
//        return "Đã duyệt bình luận";
//    }
//
//    // Ẩn bình luận
//    @Transactional
//    public String hideComment(Integer id) {
//        Comments comment = commentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Comment not found"));
//
//        comment.setStatus(CommentsStatus.DA_AN);
//        commentRepository.save(comment);
//        return "Đã ẩn bình luận";
//    }
//
//    // Trả lời bình luận
//    @Transactional
//    public String replyToComment(Integer id, String replyText) {
//        Comments parentComment = commentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Comment not found"));
//
//        Comments reply = new Comments();
//        reply.setProduct(parentComment.getProduct());
//        // Customer là null khi admin trả lời
//        reply.setComment(replyText);
//        reply.setParentId(parentComment.getId());
//        reply.setStatus(CommentsStatus.DA_DUYET); // Bình luận của admin mặc định đã duyệt
//        reply.setRate(5.0); // Default rate
//        reply.setCreatedAt(Calendar.getInstance().getTimeInMillis());
//        reply.setCreatedBy("admin"); // Lưu thông tin người trả lời là admin
//
//        commentRepository.save(reply);
//        return "Đã trả lời bình luận";
//    }
//
//    public List<Comments> findAll(){
//        return commentRepository.findAll();
//    }
//}

//
//package com.poly.app.domain.client.controller;
//
//
//import com.poly.app.domain.admin.voucher.response.VoucherReponse;
//import com.poly.app.domain.client.response.CommentMessage;
//import com.poly.app.domain.client.service.CommentService;
//import com.poly.app.domain.common.ApiResponse;
//import com.poly.app.domain.model.Comments;
//import com.poly.app.domain.repository.CommentsRepository;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/admin/comments")
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Slf4j
//public class CommentController {
//
//    CommentService commentService;
//    CommentsRepository commentsRepository;
//
//    // Lấy danh sách bình luận với bộ lọc
//    @GetMapping("/AllComments")
//    public ApiResponse<List<Map<String, Object>>> getComments(
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) Double rate,
//            @RequestParam(required = false) Integer productId,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate) {
//
//        return ApiResponse.<List<Map<String, Object>>>builder()
//                .message("Lấy danh sách bình luận thành công")
//                .data(commentService.getCommentsWithFilter(status, rate, productId, fromDate))
//                .build();
//    }
//
//    // Duyệt nhiều bình luận
//    @PutMapping("/approve")
//    public ApiResponse<String> approveMultipleComments(@RequestBody Map<String, List<Integer>> request) {
//        List<Integer> commentIds = request.get("commentIds");
//        return ApiResponse.<String>builder()
//                .message("Duyệt bình luận thành công")
//                .data(commentService.approveMultipleComments(commentIds))
//                .build();
//    }
//
//    // Duyệt một bình luận
//    @PutMapping("/{id}/approve")
//    public ApiResponse<String> approveSingleComment(@PathVariable Integer id) {
//        return ApiResponse.<String>builder()
//                .message("Duyệt bình luận thành công")
//                .data(commentService.approveSingleComment(id))
//                .build();
//    }
//
//    // Ẩn bình luận
//    @PutMapping("/{id}/hide")
//    public ApiResponse<String> hideComment(@PathVariable Integer id) {
//        return ApiResponse.<String>builder()
//                .message("Ẩn bình luận thành công")
//                .data(commentService.hideComment(id))
//                .build();
//    }
//
//    // Trả lời bình luận
//    @PostMapping("/{id}/reply")
//    public ApiResponse<String> replyToComment(
//            @PathVariable Integer id,
//            @RequestBody Map<String, String> request) {
//
//        String reply = request.get("reply");
//        return ApiResponse.<String>builder()
//                .message("Trả lời bình luận thành công")
//                .data(commentService.replyToComment(id, reply))
//                .build();
//    }
//
//    // API thêm bình luận (client)
//    @PostMapping
//    public ApiResponse<Comments> addComment(@RequestBody CommentMessage commentMessage) {
//        return ApiResponse.<Comments>builder()
//                .message("Thêm bình luận thành công")
//                .data(commentService.saveComment(
//                        commentMessage.getProductId(),
//                        commentMessage.getCustomerId(),
//                        commentMessage.getComment(),
//                        commentMessage.getRate()))
//                .build();
//    }
//
//
//    @GetMapping("/all")
//    public ApiResponse<List<Comments>> getAllComments() {
//        return ApiResponse.<List<Comments>>builder()
//                .message("List of all comments")
//                .data(commentService.findAll())
//                .build();
//    }
//
//}