package com.poly.app.domain.admin.promotion;

import com.poly.app.domain.model.Promotion;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.domain.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionSchedulerService {

    private final PromotionRepository promotionRepository;

    @Scheduled(fixedRate = 30000) // Chạy mỗi 30 giây
    public void checkPromotionExpiration() {
        log.info("Đang kiểm tra các chương trình khuyến mãi hết hạn...");

        LocalDateTime now = LocalDateTime.now();
        List<Promotion> expiredPromotions = promotionRepository
                .findByEndDateBeforeAndStatusPromotionNot(now, StatusEnum.ngung_kich_hoat);

        for (Promotion promo : expiredPromotions) {
            promo.setStatusPromotion(StatusEnum.ngung_kich_hoat);
            log.info("Promotion hết hạn: " + promo.getPromotionCode());
        }

        promotionRepository.saveAll(expiredPromotions);
    }

    @Scheduled(fixedRate = 30000) // Chạy mỗi 30 giây
    public void checkPromotionCanUsed() {
        log.info("Đang kiểm tra các chương trình khuyến mãi hết hạn...");

        LocalDateTime now = LocalDateTime.now();
        List<Promotion> expiredPromotions = promotionRepository
                .findByStartDateBeforeAndStatusPromotionNot(now, StatusEnum.chua_kich_hoat);

        for (Promotion promo : expiredPromotions) {
            promo.setStatusPromotion(StatusEnum.dang_kich_hoat);
            log.info("Promotion hết hạn: " + promo.getPromotionCode());
        }

        promotionRepository.saveAll(expiredPromotions);
    }
}
