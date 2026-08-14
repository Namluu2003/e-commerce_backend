package com.poly.app.domain.admin.product.service;

import com.poly.app.domain.admin.product.response.brand.BrandResponse;
import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.admin.product.response.color.ColorResponseSelect;
import com.poly.app.domain.model.Color;
import com.poly.app.domain.admin.product.request.color.ColorRequest;
import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.infrastructure.constant.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ColorService {
     Page<ColorResponse> getAllColor(int page, int size);
     Color createColor(ColorRequest request);
     ColorResponse updateColor(ColorRequest request, int id);
     String deleteColor (int id);
     ColorResponse getColor(int id);

     Page<ColorResponse> fillbyName(int page, int size, String name);

     boolean existsByColorName(String brandName);

     boolean existsByColorNameAndIdNot (String brandName, Integer id);
     List<ColorResponseSelect> getAll();
     List<ColorResponseSelect> getAllHD();

     String switchStatus(Integer id, Status status);

}
