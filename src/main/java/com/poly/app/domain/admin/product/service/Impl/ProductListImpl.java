package com.poly.app.domain.admin.product.service.Impl;

import com.poly.app.domain.admin.product.response.img.ImgResponse;
import com.poly.app.domain.admin.product.response.product.ProductResponse;
import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.domain.admin.product.service.ProductListService;
import com.poly.app.domain.admin.product.service.ProductService;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.domain.repository.ImageRepository;
import com.poly.app.domain.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductListImpl implements ProductListService {
    @Autowired
    ProductRepository productRepository;
    ImageRepository imageRepository;
    @Override
    public List<ProductResponse> getAllIn() {
        return productRepository.findAll().stream()
                .map(product -> ProductResponse.fromEntity(product)).toList();
    }

    public Page<ProductResponse> getAllIn(Pageable pageable) {

        return productRepository.findAll(pageable).map(ProductResponse::fromEntity


        );
    }
    @Override
    public List<Map<String, Object>> searchProduct(String query) {
        List<Object[]> results;

        // Kiểm tra nếu query là số -> tìm theo số lượng, nếu không -> tìm theo tên sản phẩm
        if (query.matches("\\d+")) {
            int minQuantity = Integer.parseInt(query);
            results = productRepository.findProductByMinQuantity(minQuantity);
        } else {
            results = productRepository.findProductQuantityByName(query);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", row[0]);
            map.put("totalQuantity", row[1]);
            response.add(map);
        }
        return response;
    }




}


