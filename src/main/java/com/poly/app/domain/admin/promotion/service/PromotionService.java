package com.poly.app.domain.admin.promotion.service;

import com.poly.app.domain.admin.promotion.request.PromotionRequest;
import com.poly.app.domain.admin.promotion.response.PromotionResponse;
import com.poly.app.domain.model.Promotion;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.infrastructure.constant.DiscountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PromotionService {
    List<PromotionResponse> getAllPromotion();

    Promotion createPromotion(PromotionRequest request);

    PromotionResponse updatePromotion(PromotionRequest request, int id);

    String deletePromotion(int id);

    Page<PromotionResponse> getAllPromotion(Pageable pageable);

    public PromotionResponse getPromotionDetail(int id);

    String switchStatus(Integer id, StatusEnum status);
    List<PromotionResponse> getAllPromotionsWithCustomer(Integer productIds);
 Page<PromotionResponse> getPagePromotion(int size, int page, StatusEnum statusPromotion, String search, String startDate, String endDate,Double discountValue);


}

