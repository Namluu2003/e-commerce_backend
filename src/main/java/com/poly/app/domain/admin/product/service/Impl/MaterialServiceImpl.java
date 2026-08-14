package com.poly.app.domain.admin.product.service.Impl;

import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.domain.admin.product.response.material.MaterialResponseSelect;
import com.poly.app.domain.model.Material;
import com.poly.app.domain.repository.MaterialRepository;
import com.poly.app.domain.admin.product.request.material.MaterialRequest;
import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.domain.admin.product.service.MaterialService;
import com.poly.app.infrastructure.constant.Status;
import com.poly.app.infrastructure.exception.ApiException;
import com.poly.app.infrastructure.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class MaterialServiceImpl implements MaterialService {

    MaterialRepository materialRepository;


    @Override
    public Material createMaterial(MaterialRequest request) {
        if (materialRepository.existsByMaterialName(request.getMaterialName())) {
            throw new ApiException(ErrorCode.NAME_EXISTS);
        }
        Material material = Material.builder()
                .materialName(request.getMaterialName())
                .status(Status.HOAT_DONG)
                .build();
        return materialRepository.save(material);
    }

    @Override
    public MaterialResponse updateMaterial(MaterialRequest request, int id) {
        Material material = materialRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id ko tồn tại"));
        if (materialRepository.existsByMaterialNameAndIdNot(request.getMaterialName(), id)) {
            throw new ApiException(ErrorCode.NAME_EXISTS);
        }
        material.setMaterialName(request.getMaterialName());


        materialRepository.save(material);

        return MaterialResponse.builder()
                .code(material.getCode())
                .id(material.getId())
                .materialName(material.getMaterialName())
                .updateAt(material.getCreatedAt())
                .status(material.getStatus())
                .build();
    }

    @Override
    public Page<MaterialResponse> getAllMaterial(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MaterialResponse> response= materialRepository.getAll(pageable);
        return response ;    }

    @Override
    public String deleteMaterial(int id) {
        if ( !materialRepository.findById(id).isEmpty()){
            materialRepository.deleteById(id);
            return "xóa thành công";
        }else{
            return "id ko tồn tại";
        }


    }

    @Override
    public MaterialResponse getMaterial(int id) {
        Material material = materialRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id ko tồn tại"));

        return MaterialResponse.builder()
                .code(material.getCode())
                .id(material.getId())
                .materialName(material.getMaterialName())
                .updateAt(material.getUpdatedAt())
                .status(material.getStatus())
                .build();
    }

    @Override
    public Page<MaterialResponse> fillbyName(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);

        Page<MaterialResponse> material = materialRepository.fillbyname(String.format("%%%s%%", name), pageable);
        log.info(name);

        return material;
    }


    @Override
    public boolean existsByMaterialName(String brandName) {
        return false;
    }

    @Override
    public boolean existsByMaterialNameAndIdNot(String brandName, Integer id) {
        return false;
    }

    @Override
    public List<MaterialResponseSelect> getAll() {
        return materialRepository.dataSelect();
    }

    @Override
    public List<MaterialResponseSelect> getAllhd() {
        return materialRepository.dataSelecthd();
    }

    @Override
    public String switchStatus(Integer id, Status status) {
        Material brand = materialRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));
        if (status.equals(Status.HOAT_DONG)) {
            brand.setStatus(Status.HOAT_DONG);
            materialRepository.save(brand);
            return "hoat dong";
        } else {
            brand.setStatus(Status.NGUNG_HOAT_DONG);
            materialRepository.save(brand);
            return "ngung hoat dong";

        }

    }
}
