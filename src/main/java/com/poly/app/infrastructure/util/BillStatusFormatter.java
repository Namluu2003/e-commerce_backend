package com.poly.app.infrastructure.util;

import com.poly.app.infrastructure.constant.BillStatus;

public class BillStatusFormatter {

    public static String format(BillStatus status) {
        return switch (status) {
            case DA_HUY -> "Đã huỷ";
            case CHO_XAC_NHAN -> "Chờ xác nhận";
            case DA_XAC_NHAN -> "Đã xác nhận";
            case CHO_VAN_CHUYEN -> "Chờ vận chuyển";
            case DANG_VAN_CHUYEN -> "Đang vận chuyển";
            case DA_GIAO_HANG -> "Đã giao hàng";
            case DA_THANH_TOAN -> "Đã thanh toán";
            case CHO_THANH_TOAN -> "Chờ thanh toán";
            case DA_HOAN_THANH -> "Đã hoàn thành";
            case TAO_DON_HANG -> "Tạo đơn hàng";
            case TRA_HANG -> "Trả hàng";
            case KHONG_TON_TAI -> "Không tồn tại";
            case HUY_YEU_CAU_TRA_HANG -> "Huỷ yêu cầu trả hàng";
            case TU_CHOI_TRA_HANG -> "Từ chối trả hàng";
            case DANG_XAC_MINH -> "Đang xác minh";
        };
    }
}
