package com.poly.app.infrastructure.util;

import java.security.MessageDigest;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Hex;

public class CloudinaryUtil {

    private static final String API_SECRET = "i8t2cshdnpH-g0gQjalYtgClxc8";

    public static String generateSignature(Map<String, String> params) {
        // Sắp xếp tham số theo thứ tự alphabet
        String data = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        // Thêm API_SECRET vào cuối
        data += API_SECRET;

        // Mã hóa SHA-1
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(data.getBytes());
            return Hex.encodeHexString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký", e);
        }
    }
}
