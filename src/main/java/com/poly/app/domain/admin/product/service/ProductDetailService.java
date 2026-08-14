package com.poly.app.domain.admin.product.service;

import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.domain.admin.product.response.product.AllNameProductDetailResponse;
import com.poly.app.domain.admin.product.response.productdetail.*;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.domain.admin.product.request.productdetail.FilterRequest;
import com.poly.app.domain.admin.product.request.productdetail.ProductDetailRequest;
import com.poly.app.infrastructure.constant.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductDetailService {

    ProductDetail createProductDetail(ProductDetailRequest request);

    ProductDetailResponse updateProductDetail(ProductDetailRequest request, int id);

    //     chua phan trang
    List<ProductDetailResponse> getAllProductDetail();

    String deleteProductDetail(int id);

    ProductDetailResponse getProductDetail(int id);

    Page<ProductDetailResponse> getAllProductDetailPage(int page, int size);
    Page<ProductDetailResponse> findByName(int page, int size,String productName);


    //emtu
        List<ProductDetailResponse> getAllProductDetailName(String productName);
    List<ProductDetailResponse> getProductDetailsByProductId(Integer productId);
    AllNameProductDetailResponse getAllFilterOptions();

    //     getall
    List<ProductDetailResponse> getAllProductDetailExportData();

    List<FilterProductDetailResponse> filterProductDetail(int page, int size, FilterRequest request);
    List<FilterProductDetailWithPromotionDTO> filterDetailProductWithPromotion(int page, int size, FilterRequest request);

    Integer getFillterElement(FilterRequest request);

    Page<ColorResponse> fillbyName(int page, int size, String name);

    boolean existsByColorName(String brandName);

    boolean existsByColorNameAndIdNot(String brandName, Integer id);

    List<ProductDetailResponse> createProductDetailList(List<ProductDetailRequest> request);

//    kiểm tra xem đã tồn tại hay chưa

    boolean existsProductDetail(ProductDetailRequest request);

    String switchStatus(Integer id, Status status);

ProductAtributeExistResponse getAttributeOfProductExist(Integer productId);
}
