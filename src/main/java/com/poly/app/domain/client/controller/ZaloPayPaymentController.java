package com.poly.app.domain.client.controller;


import com.poly.app.domain.client.service.ZaloPayService;
import com.poly.app.infrastructure.config.ZaloPayConfig;
import com.poly.app.infrastructure.util.HMACUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/zalopay")
@Slf4j
public class ZaloPayPaymentController {
    private final ZaloPayConfig zaloPayConfig;
    @Autowired
    ZaloPayService zaloPayService;

    public ZaloPayPaymentController(ZaloPayConfig zaloPayConfig) {
        this.zaloPayConfig = zaloPayConfig;
    }

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    @PostMapping(value = "/create-order")
    public Map<String, Object> createPayment(@RequestParam(name = "appuser") String appuser,
                                             @RequestParam(name = "amount") Long amount,
                                             @RequestParam(name = "order_id") Long order_id) throws Exception {

        return zaloPayService.createPayment(appuser, amount, order_id);
    }

    @GetMapping(value = "/getstatusbyapptransid")
    public Map<String, Object> getStatusByApptransid(
            @RequestParam(name = "apptransid") String apptransid)
            throws Exception {
       return zaloPayService.getStatusByApptransid(apptransid);
    }

    @PostMapping("/refund")
    public ResponseEntity<Map<String, Object>> processRefund(
            @RequestParam("zptransid") Long zptransid,
            @RequestParam("amount") int amount,
            @RequestParam("description") String description) {
        try {
            // Kiểm tra trạng thái refund trước (tuỳ chọn)
            // Giả sử bạn có mrefundid từ lần refund trước, nếu không thì bỏ qua bước này
            // Map<String, Object> status = zaloPayService.getRefundStatus("your_mrefundid_here");
            // if (status.containsKey("refund_status") && "processing".equals(status.get("refund_status"))) {
            //     throw new Exception("Giao dịch đang được xử lý hoàn tiền!");
            // }

            // Thực hiện yêu cầu refund
            Map<String, Object> refundResponse = zaloPayService.refund(zptransid, amount, description);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Refund processed successfully");
            response.put("data", refundResponse);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("Invalid refund parameters: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Invalid refund parameters: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            log.error("Error processing refund: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");

            // Xử lý lỗi cụ thể "Giao dịch đang refund!"
            if ("Refund failed: Giao dịch đang refund!".equals(e.getMessage())) {
                errorResponse.put("message", "Transaction is already being refunded");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse); // 409 Conflict
            }

            // Lỗi khác
            errorResponse.put("message", "Failed to process refund: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/refund/status")
    public ResponseEntity<Map<String, Object>> getRefundStatus(
            @RequestParam("mrefundid") String mrefundid) {
        try {
            Map<String, Object> statusResponse = zaloPayService.getRefundStatus(mrefundid);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Refund status retrieved successfully");
            response.put("data", statusResponse);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid mrefundid: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Invalid mrefundid: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            log.error("Error getting refund status: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to get refund status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
