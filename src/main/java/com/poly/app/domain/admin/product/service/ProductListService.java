package com.poly.app.domain.admin.product.service;

import com.poly.app.domain.admin.product.response.product.ProductResponse;
import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

public interface ProductListService {
    List<ProductResponse> getAllIn();
    Page<ProductResponse> getAllIn (Pageable pageable);
    List<Map<String, Object>> searchProduct(String productName);


}
