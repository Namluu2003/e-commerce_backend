package com.poly.app.domain.admin.promotion.controller;

import com.poly.app.domain.admin.promotion.request.PromotionRequest;
import com.poly.app.domain.admin.promotion.response.ApiResponse;
import com.poly.app.domain.admin.promotion.response.PromotionResponse;
import com.poly.app.domain.admin.promotion.service.PromotionService;
import com.poly.app.domain.common.PageReponse;
import com.poly.app.domain.model.Promotion;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.infrastructure.constant.DiscountType;
import com.poly.app.infrastructure.constant.VoucherType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/promotion")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PromotionController {
//    PromotionRepository promotionRepository;
    PromotionService promotionService;

    @GetMapping("/hien")
    public ApiResponse<List<PromotionResponse>> getAllPromotion() {
        return ApiResponse.<List<PromotionResponse>>builder()
                .message("list promotion")
                .data(promotionService.getAllPromotion())
                .build();
    }

    @PostMapping("/add")
    public ApiResponse<Promotion> create(@RequestBody PromotionRequest request) {
        return ApiResponse.<Promotion>builder()
                .message("create promotion")
                .data(promotionService.createPromotion(request))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<PromotionResponse> update(@RequestBody PromotionRequest request, @PathVariable int id) {
        return ApiResponse.<PromotionResponse>builder()
                .message("update promotion")
                .data(promotionService.updatePromotion(request, id))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> delete(@PathVariable int id) {
        return ApiResponse.<String>builder()
                .message("delete id promotion")
                .data(promotionService.deletePromotion(id))
                .build();
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<PromotionResponse> getPromotionDetail(@PathVariable int id) {
        return ApiResponse.<PromotionResponse>builder()
                .message("detail by id")
                .data(promotionService.getPromotionDetail(id))
                .build();
    }

    @GetMapping("/page")
    public ApiResponse<Page<PromotionResponse>> phanTrang(@RequestParam(value = "page") Integer page,
                                                                       @RequestParam(value = "size") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<PromotionResponse> list = promotionService.getAllPromotion(pageable);
        return ApiResponse.<Page<PromotionResponse>>builder()
                .message("")
                .data(list)
                .build();
    }

    @GetMapping("/switchStatus")
    public ApiResponse<String> switchStatus(@RequestParam(value = "id") Integer id,
                                                                                       @RequestParam(value = "status")
                                                                                               StatusEnum status
    ) {
        promotionService.switchStatus(id, status);

        return ApiResponse.<String>builder()
                .message("")
                .data(promotionService.switchStatus(id, status))
                .build();
    }

    @GetMapping("/index")
    public PageReponse index(@RequestParam(defaultValue = "10") Integer size,
                             @RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(required = false) StatusEnum statusPromotion,
                             @RequestParam(required = false) String search,
                             @RequestParam(required = false) Double discountValue,
                             @RequestParam(required = false) String startDate,
                             @RequestParam(required = false) String endDate
    )
    {
        log.info("startDate {}, endDate {}, discountValue {}", startDate, endDate, discountValue);
        return new PageReponse(promotionService.getPagePromotion(
                size, page, statusPromotion, search, startDate, endDate, discountValue));
    }

}
