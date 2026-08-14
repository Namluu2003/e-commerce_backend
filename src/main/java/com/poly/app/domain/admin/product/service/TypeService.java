package com.poly.app.domain.admin.product.service;

import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.admin.product.response.type.TypeResponseSelect;
import com.poly.app.domain.model.Type;
import com.poly.app.domain.admin.product.request.type.TypeRequest;
import com.poly.app.domain.admin.product.response.type.TypeResponse;
import com.poly.app.infrastructure.constant.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

     Type createType(TypeRequest request);

     TypeResponse updateType(TypeRequest request, int id);

     Page<TypeResponse> getAllType(int page, int type);

     Page<TypeResponse> fillbyTypeName(int page, int type, String name);

     String delete(int id);

     TypeResponse getType(int id);

     boolean existsByTypeName(String typeName);

     boolean existsByTypeNameAndIdNot (String typeName, Integer id);

     List<TypeResponseSelect> getAll();
     List<TypeResponseSelect> getAllhd();
     String switchStatus(Integer id, Status status);

}
