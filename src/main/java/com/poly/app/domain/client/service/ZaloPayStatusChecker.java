package com.poly.app.domain.client.service;

import com.poly.app.domain.client.response.NotificationResponse;
import com.poly.app.domain.model.*;
import com.poly.app.domain.repository.AnnouncementRepository;
import com.poly.app.domain.repository.BillHistoryRepository;
import com.poly.app.domain.repository.BillRepository;
import com.poly.app.domain.repository.PaymentBillRepository;
import com.poly.app.infrastructure.constant.BillStatus;
import com.poly.app.infrastructure.constant.PayMentBillStatus;
import com.poly.app.infrastructure.constant.PaymentMethodEnum;
import com.poly.app.infrastructure.constant.PaymentMethodsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ZaloPayStatusChecker {
    private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    private final AtomicBoolean enabled = new AtomicBoolean(false); // Mặc định TẮT
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private ScheduledFuture<?> scheduledFuture;
    @Autowired
    private PaymentBillRepository paymentBillRepository;
    @Autowired
    private ZaloPayService zaloPayService;
    @Autowired
    private BillHistoryRepository billHistoryRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Để gửi thông báo qua WebSocket

    public ZaloPayStatusChecker() {
        scheduler.initialize();
    }

    public void startPolling() {
        if (enabled.get()) {
            System.out.println("⚠️ Polling đã chạy, không cần bật lại!");
            return;
        }
        enabled.set(true);
        scheduledFuture = scheduler.scheduleAtFixedRate(this::checkPendingPayments, 5000);
        System.out.println("✅ Polling đã được bật!");
    }

    public void stopPolling() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            enabled.set(false);
            System.out.println("⏸ Polling đã bị tắt!");
        }
    }

    private void checkPendingPayments() {
        if (!enabled.get()) return;
        long currentTimeMillis = Instant.now().toEpochMilli();
        // Subtract 20 minutes (20 * 60 * 1000 milliseconds)
        long timeLimit = currentTimeMillis - (20 * 60 * 1000);
        List<PaymentBill> paymentBills = paymentBillRepository.getAllPaymentBillCTT(
                PayMentBillStatus.CHUA_THANH_TOAN,
                PaymentMethodEnum.ZALO_PAY, timeLimit
        );

        System.out.println("✅ Đang kiểm tra trạng thái đơn hàng... (Số lượng: " + paymentBills.size() + ")");

        List<CompletableFuture<Void>> futures = paymentBills.stream()
                .map(pb -> CompletableFuture.runAsync(() -> {
                    try {
                        Map<String, Object> res = zaloPayService.getStatusByApptransid(pb.getTransactionCode());
                        if (res != null && "1".equals(String.valueOf(res.get("returncode")))) {
                            pb.setPayMentBillStatus(PayMentBillStatus.DA_THANH_TOAN);
                            paymentBillRepository.save(pb);
                            billHistoryRepository.save(BillHistory
                                    .builder()
                                    .customer(pb.getBill().getCustomer())
                                    .description("Cập nhận trạng thái thanh toán thành công")
                                    .bill(pb.getBill())
                                    .status(BillStatus.DA_THANH_TOAN)
                                    .build());
                            Bill bill = billRepository.findById(pb.getBill().getId()).get();
                            bill.setStatus(BillStatus.CHO_XAC_NHAN);
                            bill.setCustomerMoney(pb.getTotalMoney());
                            billRepository.save(bill);
//                             Tạo thông báo cho customer
                            Announcement announcement = new Announcement();
                            announcement.setCustomer(pb.getBill().getCustomer());
                            announcement.setAnnouncementContent("Đã thanh toán thành công! Mã giao dịch: " + pb.getTransactionCode());
                            announcementRepository.save(announcement);

                            try {
                                messagingTemplate.convertAndSend(

                                        "/topic/global-notifications/" + pb.getBill().getCustomer().getId(),
                                        new NotificationResponse(
                                                Long.valueOf(announcement.getId()), // Chắc chắn ID không null sau khi đã save
                                                announcement.getAnnouncementContent(),
                                                new Date(announcement.getCreatedAt()),
                                                false
                                        )
                                );


                                System.out.println("Đã gửi thông báo WebSocket tới user: ");
                            } catch (Exception e) {
                                System.err.println("Lỗi khi gửi thông báo WebSocket: " + e.getMessage());
                                e.printStackTrace();
                            }
                            System.out.println("Hoàn thành giao dịch: " + pb.getTransactionCode());
                        } else {
                            System.out.println("Giao dịch chưa hoàn thành: " + pb.getTransactionCode() + " - returncode: " + res.get("returncode"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, executor)) // Sử dụng executor tùy chỉnh
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        System.out.println("✅ Đã kiểm tra xong tất cả đơn hàng.");
    }

}
