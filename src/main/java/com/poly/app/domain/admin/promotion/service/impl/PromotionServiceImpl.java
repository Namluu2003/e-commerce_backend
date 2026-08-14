package com.poly.app.domain.admin.promotion.service.impl;

import com.poly.app.domain.admin.promotion.request.PromotionRequest;
import com.poly.app.domain.admin.promotion.response.ProductpromotionResponse;
import com.poly.app.domain.admin.promotion.response.PromotionResponse;
import com.poly.app.domain.admin.promotion.service.PromotionService;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.domain.model.*;
import com.poly.app.domain.repository.*;
import com.poly.app.infrastructure.constant.DiscountType;
import com.poly.app.infrastructure.constant.VoucherType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    ProductPromotionRepository productPromotionRepository;
    @Autowired
    ProductRepository productRepository;

    public StatusEnum checkPromotionStatus(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime currentDate = LocalDateTime.now(); // Lấy thời gian hiện tại

        if (currentDate.isBefore(startDate)) {
            return StatusEnum.chua_kich_hoat; // Chưa kích hoạt
        } else if (currentDate.isAfter(endDate)) {
            return StatusEnum.ngung_kich_hoat; // Đã ngừng kích hoạt
        } else {
            return StatusEnum.dang_kich_hoat; // Đang kích hoạt
        }
    }

    @Transactional
    @Override
    public Promotion createPromotion(PromotionRequest request) {
        StatusEnum saStatusPromotion = checkPromotionStatus(request.getStartDate(), request.getEndDate());
        String generatedPromotionCode = "DGG" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Promotion promotion = Promotion.builder()
                .promotionCode(generatedPromotionCode)
                .promotionName(request.getPromotionName())
                .promotionType(request.getPromotionType())
                .discountValue(request.getDiscountValue())
                .discountType(request.getDiscountType())
                .quantity(request.getQuantity())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .statusPromotion(saStatusPromotion)
                .build();

        Promotion promotionSaved = promotionRepository.save(promotion);

        if (request.getProductDetailIds() != null && !request.getProductDetailIds().isEmpty()) {
            List<ProductDetail> productDetails = productDetailRepository.findAllById(request.getProductDetailIds());

            List<ProductPromotion> productPromotions = productDetails.stream()
                    .map(productDetail -> ProductPromotion.builder()
                            .product(productDetail.getProduct().getId()) // Lưu ID sản phẩm nếu cần
                            .productDetail(productDetail)
                            .promotion(promotionSaved)
                            .quantity(request.getQuantity())
                            .build())
                    .collect(Collectors.toList());

            productPromotionRepository.saveAll(productPromotions);
        }


        return promotionSaved;
    }



    @Override
    public List<PromotionResponse> getAllPromotion() {
        return promotionRepository.findAll().stream()
                .map(PromotionResponse::fromEntity)
                .toList();
    }

    public Page<PromotionResponse> getAllPromotion(Pageable pageable) {
        return promotionRepository.findAll(pageable).map(promotion ->
                PromotionResponse.builder()
                        .id(promotion.getId())
                        .promotionCode(promotion.getPromotionCode())
                        .promotionName(promotion.getPromotionName())
                        .promotionType(promotion.getPromotionType())
                        .discountValue(promotion.getDiscountValue())
                        .quantity(promotion.getQuantity())
                        .startDate(promotion.getStartDate())
                        .endDate(promotion.getEndDate())
                        .statusPromotion(promotion.getStatusPromotion())
                        .build()
        );
    }


    @Transactional
    @Override
    public PromotionResponse updatePromotion(PromotionRequest request, int id) {
        StatusEnum saStatusPromotion = checkPromotionStatus(request.getStartDate(), request.getEndDate());
        // Lấy thông tin chương trình khuyến mãi từ database
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with ID: " + id));

        // Cập nhật trạng thái chương trình khuyến mãi
        promotion.setStatusPromotion(checkPromotionStatus(request.getStartDate(), request.getEndDate()));

        // Cập nhật thông tin chương trình khuyến mãi
        promotion.setPromotionName(request.getPromotionName());
        promotion.setPromotionType(request.getPromotionType());
        promotion.setDiscountValue(request.getDiscountValue());
        promotion.setDiscountType(request.getDiscountType());
        promotion.setQuantity(request.getQuantity());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());

        // Xóa tất cả các quan hệ cũ giữa promotion và product/productDetail
        productPromotionRepository.deleteAllByPromotionId(id);

        List<ProductPromotion> productPromotions = new ArrayList<>();

        // Cập nhật danh sách sản phẩm nếu có
        if (request.getProductIds() != null && !request.getProductIds().isEmpty()) {
            List<Product> products = productRepository.findAllById(request.getProductIds());
            for (Product product : products) {
                productPromotions.add(ProductPromotion.builder()
                        .product(product.getId()) // Lưu ID sản phẩm thay vì đối tượng Product
                        .promotion(promotion)
                        .quantity(request.getQuantity())
                        .build());
            }
        }

        // Cập nhật danh sách chi tiết sản phẩm nếu có
        if (request.getProductDetailIds() != null && !request.getProductDetailIds().isEmpty()) {
            List<ProductDetail> productDetails = productDetailRepository.findAllById(request.getProductDetailIds());
            for (ProductDetail productDetail : productDetails) {
                productPromotions.add(ProductPromotion.builder()
                        .product(productDetail.getProduct().getId()) // Lưu ID sản phẩm từ ProductDetail
                        .productDetail(productDetail)
                        .promotion(promotion)
                        .quantity(request.getQuantity())
                        .build());
            }
        }

        // Lưu danh sách mới vào cơ sở dữ liệu
        productPromotionRepository.saveAll(productPromotions);

        // Lưu cập nhật chương trình khuyến mãi
        return PromotionResponse.fromEntity(promotionRepository.save(promotion));
    }





    @Transactional
    @Override
    public String deletePromotion(int id) {
        // Kiểm tra sự tồn tại của Promotion
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Promotion với ID: " + id));

        // Xóa tất cả ProductPromotion liên quan
        productPromotionRepository.deleteAllByPromotionId(id);

        // Xóa Promotion
        promotionRepository.delete(promotion);

        return "Promotion với ID " + id + " đã được xóa thành công.";
    }



    @Override
    public PromotionResponse getPromotionDetail(int id) {
        // Lấy thông tin Promotion từ DB
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Promotion với ID: " + id));

        // Lấy danh sách sản phẩm và chi tiết sản phẩm liên quan đến promotion
        List<ProductPromotion> productPromotions = productPromotionRepository.findProductPromotionByPromotion(promotion);

        // Lấy danh sách productIds và productDetailIds
        List<Integer> productIds = productPromotions.stream()
                .map(ProductPromotion::getProduct)
                .filter(Objects::nonNull) // Tránh null
                .collect(Collectors.toList());

        List<Integer> productDetailIds = productPromotions.stream()
                .map(pp -> pp.getProductDetail() != null ? pp.getProductDetail().getId() : null)
                .filter(Objects::nonNull) // Tránh null
                .collect(Collectors.toList());

        // Chuyển đổi từ Entity sang Response
        PromotionResponse promotionResponse = PromotionResponse.fromEntity(promotion);
        promotionResponse.setProductIds(productIds);
        promotionResponse.setProductDetailIds(productDetailIds);

        return promotionResponse;
    }




    @Override
    public String switchStatus(Integer id, StatusEnum status) {
        LocalDateTime currentDate = LocalDateTime.now(); // Lấy thời gian hiện tại
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID không tồn tại"));

        if (status == StatusEnum.ngung_kich_hoat) {
            promotion.setStatusPromotion(StatusEnum.ngung_kich_hoat);
            promotion.setEndDate(currentDate);
            promotionRepository.save(promotion);
            return "ngung_kich_hoat";
        } else {
            promotion.setStatusPromotion(StatusEnum.dang_kich_hoat);
            promotion.setStartDate(currentDate);
            promotionRepository.save(promotion);
            return "dang_kich_hoat";
        }
    }

    @Override
    public List<PromotionResponse> getAllPromotionsWithCustomer(Integer productIds) {
        return List.of();
    }

    @Override
    public Page<PromotionResponse> getPagePromotion(int size, int page, StatusEnum statusPromotion, String search, String startDate, String endDate,Double discountValue ) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Promotion> spec = Specification.where(null);

        // Lọc theo trạng thái Promotion
        if (statusPromotion != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("statusPromotion"), statusPromotion));
        }
        // Tìm kiếm theo mã hoặc tên promotion
        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("promotionCode"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("promotionName"), "%" + search + "%")

                    ));
        }
        if (discountValue != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("discountValue"), discountValue)
            );
        }


        // Lọc theo ngày bắt đầu
        if (startDate != null && !startDate.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"),
                            parseDateTime(startDate)));
        }

        // Lọc theo ngày kết thúc
        if (endDate != null && !endDate.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDate"),
                            parseDateTime(endDate))
            );
        }

        Page<Promotion> PromotionPage =promotionRepository.findAll(spec, pageable);
        List<PromotionResponse> promotionResponses = PromotionPage.getContent().stream()
                .map(PromotionResponse::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(promotionResponses, pageable, PromotionPage.getTotalElements());
    }

    private  LocalDateTime parseDateTime(String dateTime) {
        return LocalDate.parse(dateTime, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .atStartOfDay(); // Đặt giờ thành 00:00:0
    }

}
