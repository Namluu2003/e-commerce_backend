package com.poly.app.domain.admin.product.controller;

import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.domain.admin.product.response.material.MaterialResponseSelect;
import com.poly.app.domain.common.Meta;
import com.poly.app.domain.model.Material;
import com.poly.app.domain.admin.product.request.material.MaterialRequest;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.domain.admin.product.service.MaterialService;
import com.poly.app.infrastructure.constant.Status;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/material")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class MaterialController {
    MaterialService materialService;

    @PostMapping("/add")
    public ApiResponse<Material> create(@RequestBody MaterialRequest request) {
        return ApiResponse.<Material>builder()
                .message("create material")
                .data(materialService.createMaterial(request))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<MaterialResponse> update(@RequestBody MaterialRequest request, @PathVariable int id) {
        return ApiResponse.<MaterialResponse>builder()
                .message("update material")
                .data(materialService.updateMaterial(request,id))
                .build();
    }


    @GetMapping("{id}")
    public ApiResponse<MaterialResponse> getMaterial(@PathVariable int id) {
        return ApiResponse.<MaterialResponse>builder()
                .message("get material by id")
                .data(materialService.getMaterial(id))
                .build();
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<String> delete(@PathVariable int id) {
        return ApiResponse.<String>builder()
                .message("delete material by id")
                .data(materialService.deleteMaterial(id))
                .build();
    }
    @GetMapping()
    public ApiResponse<List<MaterialResponse>> getAllBrand(@RequestParam(value = "page", defaultValue = "1") int page,
                                                         @RequestParam(value = "size", defaultValue = "5") int size
    ) {

        Page<MaterialResponse> page1 = materialService.getAllMaterial(page - 1, size);
        return ApiResponse.<List<MaterialResponse>>builder()
                .message("list brand")
                .data(page1.getContent())
                .meta(Meta.builder()
                        .totalElement(page1.getTotalElements())
                        .currentPage(page1.getNumber() + 1)
                        .totalPages(page1.getTotalPages())
                        .build())
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<List<MaterialResponse>> getBrand(@RequestParam("name") String name,
                                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "size", defaultValue = "5") int size) {

        Page<MaterialResponse> page1 = materialService.fillbyName(page - 1, size, name);


        return ApiResponse.<List<MaterialResponse>>builder()
                .message("get brand by id")
                .data(page1.getContent())
                .meta(Meta.builder()
                        .totalElement(page1.getTotalElements())
                        .currentPage(page1.getNumber() + 1)
                        .totalPages(page1.getTotalPages())
                        .build())
                .build();
    }
    @GetMapping("/getallselect")
    public ApiResponse<List<MaterialResponseSelect>> getAllSelect() {
        return ApiResponse.<List<MaterialResponseSelect>>builder()
                .message("get all selected")
                .data(materialService.getAll())
                .build();
    }
    @GetMapping("/getallselecthd")
    public ApiResponse<List<MaterialResponseSelect>> getAllSelecthd() {
        return ApiResponse.<List<MaterialResponseSelect>>builder()
                .message("get all selected hd ")
                .data(materialService.getAllhd())
                .build();
    }

    @GetMapping("/switchstatus")
    public ApiResponse<?> getAllSelect(@RequestParam("status") Status status,
                                       @RequestParam("id") int id
    ) {
        return ApiResponse.<String>builder()
                .message("get all selected")
                .data(materialService.switchStatus(id,status))
                .build();
    }
}
