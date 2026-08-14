package com.poly.app.domain.admin.bill.service;

import com.poly.app.domain.model.ProductDetail;
import com.poly.app.domain.model.TemporaryHold;
import com.poly.app.domain.repository.ProductDetailRepository;
import com.poly.app.domain.repository.TemporaryHoldRepository;
import com.poly.app.infrastructure.constant.HoldStatus;
import com.poly.app.infrastructure.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ProductHoldService {

    @Autowired
    private ProductDetailRepository productRepository;

    @Autowired
    private TemporaryHoldRepository temporaryHoldRepository;




}