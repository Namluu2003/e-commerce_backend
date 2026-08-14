package com.poly.app.infrastructure.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String formatCurrencyVND(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(amount);
    }

    public static void main(String[] args) {
        double amount = 12345678;
        System.out.println(formatCurrencyVND(amount));
        // Kết quả: 12.345.678,9 ₫
    }
}
