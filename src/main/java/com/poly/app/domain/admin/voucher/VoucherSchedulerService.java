package com.poly.app.domain.admin.voucher;
import com.poly.app.domain.model.Voucher;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.domain.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherSchedulerService {

    private final VoucherRepository voucherRepository;

    // Chạy mỗi 30 giây
    @Scheduled(fixedRate = 30000)
    public void checkVoucherExpiration() {
        log.info("Đang kiểm tra voucher hết hạn...");

        LocalDateTime now = LocalDateTime.now();
        List<Voucher> expiredVouchers = voucherRepository
                .findByEndDateBeforeAndStatusVoucherNotOrQuantity(now, StatusEnum.ngung_kich_hoat, 0);

        for (Voucher voucher : expiredVouchers) {
            voucher.setStatusVoucher(StatusEnum.ngung_kich_hoat);
            log.info("Voucher hết hạn: " + voucher.getVoucherCode());
        }

        voucherRepository.saveAll(expiredVouchers);
    }


    @Scheduled(fixedRate = 30000)
    public void checkVoucherActivation() {
        log.info("Đang kiểm tra voucher đủ điều kiện kích hoạt...");

        LocalDateTime now = LocalDateTime.now();
        List<Voucher> vouchersToActivate = voucherRepository
                .findByStartDateBeforeAndStatusVoucher(now, StatusEnum.chua_kich_hoat);

        for (Voucher voucher : vouchersToActivate) {
            voucher.setStatusVoucher(StatusEnum.dang_kich_hoat);
            log.info("Voucher được kích hoạt: " + voucher.getVoucherCode());
        }

        voucherRepository.saveAll(vouchersToActivate);
    }

}
