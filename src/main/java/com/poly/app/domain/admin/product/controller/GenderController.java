package com.poly.app.domain.admin.product.controller;

import com.poly.app.domain.admin.product.response.gender.GenderResponse;
import com.poly.app.domain.admin.product.response.gender.GenderResponseSelect;
import com.poly.app.domain.common.Meta;
import com.poly.app.domain.model.Gender;
import com.poly.app.domain.admin.product.request.gender.GenderRequest;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.admin.product.response.gender.GenderResponse;
import com.poly.app.domain.admin.product.service.GenderService;
import com.poly.app.infrastructure.constant.Status;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/gender")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class GenderController {
    GenderService genderService;

    @PostMapping("/add")
    public ApiResponse<Gender> create(@RequestBody GenderRequest request) {
        return ApiResponse.<Gender>builder()
                .message("create gender")
                .data(genderService.createGender(request))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<GenderResponse> update(@RequestBody GenderRequest request, @PathVariable int id) {
        return ApiResponse.<GenderResponse>builder()
                .message("update gender")
                .data(genderService.updateGender(request,id))
                .build();
    }

 
    @GetMapping("{id}")
    public ApiResponse<GenderResponse> getGender(@PathVariable int id) {
        return ApiResponse.<GenderResponse>builder()
                .message("get gender by id")
                .data(genderService.getGender(id))
                .build();
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<String> delete(@PathVariable int id) {
        return ApiResponse.<String>builder()
                .message("delete gender by id")
                .data(genderService.deleteGender(id))
                .build();
    }
    @GetMapping()
    public ApiResponse<List<GenderResponse>> getAllBrand(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "5") int size
    ) {

        Page<GenderResponse> page1 = genderService.getAllGender(page - 1, size);
        return ApiResponse.<List<GenderResponse>>builder()
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
    public ApiResponse<List<GenderResponse>> getBrand(@RequestParam("name") String name,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "5") int size) {

        Page<GenderResponse> page1 = genderService.fillbyName(page - 1, size, name);


        return ApiResponse.<List<GenderResponse>>builder()
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
    public ApiResponse<List<GenderResponseSelect>> getAllSelect() {
        return ApiResponse.<List<GenderResponseSelect>>builder()
                .message("get all selected")
                .data(genderService.getAll())
                .build();
    }
    @GetMapping("/getallselecthd")
    public ApiResponse<List<GenderResponseSelect>> getAllSelecthd() {
        return ApiResponse.<List<GenderResponseSelect>>builder()
                .message("get all selected hd")
                .data(genderService.getAllhd())
                .build();
    }
    @GetMapping("/switchstatus")
    public ApiResponse<?> getAllSelect(@RequestParam("status") Status status,
                                       @RequestParam("id") int id
    ) {
        return ApiResponse.<String>builder()
                .message("get all selected")
                .data(genderService.switchStatus(id,status))
                .build();
    }
}
