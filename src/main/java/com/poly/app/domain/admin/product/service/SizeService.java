package com.poly.app.domain.admin.product.service;

import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.admin.product.response.size.SizeResponseSelect;
import com.poly.app.domain.model.Size;
import com.poly.app.domain.admin.product.request.size.SizeRequest;
import com.poly.app.domain.admin.product.response.size.SizeResponse;
import com.poly.app.infrastructure.constant.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SizeService {

     Size createSize(SizeRequest request);

     SizeResponse updateSize(SizeRequest request, int id);

     Page<SizeResponse> getAllSize(int page, int size);

     Page<SizeResponse> fillbySizeName(int page, int size, String name);

     String delete(int id);

     SizeResponse getSize(int id);

     boolean existsBySizeName(String sizeName);

     boolean existsBySizeNameAndIdNot (String sizeName, Integer id);
     List<SizeResponseSelect> getAll();
     List<SizeResponseSelect> getAllhd();

     String switchStatus(Integer id, Status status);

}
