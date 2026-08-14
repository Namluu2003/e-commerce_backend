package com.poly.app.domain.admin.product.controller;

import com.poly.app.domain.admin.product.response.size.SizeResponseSelect;
import com.poly.app.domain.common.Meta;
import com.poly.app.domain.model.Size;
import com.poly.app.domain.admin.product.request.size.SizeRequest;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.admin.product.response.size.SizeResponse;
import com.poly.app.domain.admin.product.service.SizeService;
import com.poly.app.infrastructure.constant.Status;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/size")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class SizeController {
    SizeService sizeService;

    @PostMapping("/add")
    public ApiResponse<Size> create(@RequestBody @Valid SizeRequest request) {
        return ApiResponse.<Size>builder()
                .message("craete size")
                .data(sizeService.createSize(request))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<SizeResponse> update(@RequestBody SizeRequest request, @PathVariable int id) {
        return ApiResponse.<SizeResponse>builder()
                .message("update size")
                .data(sizeService.updateSize(request, id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<SizeResponse>> getAllSize(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "2") int size
    ) {

        Page<SizeResponse> page1 = sizeService.getAllSize(page - 1, size);
        return ApiResponse.<List<SizeResponse>>builder()
                .message("list size")
                .data(page1.getContent())
                .meta(Meta.builder()
                        .totalElement(page1.getTotalElements())
                        .currentPage(page1.getNumber() + 1)
                        .totalPages(page1.getTotalPages())
                        .build())
                .build();
    }

    @GetMapping("{id}")
    public ApiResponse<SizeResponse> getSize(@PathVariable int id) {
        return ApiResponse.<SizeResponse>builder()
                .message("get size by id")
                .data(sizeService.getSize(id))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<SizeResponse>> getSize(@RequestParam("name") String name,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "5") int size) {

        Page<SizeResponse> page1 = sizeService.fillbySizeName(page - 1, size, name);
        return ApiResponse.<List<SizeResponse>>builder()
                .message("get size by id")
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
                .data(sizeService.delete(id))
                .build();
    }

    @GetMapping("/existsbysizename")
    public ApiResponse<Boolean> existsBySizeName(@RequestParam String sizeName) {
        return ApiResponse.<Boolean>builder()
                .message("existsBySizeName")
                .data(sizeService.existsBySizeName(sizeName))
                .build();
    }

    @GetMapping("/existsbysizenameandidnot")
    public ApiResponse<Boolean> existsBySizeNameAndIdNot(@RequestParam String sizeName, @RequestParam Integer id) {
        return ApiResponse.<Boolean>builder()
                .message("existsBySizeName")
                .data(sizeService.existsBySizeNameAndIdNot(sizeName, id))
                .build();
    }

    @GetMapping("/getallselect")
    public ApiResponse<List<SizeResponseSelect>> getAllSelect() {
        return ApiResponse.<List<SizeResponseSelect>>builder()
                .message("get all selected")
                .data(sizeService.getAll())
                .build();
    }
    @GetMapping("/getallselecthd")
    public ApiResponse<List<SizeResponseSelect>> getAllSelecthd() {
        return ApiResponse.<List<SizeResponseSelect>>builder()
                .message("get alhdl selected hd")
                .data(sizeService.getAllhd())
                .build();
    }
    @GetMapping("/switchstatus")
    public ApiResponse<?> getAllSelect(@RequestParam("status") Status status,
                                       @RequestParam("id") int id
    ) {
        return ApiResponse.<String>builder()
                .message("get all selected")
                .data(sizeService.switchStatus(id,status))
                .build();
    }
}
