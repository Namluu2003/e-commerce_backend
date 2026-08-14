package com.poly.app.domain.repository;

import com.poly.app.domain.model.Announcement;
import com.poly.app.domain.model.Comments;
import com.poly.app.domain.model.Product;
import com.poly.app.domain.model.Voucher;
import com.poly.app.infrastructure.constant.VoucherType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments,Integer> {
    List<Comments> findByProductIdOrderByCreatedAtAsc(Integer productId);
    List<Comments> findAllByOrderByCreatedAtAsc();
    List<Comments> findAllByProductIdOrderByCreatedAtAsc(Integer productId);
    List<Comments> findAllByParentId(Integer parentId); // Tìm tất cả các câu trả lời
    boolean existsByProductIdAndCustomerId(Integer productId, Integer customerId);
//@Query("SELECT c FROM Comments c WHERE " +
//        "(:productName IS NULL OR c.product.productName = :productName) " +
//        "AND (:fromDate IS NULL OR c.createdAt >= :fromDate) " +
//        "AND (:toDate IS NULL OR c.createdAt <= :toDate)")
//List<Comments> findByProductNameAndCreatedAtBetween(
//        @Param("productName") String productName,
//        @Param("fromDate") Long fromDate,
//        @Param("toDate") Long toDate
//);
@Query("SELECT c FROM Comments c WHERE " +
        "(:productName IS NULL OR c.product.productName = :productName)")
Page<Comments> findByProductName(
        @Param("productName") String productName,
        Pageable pageable
);


    Page<Comments> findAll(Specification<Comments> spec, Pageable pageable);
}