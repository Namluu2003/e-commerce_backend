package com.poly.app.domain.admin.product.controller;

import com.poly.app.domain.admin.product.response.sole.SoleResponseSelect;
import com.poly.app.domain.admin.product.response.type.TypeResponseSelect;
import com.poly.app.domain.common.Meta;
import com.poly.app.domain.model.Type;
import com.poly.app.domain.admin.product.request.type.TypeRequest;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.admin.product.response.type.TypeResponse;
import com.poly.app.domain.admin.product.service.TypeService;
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
@RequestMapping("api/admin/type")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class TypeController {
    TypeService typeService;

    @PostMapping("/add")
    public ApiResponse<Type> create(@RequestBody @Valid TypeRequest request) {
        return ApiResponse.<Type>builder()
                .message("craete type")
                .data(typeService.createType(request))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<TypeResponse> update(@RequestBody TypeRequest request, @PathVariable int id) {
        return ApiResponse.<TypeResponse>builder()
                .message("update type")
                .data(typeService.updateType(request, id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<TypeResponse>> getAllType(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "size", defaultValue = "5") int type
    ) {

        Page<TypeResponse> page1 = typeService.getAllType(page - 1, type);
        return ApiResponse.<List<TypeResponse>>builder()
                .message("list type")
                .data(page1.getContent())
                .meta(Meta.builder()
                        .totalElement(page1.getTotalElements())
                        .currentPage(page1.getNumber() + 1)
                        .totalPages(page1.getTotalPages())
                        .build())
                .build();
    }

    @GetMapping("{id}")
    public ApiResponse<TypeResponse> getType(@PathVariable int id) {
        return ApiResponse.<TypeResponse>builder()
                .message("get type by id")
                .data(typeService.getType(id))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<TypeResponse>> getType(@RequestParam("name") String name,
                                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "size", defaultValue = "5") int type) {

        Page<TypeResponse> page1 = typeService.fillbyTypeName(page - 1, type, name);
        return ApiResponse.<List<TypeResponse>>builder()
                .message("get type by id")
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
                .data(typeService.delete(id))
                .build();
    }

    @GetMapping("/existsbytypename")
    public ApiResponse<Boolean> existsByTypeName(@RequestParam String typeName) {
        return ApiResponse.<Boolean>builder()
                .message("existsByTypeName")
                .data(typeService.existsByTypeName(typeName))
                .build();
    }

    @GetMapping("/existsbytypenameandidnot")
    public ApiResponse<Boolean> existsByTypeNameAndIdNot(@RequestParam String typeName, @RequestParam Integer id) {
        return ApiResponse.<Boolean>builder()
                .message("existsByTypeName")
                .data(typeService.existsByTypeNameAndIdNot(typeName, id))
                .build();
    }
    @GetMapping("/getallselect")
    public ApiResponse<List<TypeResponseSelect>> getAllSelect() {
        return ApiResponse.<List<TypeResponseSelect>>builder()
                .message("get all selected")
                .data(typeService.getAll())
                .build();
    }
    @GetMapping("/getallselecthd")
    public ApiResponse<List<TypeResponseSelect>> getAllSelecthd() {
        return ApiResponse.<List<TypeResponseSelect>>builder()
                .message("get all selected hd")
                .data(typeService.getAllhd())
                .build();
    }
    @GetMapping("/switchstatus")
    public ApiResponse<?> getAllSelect(@RequestParam("status") Status status,
                                       @RequestParam("id") int id
    ) {
        return ApiResponse.<String>builder()
                .message("get all selected")
                .data(typeService.switchStatus(id,status))
                .build();
    }
}
