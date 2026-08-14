package com.poly.app.domain.admin.product.controller;

import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.common.Meta;
import com.poly.app.domain.model.Brand;
import com.poly.app.domain.admin.product.request.brand.BrandRequest;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.admin.product.response.brand.BrandResponse;
import com.poly.app.domain.admin.product.service.BrandService;
import com.poly.app.infrastructure.constant.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/brand")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "brand controller")
public class BrandController {
    BrandService brandService;

    @PostMapping("/add")
    public ApiResponse<Brand> create(@RequestBody @Valid BrandRequest request) {
        return ApiResponse.<Brand>builder()
                .message("craete brand")
                .data(brandService.createBrand(request))
                .build();
    }

    @Operation(summary = "sua brand", description = "sua brand voi id")
    @PutMapping("/update/{id}")
    public ApiResponse<BrandResponse> update(@RequestBody BrandRequest request, @PathVariable int id) {
        return ApiResponse.<BrandResponse>builder()
                .message("update brand")
                .data(brandService.updateBrand(request, id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<BrandResponse>> getAllBrand(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "2") int size
    ) {

        Page<BrandResponse> page1 = brandService.getAllBrand(page - 1, size);
        return ApiResponse.<List<BrandResponse>>builder()
                .message("list brand")
                .data(page1.getContent())
                .meta(Meta.builder()
                        .totalElement(page1.getTotalElements())
                        .currentPage(page1.getNumber() + 1)
                        .totalPages(page1.getTotalPages())
                        .build())
                .build();
    }

    @GetMapping("{id}")
    public ApiResponse<BrandResponse> getBrand(@PathVariable int id) {
        return ApiResponse.<BrandResponse>builder()
                .message("get brand by id")
                .data(brandService.getBrand(id))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<BrandResponse>> getBrand(@RequestParam("name") String name,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "5") int size) {

        Page<BrandResponse> page1 = brandService.fillbyBrandName(page - 1, size, name);
        return ApiResponse.<List<BrandResponse>>builder()
                .message("get brand by id")
                .data(page1.getContent())
                .meta(Meta.builder()
                        .totalElement(page1.getTotalElements())
                        .currentPage(page1.getNumber() + 1)
                        .totalPages(page1.getTotalPages())
                        .build())
                .build();
    }

    @DeleteMapping("{id}")
    public ApiResponse<String> delete(@PathVariable int id) {
        return ApiResponse.<String>builder()
                .message("delete by id")
                .data(brandService.delete(id))
                .build();
    }

    @GetMapping("/existsbybrandname")
    public ApiResponse<Boolean> existsByBrandName(@RequestParam String brandName) {
        return ApiResponse.<Boolean>builder()
                .message("existsByBrandName")
                .data(brandService.existsByBrandName(brandName))
                .build();
    }

    @GetMapping("/existsbybrandnameandidnot")
    public ApiResponse<Boolean> existsByBrandNameAndIdNot(@RequestParam String brandName, @RequestParam Integer id) {
        return ApiResponse.<Boolean>builder()
                .message("existsByBrandName")
                .data(brandService.existsByBrandNameAndIdNot(brandName, id))
                .build();
    }

    @GetMapping("/getallselect")
    public ApiResponse<List<BrandResponseSelect>> getAllSelect() {
        return ApiResponse.<List<BrandResponseSelect>>builder()
                .message("get all selected")
                .data(brandService.getAll())
                .build();
    }
    @GetMapping("/getallselecthd")
    public ApiResponse<List<BrandResponseSelect>> getAllSelecthd() {
        return ApiResponse.<List<BrandResponseSelect>>builder()
                .message("get all selected hd")
                .data(brandService.getAllHD())
                .build();
    }

    @GetMapping("/switchstatus")
    public ApiResponse<?> getAllSelect(@RequestParam("status") Status status,
                                       @RequestParam("id") int id
    ) {
        return ApiResponse.<String>builder()
                .message("get all selected")
                .data(brandService.switchStatus(id, status))
                .build();
    }
}
