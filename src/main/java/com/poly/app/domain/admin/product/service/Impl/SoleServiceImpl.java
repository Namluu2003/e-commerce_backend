package com.poly.app.domain.admin.product.service.Impl;

import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.domain.admin.product.response.sole.SoleResponseSelect;
import com.poly.app.domain.model.Sole;
import com.poly.app.domain.repository.SoleRepository;
import com.poly.app.domain.admin.product.request.sole.SoleRequest;
import com.poly.app.domain.admin.product.response.sole.SoleResponse;
import com.poly.app.domain.admin.product.service.SoleService;
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

public class SoleServiceImpl implements SoleService {

    SoleRepository soleRepository;


    @Override
    public Sole createSole(SoleRequest request) {
        if (soleRepository.existsBySoleName(request.getSoleName())) {
            throw new ApiException(ErrorCode.NAME_EXISTS );
        }
        Sole sole = Sole.builder()
                .soleName(request.getSoleName())
                .status(Status.HOAT_DONG)
                .build();
        return soleRepository.save(sole);
    }

    @Override
    public SoleResponse updateSole(SoleRequest request, int id) {
        Sole sole = soleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));

        if (soleRepository.existsBySoleNameAndIdNot(request.getSoleName(),id)) {
            throw new ApiException(ErrorCode.NAME_EXISTS );
        }
        sole.setSoleName(request.getSoleName());


        soleRepository.save(sole);

        return SoleResponse.builder()
                .code(sole.getCode())
                .id(sole.getId())
                .soleName(sole.getSoleName())
                .updateAt(sole.getCreatedAt())
                .status(sole.getStatus())
                .build();
    }


    @Override
    public Page<SoleResponse> getAllSole(int page, int sole) {
        Pageable pageable = PageRequest.of(page, sole);
        Page<SoleResponse> response= soleRepository.getAll(pageable);
        return response ;    }

    @Override
    public Page<SoleResponse> fillbySoleName(int page, int sole, String name) {
        Pageable pageable = PageRequest.of(page, sole);


        Page<SoleResponse> solePage = soleRepository.fillbyname(String.format("%%%s%%", name), pageable);
        log.info(name);
        // Chuyển đổi từ Page<Sole> sang Page<SoleResponse>
        return solePage;
    }


    @Override
    public String delete(int id) {
        if (!soleRepository.findById(id).isEmpty()) {
            soleRepository.deleteById(id);
            return "xóa thành công";
        } else {
            return "id ko tồn tại";
        }


    }

    @Override
    public SoleResponse getSole(int id) {
        Sole sole = soleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));

        return SoleResponse.builder()
                .code(sole.getCode())
                .id(sole.getId())
                .soleName(sole.getSoleName())
                .updateAt(sole.getUpdatedAt())
                .status(sole.getStatus())
                .build();
    }

    @Override
    public boolean existsBySoleName(String soleName) {
        if (soleRepository.existsBySoleName(soleName)) return true;
        return false;
    }

    @Override
    public boolean existsBySoleNameAndIdNot(String soleName, Integer id) {
        if (soleRepository.existsBySoleNameAndIdNot(soleName, id)) return true;
        return false;
    }

    @Override
    public List<SoleResponseSelect> getAll() {
        return soleRepository.dataSelect();
    }

    @Override
    public List<SoleResponseSelect> getAllhd() {
        return soleRepository.dataSelecthd();

    }

    @Override
    public String switchStatus(Integer id, Status status) {
        Sole brand = soleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));
        if (status.equals(Status.HOAT_DONG)) {
            brand.setStatus(Status.HOAT_DONG);
            soleRepository.save(brand);
            return "hoat dong";
        } else {
            brand.setStatus(Status.NGUNG_HOAT_DONG);
            soleRepository.save(brand);
            return "ngung hoat dong";

        }

    }
}
