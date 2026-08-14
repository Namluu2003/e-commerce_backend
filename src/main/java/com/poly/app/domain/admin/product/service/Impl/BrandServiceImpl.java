package com.poly.app.domain.admin.product.service.Impl;

import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.model.Brand;
import com.poly.app.domain.repository.BrandRepository;
import com.poly.app.domain.admin.product.request.brand.BrandRequest;
import com.poly.app.domain.admin.product.response.brand.BrandResponse;
import com.poly.app.domain.admin.product.service.BrandService;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class BrandServiceImpl implements BrandService {

    BrandRepository brandRepository;


    @Override
    public Brand createBrand(BrandRequest request) {
        if (brandRepository.existsByBrandName(request.getBrandName())) {
            throw new ApiException(ErrorCode.BRAND_EXISTS);
        }
        Brand brand = Brand.builder()
                .brandName(request.getBrandName())
                .status(Status.HOAT_DONG)
                .build();
        return brandRepository.save(brand);
    }

    @Override
    public BrandResponse updateBrand(BrandRequest request, int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));

        if (brandRepository.existsByBrandNameAndIdNot(request.getBrandName(), id)) {
            throw new ApiException(ErrorCode.BRAND_EXISTS);
        }
        brand.setBrandName(request.getBrandName());
        brand.setStatus(Status.HOAT_DONG);

        brandRepository.save(brand);

        return BrandResponse.builder()
                .code(brand.getCode())
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .updateAt(brand.getCreatedAt())
                .status(brand.getStatus())
                .build();
    }

    @Override
    public Page<BrandResponse> getAllBrand(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Thay vì stream và toList, sử dụng phương thức map của Page
        Page<Brand> brandPage = brandRepository.getAll(pageable);

        // Chuyển đổi từ Page<Brand> sang Page<BrandResponse>
        return brandPage.map(brand -> new BrandResponse(
                brand.getId(),
                brand.getCode(),
                brand.getBrandName(),
                brand.getUpdatedAt(),
                brand.getStatus()
        ));
    }

    @Override
    public Page<BrandResponse> fillbyBrandName(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);


        Page<BrandResponse> brandPage = brandRepository.fillbyname(String.format("%%%s%%", name), pageable);
        log.info(name);
        // Chuyển đổi từ Page<Brand> sang Page<BrandResponse>
        return brandPage;
    }


    @Override
    public String delete(int id) {
        if (!brandRepository.findById(id).isEmpty()) {
            brandRepository.deleteById(id);
            return "xóa thành công";
        } else {
            return "id ko tồn tại";
        }


    }

    @Override
    public BrandResponse getBrand(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));

        return BrandResponse.builder()
                .code(brand.getCode())
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .updateAt(brand.getUpdatedAt())
                .status(brand.getStatus())
                .build();
    }

    @Override
    public boolean existsByBrandName(String brandName) {
        if (brandRepository.existsByBrandName(brandName)) return true;
        return false;
    }

    @Override
    public boolean existsByBrandNameAndIdNot(String brandName, Integer id) {
        if (brandRepository.existsByBrandNameAndIdNot(brandName, id)) return true;
        return false;
    }

    @Override
    public List<BrandResponseSelect> getAll() {
        return brandRepository.dataSelect();
    }

    @Override
    public List<BrandResponseSelect> getAllHD() {
        return brandRepository.dataSelectHD();    }

    @Override
    public String switchStatus(Integer id, Status status) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));
        if (status.equals(Status.HOAT_DONG)) {
            brand.setStatus(Status.HOAT_DONG);
            brandRepository.save(brand);
            return "hoat dong";
        } else {
            brand.setStatus(Status.NGUNG_HOAT_DONG);
            brandRepository.save(brand);
            return "ngung hoat dong";

        }

    }


}
