package com.poly.app.domain.admin.product.service;

import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.domain.admin.product.response.material.MaterialResponseSelect;
import com.poly.app.domain.model.Material;
import com.poly.app.domain.admin.product.request.material.MaterialRequest;
import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.infrastructure.constant.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MaterialService {
     Page<MaterialResponse> getAllMaterial(int page, int size);
     Material createMaterial(MaterialRequest request);
     MaterialResponse updateMaterial(MaterialRequest request, int id);
     String deleteMaterial (int id);
     MaterialResponse getMaterial(int id);

     Page<MaterialResponse> fillbyName(int page, int size, String name);

     boolean existsByMaterialName(String brandName);

     boolean existsByMaterialNameAndIdNot (String brandName, Integer id);
     List<MaterialResponseSelect> getAll();
     List<MaterialResponseSelect> getAllhd();
     String switchStatus(Integer id, Status status);

}
