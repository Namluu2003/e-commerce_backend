package com.poly.app.domain.admin.product.service;

import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.model.Brand;
import com.poly.app.domain.admin.product.request.brand.BrandRequest;
import com.poly.app.domain.admin.product.response.brand.BrandResponse;
import com.poly.app.infrastructure.constant.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {

    Brand createBrand(BrandRequest request);

    BrandResponse updateBrand(BrandRequest request, int id);

    Page<BrandResponse> getAllBrand(int page, int size);

    Page<BrandResponse> fillbyBrandName(int page, int size, String name);

    String delete(int id);

    BrandResponse getBrand(int id);

    boolean existsByBrandName(String brandName);

    boolean existsByBrandNameAndIdNot (String brandName, Integer id);

    List<BrandResponseSelect> getAll();
    List<BrandResponseSelect> getAllHD();

    String switchStatus(Integer id, Status  status);
}
