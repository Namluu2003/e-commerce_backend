package com.poly.app.domain.admin.bill.controller;


import com.poly.app.domain.admin.bill.request.RestoreQuantityRequest;
import com.poly.app.domain.admin.bill.service.WebSocketService;
import com.poly.app.domain.admin.product.response.img.ImgResponse;
import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.domain.client.repository.ProductViewRepository;
import com.poly.app.domain.client.response.PromotionResponse;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.domain.repository.ImageRepository;
import com.poly.app.domain.repository.ProductDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/bill")
public class BillDetailController {

    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    private ProductViewRepository productViewRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private WebSocketService webSocketService;

    @PostMapping("/restore-quantity")
    public ResponseEntity<?> restoreQuantity(
            @RequestBody List<RestoreQuantityRequest> requests) {
            log.info("restore quantity request: {}", requests.toString());
        try {
            for (RestoreQuantityRequest request : requests) {
                ProductDetail product = productDetailRepository.findById(request.getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                if (request.getIsRestoreQuantity()) {
                    product.setQuantity(product.getQuantity() + request.getQuantity());
                } else {
                    product.setQuantity(product.getQuantity() - request.getQuantity());
                }
                ProductDetail productDetail = productDetailRepository.save(product);
                ProductDetailResponse productDetailResponse = ProductDetailResponse.fromEntity(productDetail);
                List<ImgResponse> images = imageRepository.findByProductDetailId(productDetail.getId());
                productDetailResponse.setImage(images);

                List<PromotionResponse> promotionResponses = productViewRepository.findPromotionByProductDetailId(productDetail.getId(), ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());

                Optional<PromotionResponse> maxPromotion = promotionResponses.stream().max(Comparator.comparing(PromotionResponse::getDiscountValue));
                productDetailResponse.setPromotionResponse(maxPromotion.orElse(null));
                webSocketService.sendProductUpdate(productDetailResponse);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error restoring quantity: " + e.getMessage());


        }
    }
}
