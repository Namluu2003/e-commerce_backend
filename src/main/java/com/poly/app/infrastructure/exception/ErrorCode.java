package com.poly.app.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    BILL_NOT_EXISTS(6000, "bill ko tồn tại"),
    NOT_BLANK(3000, "Không được để trống"),
    UNCATEGORIZED_EXCEPTION(9999, "UNCATEGORIZED ERROR"),
    INVALID_KEY(1001, "INVALID MESSAGE KEY"),
    MAUSAC_NOT_FOUND(1002, "Mausac Not Found"),
    MAUSAC_ALREADY_EXISTS(1003, "Mausac đã tồn tại !"),
    TEN_MAUSAC_INVALID(1004, "Tên màu sắc cần nhiều hơn 2 ký tự !"),
    TRANGTHAI_MAUSAC_INVALID(1004, "Trạng thái không hợp lệ !"),
    CHATLIEUDE_NOT_FOUND(1005, "Chất liệu đế không tồn tại !"),
    CHATLIEUDE_ALREADY_EXISTS(1006, "Chất liệu đế đã tồn tại !"),
    TEN_CHATLIEUDE_INVALID(1007, "Tên chất liệu đế cần nhiều hơn 3 ký tự !"),
    KICHTHUOC_ALREADY_EXISTS(1008, "KichThuoc đã tồn tại!"),
    KICHTHUOC_NOT_FOUND(1009, "KichThuoc không tồn tại!"),
    TRANGTHAI_KICHTHUOC_INVALID(1009, "Trạng thái không hợp lệ !"),
    CHATLIEUVAI_NOT_FOUND(1002, "chất liệu đế Not Found"),
    CHATLIEUVAI_ALREADY_EXISTS(1003, "chất liệu đế đã tồn tại !"),
    TEN_CHATLIEUVAI_INVALID(1004, "Tên chất liệu đế cần nhiều hơn 2 ký tự !"),
    TRANGTHAI_CHATLIEUVAI_INVALID(1004, "Trạng thái không hợp lệ !"),
    HANG_ALREADY_EXISTS(1010, "DanhMuc da ton tai!"),
    HANG_NOT_FOUND(1011, "DanhMuc khong ton tai"),
    TAIKHOAN_NOT_FOUND(3000, "Tài khoản không tồn tại !"),
    TEN_TAIKHOAN_INVALID(3001, "Tên tài khoản cần trên 8 kí tự"),
    MATKHAU_TAIKHOAN_INVALID(3002, "Mật khẩu tài khoản cần trên 8 kí tự"),
    TRANGTHAI_TAIKHOAN_INVALID(3003, "Trạng thái tài khoản không hợp lệ !"),
    MA_TAIKHOAN_EXIST(3004, "Mã tài khoản không được trùng !"),
    TEN_TAIKHOAN_EXISTED(3007, "Tên tài khoản đã tồn tại !"),
    // Bổ sung các mã lỗi cho voucher
    VOUCHER_ALREADY_EXISTS(1020, "Voucher đã tồn tại!"),
    VOUCHER_NOT_FOUND(1021, "Voucher không tồn tại!"),
    EMAIL_ALREADY_EXISTS(1020, "Email đã tồn tại!"),
    PHONE_ALREADY_EXISTS(1020, "SDT đã tồn tại!"),
    KHACH_HANG_NOT_FOUND(1021, "Khách hàng không tồn tại!"),

    THUONGHIEU_ALREADY_EXISTS(1012, "Thuong hieu da ton tai !"),
    THUONGHIEU_NOT_FOUND(1013, "Thuong hieu khong ton tai !"),
    SANPHAM_NOT_FOUND(1014, "Sản phẩm không tồn tại !"),
    TEN_SANPHAM_INVALID(1014, "Tên sản phẩm không hợp lệ phải nhiều hơn 8 kí tự !"),
    TEN_SANPHAM_EXIST(1014, "Tên sản phẩm không được trùng !"),
    DANHMUC_SANPHAM_INVALID(1014, "Danh mục không được để trống"),
    DANHMUC_NOT_FOUND(1014, "Danh mục không tồn tại !"),
    THUONGHIEU_SANPHAM_INVALID(1014, "Thương hiệu không được để trống"),
    DEGIAY_SANPHAM_INVALID(1014, "Đế giày không được để trống"),
    TRANGTHAI_SANPHAM_INVALID(1014, "Trạng thái không hợp lệ !"),
    LOAIVAI_SANPHAM_INVALID(1014, "Loại vải không được để trống"),
    DIA_CHI_NOT_FOUND(2002, "Địa chỉ không tồn tại !"),
    VOUCHER_NOT_YET_STARTED(1040, "VOUCHER_NOT_YET_STARTED"),
    VOUCHER_EXPIRED(1041, "VOUCHER_EXPIRED"),
    LOAI_HOA_DON_INVALID(1022, "Loại hóa đơn không đúng !"),
    LICH_SU_THANH_TOAN_NOT_FOUND(1023, "Lịch sử thanh toán không tồn tại !"),
    TEN_TAIkHOAN_NOT_EXIST(3005, "Tên tài khoản không tồn tại !"),
    UNAUTHENTICATED(3006, "Unauthenticated"),
    TEN_TAIkHOAN_EXISTED(3007, "Tên tài khoản đã tồn tại !"),

    SANPHAMCHITIET_NOT_FOUND(1015, "Sản phẩm chi tiết không tồn tại !"),
    SANPHAMCHITIET_EXIST(1016, "Sản phẩm chi tiết đã tồn tại !"),
    GIA_BAN_INVALID(1017, "Giá bán không hợp lệ !"),
    SO_LUONG_INVALID(1018, "Số lượng không hợp lệ !"),
    Sale_NOT_FOUND(1019, "Sale không tồn tại !"),
    TEN_SALE_INVALID(1019, "Tên sale không đã tồn tại !"),
    THOI_GIAN_INVALID(1019, "Thời gian không hợp lệ !"),

    GIO_HANG_NOT_FOUND(2000, "Giỏ hàng không tồn tại !"),
    GIO_HANG_CHI_TIET_NOT_FOUND(2000, "Giỏ hàng chi tiết không tồn tại !"),
    HOA_DON_INVALID(2000, "Không thể tạo hóa đơn"),
    HOA_DON_NOT_FOUND(2000, "Hóa đơn không tồn tại !"),
    HOA_DON_CHI_TIET_NOT_FOUND_LIST(2000, "Danh sách hóa đơn chi tiết by id hóa đơn không tồn tại"),
    NHANVIEN_NOT_FOUND(2000, "Nhân viên không tồn tại !"),

    SOLUONG_SANPHAM_KHONG_DU(2000, "Số lượng sản phẩm mua nhiều hơn số lượng có sẵn"),
    ID_GIO_HANG_CANT_BE_NULL(2000, "Id giỏ hàng không được null"),
    CART_DONT_HAVE_PRODUCT(2000, "Giỏ hàng không có sản phẩm nào"),
    LICH_SU_HOA_DON_NOT_FOUND(2004, "LICH_SU_HOA_DON_NOT_FOUND"),
    HOA_DON_CT_NOT_FOUND(2005, "HOA_DON_CT_NOT_FOUND"),


    LOGIN_FAILED(2000, "Login thất bại"), LOGOUT_FAILED(2000, "Logout thất bại"),
    REGISTER_ACCOUNT_FAILED(2000, "Đăng ký thất bại"),
    USER_USERNAME_EXISTED(2000, "Tên tài khoản đã tồn tại"),
    ACCOUNT_EMAIL_EXISTED(2000, "Email tài khoản đã tồn tại"),
    USER_CANT_CREATE_USER(2000, "Không thể tạo mới khách hàng"),
    ROLE_NOT_FOUND(2000, "Vai trò không tồn tại"),
    VOUCHER_OFF(2404, "VOUCHER_OFF"),
    EMAIL_INCORRECT_FORMAT(2000, "Email không đúng định dạng"),
    TOKEN_RESET_PASSWORD_INVALID(2005, "TOKEN RESET PASSWORD INVALID"),
    EMAIL_SEND_FAILED(2000, "Gửi email không thành công"),
    VOUCHER_INVALID(2000, "Voucher đã không còn hoạt động"),
    SO_LUONG_SAN_PHAM_KHONG_DU(2000, "Số lượng sản phẩm mua nhiều hơn số lượng có sẵn"),
    TOTAL_BILL_CANT_BE_THAN_LIMIT(2000, "Giá trị đơn hàng không được lớn hơn 10 triệu"),
    TOTAL_ITEM_CAN_BE_THAN_LIMIT(2000, "Tổng số lượng sản phẩm không được lơn hơn 20"),
    TOTAL_IN_A_ITEM(2000, "Mỗi sản phẩm không được mua quá số lượng 5"),
    ACCOUNT_NOT_ACTIVE(2000, "Tài khoản đã bị khóa không thể đăng nhập"),
    CANT_BE_CANCEL_WHEN_SHIPPING(2000, "Không thể hủy khi đơn hàng đã chuyển sang trạng thái giao hàng hoặc đã hoàn thành"),
    BRAND_EXISTS(1100, "Hãng đã tồn tại!"),
    COLOR_EXISTS(1200, "Color đã tồn tại!"),
    NAME_EXISTS(3000, "Tên đã tồn tại!"),
    PRODUCT_DETAIL_EXISTS(4100, "Đã tồn tại chi tiết này rồi");

    private int code;
    private String message;
}
