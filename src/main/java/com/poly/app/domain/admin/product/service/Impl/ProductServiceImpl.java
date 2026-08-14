package com.poly.app.domain.admin.product.service.Impl;

import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.domain.admin.product.response.product.IProductResponse;
import com.poly.app.domain.admin.product.response.product.ProductResponseSelect;
import com.poly.app.domain.admin.promotion.response.PromotionResponse;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.domain.model.Product;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.domain.model.Voucher;
import com.poly.app.domain.repository.ProductDetailRepository;
import com.poly.app.domain.repository.ProductRepository;
import com.poly.app.domain.admin.product.request.product.ProductRequest;
import com.poly.app.domain.admin.product.response.product.ProductResponse;
import com.poly.app.domain.admin.product.service.ProductService;
import com.poly.app.infrastructure.constant.DiscountType;
import com.poly.app.infrastructure.constant.Status;
import com.poly.app.infrastructure.constant.VoucherType;
import com.poly.app.infrastructure.exception.ApiException;
import com.poly.app.infrastructure.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    ProductDetailRepository productDetailRepository;

    @Override
    public Product createProduct(ProductRequest request) {
        if (productRepository.existsByProductName(request.getProductName())) {
            throw new ApiException(ErrorCode.NAME_EXISTS);
        }
        Product product = Product.builder()
                .productName(request.getProductName())
                .status(Status.HOAT_DONG)
                .build();
        return productRepository.save(product);
    }

    @Override
    public ProductResponse updateProduct(ProductRequest request, int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));

        if (productRepository.existsByProductNameAndIdNot(request.getProductName(), id)) {
            throw new ApiException(ErrorCode.NAME_EXISTS);
        }
        product.setProductName(request.getProductName());

        productRepository.save(product);

        return ProductResponse.builder()
                .code(product.getCode())
                .id(product.getId())
                .productName(product.getProductName())
                .updateAt(product.getCreatedAt())
                .status(product.getStatus())
                .build();
    }


    @Override
    public Page<IProductResponse> getAllProduct(int page, int product) {
        Pageable pageable = PageRequest.of(page, product);
        Page<IProductResponse> response = productRepository.getAll(pageable);
        return response;
    }

    @Override
    public Page<IProductResponse> fillbyProductName(int page, int product, String name) {
        Pageable pageable = PageRequest.of(page, product);


        Page<IProductResponse> productPage = productRepository.fillbyname(String.format("%%%s%%", name), pageable);
        log.info(name);
        // Chuyển đổi từ Page<Product> sang Page<ProductResponse>
        return productPage;
    }


    @Override
    public String delete(int id) {
        if (!productRepository.findById(id).isEmpty()) {
            productRepository.deleteById(id);
            return "xóa thành công";
        } else {
            return "id ko tồn tại";
        }


    }

    @Override
    public ProductResponse getProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));

        return ProductResponse.builder()
                .code(product.getCode())
                .id(product.getId())
                .productName(product.getProductName())
                .updateAt(product.getUpdatedAt())
                .status(product.getStatus())
                .build();
    }

    @Override
    public boolean existsByProductName(String productName) {
        if (productRepository.existsByProductName(productName)) return true;
        return false;
    }

    @Override
    public boolean existsByProductNameAndIdNot(String productName, Integer id) {
        if (productRepository.existsByProductNameAndIdNot(productName, id)) return true;
        return false;
    }

    @Override
    public List<ProductResponseSelect> getAll() {
        return productRepository.dataSelect();
    }

    @Override
    public List<ProductResponseSelect> getAllhd() {
        return productRepository.dataSelecthd();

    }

    @Transactional
    @Override
    public String switchStatus(Integer id, Status status) {
        Product brand = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));
        if (status.equals(Status.HOAT_DONG)) {
            brand.setStatus(Status.HOAT_DONG);
            productRepository.save(brand);
            productDetailRepository.switchStatusPD("0", id);
            return "hoat dong";
        } else {
            brand.setStatus(Status.NGUNG_HOAT_DONG);
            productRepository.save(brand);
            productDetailRepository.switchStatusPD("1", id);
            return "ngung hoat dong";

        }
    }
    //em tú làm

    // em tú hết làm
}
