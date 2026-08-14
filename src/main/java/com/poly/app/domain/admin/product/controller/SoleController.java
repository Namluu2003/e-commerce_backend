package com.poly.app.domain.admin.product.controller;

import com.poly.app.domain.admin.product.response.sole.SoleResponseSelect;
import com.poly.app.domain.common.Meta;
import com.poly.app.domain.model.Sole;
import com.poly.app.domain.admin.product.request.sole.SoleRequest;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.admin.product.response.sole.SoleResponse;
import com.poly.app.domain.admin.product.service.SoleService;
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
@RequestMapping("api/admin/sole")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class SoleController {
    SoleService soleService;

    @PostMapping("/add")
    public ApiResponse<Sole> create(@RequestBody @Valid SoleRequest request) {
        return ApiResponse.<Sole>builder()
                .message("craete sole")
                .data(soleService.createSole(request))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<SoleResponse> update(@RequestBody SoleRequest request, @PathVariable int id) {
        return ApiResponse.<SoleResponse>builder()
                .message("update sole")
                .data(soleService.updateSole(request, id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<SoleResponse>> getAllSole(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "size", defaultValue = "5") int sole
    ) {

        Page<SoleResponse> page1 = soleService.getAllSole(page - 1, sole);
        return ApiResponse.<List<SoleResponse>>builder()
                .message("list sole")
                .data(page1.getContent())
                .meta(Meta.builder()
                        .totalElement(page1.getTotalElements())
                        .currentPage(page1.getNumber() + 1)
                        .totalPages(page1.getTotalPages())
                        .build())
                .build();
    }

    @GetMapping("{id}")
    public ApiResponse<SoleResponse> getSole(@PathVariable int id) {
        return ApiResponse.<SoleResponse>builder()
                .message("get sole by id")
                .data(soleService.getSole(id))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<SoleResponse>> getSole(@RequestParam("name") String name,
                                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "size", defaultValue = "5") int sole) {

        Page<SoleResponse> page1 = soleService.fillbySoleName(page - 1, sole, name);
        return ApiResponse.<List<SoleResponse>>builder()
                .message("get sole by id")
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
                .data(soleService.delete(id))
                .build();
    }

    @GetMapping("/existsbysolename")
    public ApiResponse<Boolean> existsBySoleName(@RequestParam String soleName) {
        return ApiResponse.<Boolean>builder()
                .message("existsBySoleName")
                .data(soleService.existsBySoleName(soleName))
                .build();
    }

    @GetMapping("/existsbysolenameandidnot")
    public ApiResponse<Boolean> existsBySoleNameAndIdNot(@RequestParam String soleName, @RequestParam Integer id) {
        return ApiResponse.<Boolean>builder()
                .message("existsBySoleName")
                .data(soleService.existsBySoleNameAndIdNot(soleName, id))
                .build();
    }
    @GetMapping("/getallselect")
    public ApiResponse<List<SoleResponseSelect>> getAllSelect() {
        return ApiResponse.<List<SoleResponseSelect>>builder()
                .message("get all selected")
                .data(soleService.getAll())
                .build();
    }
    @GetMapping("/getallselecthd")
    public ApiResponse<List<SoleResponseSelect>> getAllSelecthd() {
        return ApiResponse.<List<SoleResponseSelect>>builder()
                .message("get all selected hd")
                .data(soleService.getAllhd())
                .build();
    }
    @GetMapping("/switchstatus")
    public ApiResponse<?> getAllSelect(@RequestParam("status") Status status,
                                       @RequestParam("id") int id
    ) {
        return ApiResponse.<String>builder()
                .message("get all selected")
                .data(soleService.switchStatus(id,status))
                .build();
    }
}
