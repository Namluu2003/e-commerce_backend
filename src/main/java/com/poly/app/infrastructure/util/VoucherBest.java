package com.poly.app.infrastructure.util;

import com.poly.app.domain.client.response.VoucherBestResponse;
import com.poly.app.domain.model.Voucher;
import com.poly.app.infrastructure.constant.DiscountType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VoucherBest {


    public VoucherBestResponse recommendBestVoucher(double billValue, List<Voucher> vouchers) {
        Optional<Voucher> bestVoucher = vouchers.stream()
                .filter(v -> billValue >= v.getBillMinValue())
                .max(Comparator.comparingDouble(v -> calculateDiscount(v, billValue)));

        if (bestVoucher.isPresent()) {
            String result = "Best voucher: " + bestVoucher.get().getVoucherName() + " - Discount: " +
                            CurrencyFormatter.formatCurrencyVND(calculateDiscount(bestVoucher.get(), billValue));
            String additionalSuggestion = suggestAdditionalPurchaseForBetterVoucher(billValue,vouchers);
            return VoucherBestResponse.builder()
                    .voucher(bestVoucher.get())
                    .note(additionalSuggestion)
                    .discount(calculateDiscount(bestVoucher.get(), billValue))
                    .build();
        } else {
            return VoucherBestResponse.builder()
                    .note(suggestAdditionalPurchase(billValue,vouchers))
                    .build();
        }
    }

    private double calculateDiscount(Voucher voucher, double billValue) {
        if ("PERCENT".equalsIgnoreCase(voucher.getDiscountType().name())) {
            double discount = (billValue * voucher.getDiscountValue()) / 100;
            return Math.min(discount, voucher.getDiscountMaxValue());
        } else {
            return Math.min(voucher.getDiscountValue(), voucher.getDiscountMaxValue());
        }
    }

    private String suggestAdditionalPurchase(double billValue,List<Voucher> vouchers) {
        List<Voucher> possibleVouchers = vouchers.stream()
                .filter(v -> billValue < v.getBillMinValue())
                .sorted(Comparator.comparingDouble(Voucher::getBillMinValue))
                .collect(Collectors.toList());

        if (!possibleVouchers.isEmpty()) {
            Voucher suggestedVoucher = possibleVouchers.get(0);
            double amountNeeded = suggestedVoucher.getBillMinValue() - billValue;
            return "Bạn chỉ cần mua thêm " + CurrencyFormatter.formatCurrencyVND(amountNeeded)+" đ" + " để sử dụng mã voucher " + suggestedVoucher.getVoucherCode();
        }
        return "Không có voucher phù hợp hiện tại.";
    }

    private String suggestAdditionalPurchaseForBetterVoucher(double billValue,List<Voucher> vouchers) {
        Optional<Voucher> betterVoucher = vouchers.stream()
                .filter(v -> billValue < v.getBillMinValue())
                .min(Comparator.comparingDouble(Voucher::getBillMinValue));

        if (betterVoucher.isPresent()) {
            double amountNeeded = betterVoucher.get().getBillMinValue() - billValue;
            return "Đã áp dụng voucher tốt nhất \n bạn chỉ cần mua thêm " + CurrencyFormatter.formatCurrencyVND(amountNeeded) +" đ"+
                   " để sử dụng voucher tốt hơn: " + betterVoucher.get().getVoucherName()+
                   "-"+betterVoucher.get().getVoucherCode()+
                   " tiết kiệm lên tới "+CurrencyFormatter.formatCurrencyVND(betterVoucher.get().getDiscountValue())+(betterVoucher.get().getDiscountType().equals(DiscountType.PERCENT)?"%":"đ");
        }
        return "";
    }


}