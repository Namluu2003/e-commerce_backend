package com.poly.app.domain.admin.product.controller;

import com.poly.app.domain.admin.product.response.brand.BrandResponse;
import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.admin.product.response.color.ColorResponseSelect;
import com.poly.app.domain.common.Meta;
import com.poly.app.domain.model.Color;
import com.poly.app.domain.admin.product.request.color.ColorRequest;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.domain.admin.product.service.ColorService;
import com.poly.app.infrastructure.constant.Status;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/color")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class ColorController {
    ColorService colorService;

    @PostMapping("/add")
    public ApiResponse<Color> create(@RequestBody ColorRequest request) {
        return ApiResponse.<Color>builder()
                .message("create color")
                .data(colorService.createColor(request))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ColorResponse> update(@RequestBody ColorRequest request, @PathVariable int id) {
        return ApiResponse.<ColorResponse>builder()
                .message("update color")
                .data(colorService.updateColor(request, id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<ColorResponse>> getAllBrand(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "5") int size
    ) {

        Page<ColorResponse> page1 = colorService.getAllColor(page - 1, size);
        return ApiResponse.<List<ColorResponse>>builder()
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
    public ApiResponse<List<ColorResponse>> getBrand(@RequestParam("name") String name,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "5") int size) {

        Page<ColorResponse> page1 = colorService.fillbyName(page - 1, size, name);
        return ApiResponse.<List<ColorResponse>>builder()
                .message("get brand by id")
                .data(page1.getContent())
                .meta(Meta.builder()
                        .totalElement(page1.getTotalElements())
                        .currentPage(page1.getNumber() + 1)
                        .totalPages(page1.getTotalPages())
                        .build())
                .build();
    }

    @GetMapping("{id}")
    public ApiResponse<ColorResponse> getColor(@PathVariable int id) {
        return ApiResponse.<ColorResponse>builder()
                .message("get color by id")
                .data(colorService.getColor(id))
                .build();
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<String> delete(@PathVariable int id) {
        return ApiResponse.<String>builder()
                .message("delete color by id")
                .data(colorService.deleteColor(id))
                .build();
    }
    @GetMapping("/getallselect")
    public ApiResponse<List<ColorResponseSelect>> getAllSelect() {
        return ApiResponse.<List<ColorResponseSelect>>builder()
                .message("get all selected")
                .data(colorService.getAll())
                .build();
    }
    @GetMapping("/getallselecthd")
    public ApiResponse<List<ColorResponseSelect>> getAllSelecthd() {
        return ApiResponse.<List<ColorResponseSelect>>builder()
                .message("get all selected hd")
                .data(colorService.getAllHD())
                .build();
    }
    @GetMapping("/switchstatus")
    public ApiResponse<?> getAllSelect(@RequestParam("status") Status status,
                                       @RequestParam("id") int id
    ) {
        return ApiResponse.<String>builder()
                .message("get all selected")
                .data(colorService.switchStatus(id,status))
                .build();
    }
}
