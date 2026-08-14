package com.poly.app.domain.admin.product.service.Impl;

import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.domain.admin.product.response.type.TypeResponseSelect;
import com.poly.app.domain.model.Type;
import com.poly.app.domain.repository.TypeRepository;
import com.poly.app.domain.admin.product.request.type.TypeRequest;
import com.poly.app.domain.admin.product.response.type.TypeResponse;
import com.poly.app.domain.admin.product.service.TypeService;
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

public class TypeServiceImpl implements TypeService {

    TypeRepository typeRepository;


    @Override
    public Type createType(TypeRequest request) {
        if (typeRepository.existsByTypeName(request.getTypeName())) {
            throw new ApiException(ErrorCode.NAME_EXISTS );
        }
        Type type = Type.builder()
                .typeName(request.getTypeName())
                .status(Status.HOAT_DONG)
                .build();
        return typeRepository.save(type);
    }

    @Override
    public TypeResponse updateType(TypeRequest request, int id) {
        Type type = typeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));

        if (typeRepository.existsByTypeNameAndIdNot(request.getTypeName(),id)) {
            throw new ApiException(ErrorCode.NAME_EXISTS );
        }
        type.setTypeName(request.getTypeName());


        typeRepository.save(type);

        return TypeResponse.builder()
                .code(type.getCode())
                .id(type.getId())
                .typeName(type.getTypeName())
                .updateAt(type.getCreatedAt())
                .status(type.getStatus())
                .build();
    }


    @Override
    public Page<TypeResponse> getAllType(int page, int type) {
        Pageable pageable = PageRequest.of(page, type);
        Page<TypeResponse> response= typeRepository.getAll(pageable);
        return response ;    }

    @Override
    public Page<TypeResponse> fillbyTypeName(int page, int type, String name) {
        Pageable pageable = PageRequest.of(page, type);


        Page<TypeResponse> typePage = typeRepository.fillbyname(String.format("%%%s%%", name), pageable);
        log.info(name);
        // Chuyển đổi từ Page<Type> sang Page<TypeResponse>
        return typePage;
    }


    @Override
    public String delete(int id) {
        if (!typeRepository.findById(id).isEmpty()) {
            typeRepository.deleteById(id);
            return "xóa thành công";
        } else {
            return "id ko tồn tại";
        }


    }

    @Override
    public TypeResponse getType(int id) {
        Type type = typeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));

        return TypeResponse.builder()
                .code(type.getCode())
                .id(type.getId())
                .typeName(type.getTypeName())
                .updateAt(type.getUpdatedAt())
                .status(type.getStatus())
                .build();
    }

    @Override
    public boolean existsByTypeName(String typeName) {
        if (typeRepository.existsByTypeName(typeName)) return true;
        return false;
    }

    @Override
    public boolean existsByTypeNameAndIdNot(String typeName, Integer id) {
        if (typeRepository.existsByTypeNameAndIdNot(typeName, id)) return true;
        return false;
    }

    @Override
    public List<TypeResponseSelect> getAll() {
        return typeRepository.dataSelect();
    }

    @Override
    public List<TypeResponseSelect> getAllhd() {
        return typeRepository.dataSelecthd();
    }

    @Override
    public String switchStatus(Integer id, Status status) {
        Type brand = typeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));
        if (status.equals(Status.HOAT_DONG)) {
            brand.setStatus(Status.HOAT_DONG);
            typeRepository.save(brand);
            return "hoat dong";
        } else {
            brand.setStatus(Status.NGUNG_HOAT_DONG);
            typeRepository.save(brand);
            return "ngung hoat dong";

        }

    }
}
