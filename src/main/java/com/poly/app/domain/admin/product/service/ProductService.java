package com.poly.app.domain.admin.product.service;

import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.admin.product.response.product.IProductResponse;
import com.poly.app.domain.admin.product.response.product.ProductResponseSelect;
import com.poly.app.domain.admin.promotion.response.PromotionResponse;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.domain.model.Product;
import com.poly.app.domain.admin.product.request.product.ProductRequest;
import com.poly.app.domain.admin.product.response.product.ProductResponse;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.infrastructure.constant.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {


     Product createProduct(ProductRequest request);

     ProductResponse updateProduct(ProductRequest request, int id);

     Page<IProductResponse> getAllProduct(int page, int product);

     Page<IProductResponse> fillbyProductName(int page, int product, String name);

     String delete(int id);

     ProductResponse getProduct(int id);

     boolean existsByProductName(String productName);

     boolean existsByProductNameAndIdNot (String productName, Integer id);
     List<ProductResponseSelect> getAll();
     List<ProductResponseSelect> getAllhd();
     String switchStatus(Integer id, Status status);

}
