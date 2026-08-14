package com.poly.app.domain.repository;

import com.poly.app.domain.model.PaymentMethods;
import com.poly.app.infrastructure.constant.PaymentMethodEnum;
import com.poly.app.infrastructure.constant.PaymentMethodsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods,Integer> {
    Optional<PaymentMethods> findByPaymentMethodsTypeAndAndPaymentMethod(PaymentMethodsType paymentMethodsType, PaymentMethodEnum paymentMethodEnum);
}
