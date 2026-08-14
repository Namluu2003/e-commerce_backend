package com.poly.app.domain.admin.product.service;

import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.domain.admin.product.response.gender.GenderResponseSelect;
import com.poly.app.domain.model.Gender;
import com.poly.app.domain.admin.product.request.gender.GenderRequest;
import com.poly.app.domain.admin.product.response.gender.GenderResponse;
import com.poly.app.infrastructure.constant.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GenderService {
     Page<GenderResponse> getAllGender(int page, int size);
     Gender createGender(GenderRequest request);
     GenderResponse updateGender(GenderRequest request, int id);
     String deleteGender (int id);
     GenderResponse getGender(int id);

     Page<GenderResponse> fillbyName(int page, int size, String name);

     boolean existsByGenderName(String brandName);

     boolean existsByGenderNameAndIdNot (String brandName, Integer id);
     List<GenderResponseSelect> getAll();
     List<GenderResponseSelect> getAllhd();
     String switchStatus(Integer id, Status status);

}
