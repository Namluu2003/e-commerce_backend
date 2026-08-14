package com.poly.app.domain.admin.bill.response;

public interface PaymentBillResponse {

    String getPaymentBillCode();

    String getPaymentMethodsType();

    String getPaymentMethod();

    String getNotes();

    String getTransactionCode();

    Double getTotalMoney();

    Double getCreatedAt();

    String getPaymentBillStatus();



}
