package com.poly.app.domain.admin.voucher.service;

public class tam {
    // Tìm voucher theo tên
//    @Override
//    public List<VoucherReponse> searchVoucherByName(String voucherName) {
//        return voucherRepository.findByVoucherNameContainingIgnoreCase(voucherName)
//                .stream()
//                .map(VoucherReponse::formEntity)
//                .collect(Collectors.toList());
//    }
//
//    // 🔍 Tìm voucher theo trạng thái
//    @Override
//    public List<VoucherReponse> searchVoucherByStatus(StatusEnum statusEnum) {
//        return voucherRepository.findByStatusEnum(statusEnum)
//                .stream()
//                .map(VoucherReponse::formEntity)
//                .collect(Collectors.toList());
//    }
//
//    // 🔍 Tìm voucher theo số lượng
//    @Override
//    public List<VoucherReponse> searchVoucherByQuantity(Integer quantity) {
//        return voucherRepository.findByQuantity(quantity)
//                .stream()
//                .map(VoucherReponse::formEntity)
//                .collect(Collectors.toList());
//    }
//
//    // 🔍 Tìm voucher theo loại
//    @Override
//    public List<VoucherReponse> searchVoucherByType(VoucherType voucherType) {
//        return voucherRepository.findByVoucherType(voucherType)
//                .stream()
//                .map(VoucherReponse::formEntity)
//                .collect(Collectors.toList());
//    }
//
//    // 🔍 Tìm voucher theo giá trị giảm tối đa
//    @Override
//    public List<VoucherReponse> searchVoucherByDiscountMaxValue(Double discountMaxValue) {
//        return voucherRepository.findByDiscountMaxValue(discountMaxValue)
//                .stream()
//                .map(VoucherReponse::formEntity)
//                .collect(Collectors.toList());
//    }
//
//    // 🔍 Tìm voucher theo giá trị giảm tối thiểu
//    @Override
//    public List<VoucherReponse> searchVoucherByBillMinValue(Double billMinValue) {
//        return voucherRepository.findByBillMinValue(billMinValue)
//                .stream()
//                .map(VoucherReponse::formEntity)
//                .collect(Collectors.toList());
//    }
//
//    // 🔍 Tìm voucher theo khoảng ngày bắt đầu và kết thúc
//    @Override
//    public List<VoucherReponse> searchVoucherByStartDateRange(LocalDateTime startDate, LocalDateTime endDate) {
//        return voucherRepository.findByStartDateBetween(startDate, endDate)
//                .stream()
//                .map(VoucherReponse::formEntity)
//                .collect(Collectors.toList());
//    }





    // 🔍 Tìm kiếm voucher theo tên
//    @GetMapping("/search/by-name")
//    public ApiResponse<List<VoucherReponse>> searchVoucherByName(@RequestParam String voucherName) {
//        return ApiResponse.<List<VoucherReponse>>builder()
//                .message("Search results by name")
//                .data(voucherService.searchVoucherByName(voucherName))
//                .build();
//    }
//
//    // 🔍 Tìm kiếm voucher theo trạng thái
//    @GetMapping("/search/by-status")
//    public ApiResponse<List<VoucherReponse>> searchVoucherByStatus(@RequestParam StatusEnum status) {
//        return ApiResponse.<List<VoucherReponse>>builder()
//                .message("Search results by status")
//                .data(voucherService.searchVoucherByStatus(status))
//                .build();
//    }
//
//    // 🔍 Tìm kiếm voucher theo số lượng
//    @GetMapping("/search/by-quantity")
//    public ApiResponse<List<VoucherReponse>> searchVoucherByQuantity(@RequestParam Integer quantity) {
//        return ApiResponse.<List<VoucherReponse>>builder()
//                .message("Search results by quantity")
//                .data(voucherService.searchVoucherByQuantity(quantity))
//                .build();
//    }
//
//    // 🔍 Tìm kiếm voucher theo loại
//    @GetMapping("/search/by-type")
//    public ApiResponse<List<VoucherReponse>> searchVoucherByType(@RequestParam VoucherType voucherType) {
//        return ApiResponse.<List<VoucherReponse>>builder()
//                .message("Search results by voucher type")
//                .data(voucherService.searchVoucherByType(voucherType))
//                .build();
//    }
//
//    // 🔍 Tìm kiếm voucher theo giá trị giảm tối đa
//    @GetMapping("/search/by-discount-max")
//    public ApiResponse<List<VoucherReponse>> searchVoucherByDiscountMax(@RequestParam Double discountMaxValue) {
//        return ApiResponse.<List<VoucherReponse>>builder()
//                .message("Search results by discount max value")
//                .data(voucherService.searchVoucherByDiscountMaxValue(discountMaxValue))
//                .build();
//    }
//
//    // 🔍 Tìm kiếm voucher theo giá trị giảm tối thiểu
//    @GetMapping("/search/by-bill-min")
//    public ApiResponse<List<VoucherReponse>> searchVoucherByBillMin(@RequestParam Double billMinValue) {
//        return ApiResponse.<List<VoucherReponse>>builder()
//                .message("Search results by bill min value")
//                .data(voucherService.searchVoucherByBillMinValue(billMinValue))
//                .build();
//    }
//
//    // 🔍 Tìm kiếm voucher theo khoảng thời gian bắt đầu và kết thúc
//    @GetMapping("/search/by-date-range")
//    public ApiResponse<List<VoucherReponse>> searchVoucherByDateRange(
//            @RequestParam LocalDateTime startDate,
//            @RequestParam LocalDateTime endDate) {
//        return ApiResponse.<List<VoucherReponse>>builder()
//                .message("Search results by start date range")
//                .data(voucherService.searchVoucherByStartDateRange(startDate, endDate))
//                .build();
//    }



    //    List<Voucher> findByVoucherNameContainingIgnoreCase(String voucherName);
//    // 🔍 Tìm kiếm theo trạng thái voucher (statusEnum)
//    List<Voucher> findByStatusEnum(StatusEnum statusEnum);
//
//    // 🔍 Tìm kiếm theo số lượng voucher (quantity)
//    List<Voucher> findByQuantity(Integer quantity);
//
//    // 🔍 Tìm kiếm theo loại voucher (voucherType)
//    List<Voucher> findByVoucherType(VoucherType voucherType);
//
//    // 🔍 Tìm kiếm theo giá trị giảm tối đa (discountMaxValue)
//    List<Voucher> findByDiscountMaxValue(Double discountMaxValue);
//
//    // 🔍 Tìm kiếm theo giá trị giảm tối thiểu (billMinValue)
//    List<Voucher> findByBillMinValue(Double billMinValue);
//
//    // 🔍 Tìm kiếm theo khoảng thời gian bắt đầu (startDate) và kết thúc (endDate)
//    List<Voucher> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);



    //    // 🔍 Tìm voucher theo tên
//    List<VoucherReponse> searchVoucherByName(String voucherName);
//
//    // 🔍 Tìm voucher theo trạng thái
//    List<VoucherReponse> searchVoucherByStatus(StatusEnum statusEnum);
//
//    // 🔍 Tìm voucher theo số lượng
//    List<VoucherReponse> searchVoucherByQuantity(Integer quantity);
//
//    // 🔍 Tìm voucher theo loại
//    List<VoucherReponse> searchVoucherByType(VoucherType voucherType);
//
//    // 🔍 Tìm voucher theo giá trị giảm tối đa
//    List<VoucherReponse> searchVoucherByDiscountMaxValue(Double discountMaxValue);
//
//    // 🔍 Tìm voucher theo giá trị giảm tối thiểu
//    List<VoucherReponse> searchVoucherByBillMinValue(Double billMinValue);
//
//    // 🔍 Tìm voucher theo khoảng thời gian bắt đầu và kết thúc
//    List<VoucherReponse> searchVoucherByStartDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
