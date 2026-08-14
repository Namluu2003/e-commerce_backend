package com.poly.app.domain.admin.product.controller;

import com.poly.app.domain.admin.product.response.product.AllNameProductDetailResponse;
import com.poly.app.domain.admin.product.response.productdetail.FilterProductDetailWithPromotionDTO;
import com.poly.app.domain.common.Meta;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.domain.repository.ProductDetailRepository;
import com.poly.app.domain.repository.ProductDetailRepositoryDTO;
import com.poly.app.domain.admin.product.request.productdetail.FilterRequest;
import com.poly.app.domain.admin.product.request.productdetail.ProductDetailRequest;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.admin.product.response.productdetail.FilterProductDetailResponse;
import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.domain.admin.product.service.ProductDetailService;
import com.poly.app.infrastructure.constant.Status;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/productdetail")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@Slf4j

public class ProductDetailController {
    ProductDetailService productDetailService;

    ProductDetailRepository productDetailRepository;

    ProductDetailRepositoryDTO productDetailRepositoryDTO;

    @PostMapping("/add")
    public ApiResponse<ProductDetail> create(@RequestBody ProductDetailRequest request) {
        return ApiResponse.<ProductDetail>builder()
                .message("create productDetail")
                .data(productDetailService.createProductDetail(request))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ProductDetailResponse> update(@RequestBody ProductDetailRequest request, @PathVariable int id) {
        return ApiResponse.<ProductDetailResponse>builder()
                .message("update productdetail")
                .data(productDetailService.updateProductDetail(request, id))
                .build();
    }

    @PostMapping("/existsproductdetail")
    public ApiResponse<Boolean> exists(@RequestBody ProductDetailRequest request) {
        return ApiResponse.<Boolean>builder()
                .message("exitst productdetail")
                .data(productDetailService.existsProductDetail(request))
                .build();
    }


    //    teest
//    @GetMapping("/test")
//    public List<ProductDetail> getAllType() {
//        return productDetailService.getAllProductDetail();
//    }

    @GetMapping()
    public ApiResponse<List<ProductDetailResponse>> getAllProductPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", defaultValue = "1") int size) {

        Page<ProductDetailResponse> page1 = productDetailService.getAllProductDetailPage(page - 1, size);
        return ApiResponse.<List<ProductDetailResponse>>builder()
                .message("list product detail page")
                .data(page1.getContent())
                .meta(Meta.builder()
                        .currentPage(page1.getNumber())
                        .totalPages(page1.getTotalPages())
                        .totalElement(page1.getTotalElements())
                        .build())
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductDetailResponse>> findName(@RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "1") int size,
                                                             @RequestParam(value = "name", defaultValue = "") String productDetailName) {

        Page<ProductDetailResponse> page1 = productDetailService.findByName(page - 1, size, productDetailName);

        return ApiResponse.<List<ProductDetailResponse>>builder()
                .message("find product detail page")
                .data(page1.getContent())
                .meta(Meta.builder()
                        .currentPage(page1.getNumber())
                        .totalPages(page1.getTotalPages())
                        .totalElement(page1.getTotalElements())
                        .build())
                .build();
    }

    @PostMapping("/filter")
    public ApiResponse<List<FilterProductDetailResponse>> getFilterPD(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                      @RequestParam(value = "size", defaultValue = "5") int size,
                                                                      @RequestBody FilterRequest request
    ) {

        Integer totalElement = productDetailService.getFillterElement(request);


        return ApiResponse.<List<FilterProductDetailResponse>>builder()
                .message("fillter")
                .data(productDetailService.filterProductDetail(page, size, request))
                .meta(Meta.builder()
                        .currentPage(page)
                        .totalPages((int) Math.ceil(Double.valueOf(totalElement) / size))
                        .totalElement((long) totalElement)
                        .build())
                .build();

    }


    @PostMapping("/filter-with-promotion")
    public ApiResponse<List<FilterProductDetailWithPromotionDTO>> getFilterWithPromotionPD(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                                           @RequestParam(value = "size", defaultValue = "5") int size,
                                                                                           @RequestBody FilterRequest request
    ) {
        Integer totalElement = productDetailService.getFillterElement(request);
        return ApiResponse.<List<FilterProductDetailWithPromotionDTO>>builder()
                .message("fillter")
                .data(productDetailService.filterDetailProductWithPromotion(page, size, request))
                .meta(Meta.builder()
                        .currentPage(page)
                        .totalPages((int) Math.ceil(Double.valueOf(totalElement) / size))
                        .totalElement((long) totalElement)
                        .build())
                .build();

    }


//    @GetMapping("/test")
//    public ApiResponse<List<ProductDetailRepositoryDTO>> test(@RequestParam(value = "page", defaultValue = "1") int page,
//                               @RequestParam(value = "size", defaultValue = "1") int size,
//                               @RequestBody FilterRequest request) {
//
//
//
//        List<ProductDetailRepositoryDTO> list = productDetailRepositoryDTO.getFilterProductDetail(request.getProductName(),
//                request.getBrandName(),
//                request.getTypeName(),
//                request.getColorName(),
//                request.getMaterialName(),
//                request.getSizeName(), request.getSoleName(), request.getGenderName(), request.getStatus(), request.getSortByQuantity(), request.getSortByPrice(), page,size);
//        return ApiResponse.<List<ProductDetailRepositoryDTO>>builder()
//                .data(list)
//                .build();
//    }

    @GetMapping("{id}")
    public ApiResponse<ProductDetailResponse> getProductDetail(@PathVariable int id) {
        return ApiResponse.<ProductDetailResponse>builder()
                .message("get product detail by id")
                .data(productDetailService.getProductDetail(id))
                .build();
    }
//detail theo ten

    @GetMapping("/name/{productName}")
    public ApiResponse<List<ProductDetailResponse>> getAllProductDetailName(@PathVariable String productName) {
        return ApiResponse.<List<ProductDetailResponse>>builder()
                .message("Lấy thông tin chi tiết sản phẩm theo tên")
                .data(productDetailService.getAllProductDetailName(productName))
                .build();
    }


    //

    @DeleteMapping("{id}")
    public ApiResponse<String> delete(@PathVariable int id) {
        return ApiResponse.<String>builder()
                .message("delete product detail by id")
                .data(productDetailService.deleteProductDetail(id))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<List<ProductDetailResponse>> createProductDetailList(@RequestBody List<ProductDetailRequest> requests) {
        return ApiResponse.<List<ProductDetailResponse>>builder()
                .message("add")
                .data(productDetailService.createProductDetailList(requests))
                .build();
    }

    @GetMapping("/exportdata")
    public ApiResponse<List<ProductDetailResponse>> getAllProductDetailExportData() {
        return ApiResponse.<List<ProductDetailResponse>>builder()
                .message("export data to excel")
                .data(productDetailService.getAllProductDetailExportData())
                .build();
    }

    @GetMapping("/full")
    public ApiResponse<List<ProductDetailResponse>> getAllSelectDetail() {
        return ApiResponse.<List<ProductDetailResponse>>builder()
                .message("get all selected")
                .data(productDetailRepository.getAllProductDetail())
                .build();
    }

    @GetMapping("/switchstatus")
    public ApiResponse<?> getAllSelect(@RequestParam("status") Status status,
                                       @RequestParam("id") int id
    ) {
        return ApiResponse.<String>builder()
                .message("get all selected")
                .data(productDetailService.switchStatus(id, status))
                .build();
    }

    //em tú làm
    @GetMapping("/product/{productId}")
    public ApiResponse<List<ProductDetailResponse>> getProductDetailsByProductId(@PathVariable Integer productId) {
        return ApiResponse.<List<ProductDetailResponse>>builder()
                .message("Lấy danh sách ProductDetail theo productId")
                .data(productDetailService.getProductDetailsByProductId(productId)) // Trả về List<ProductDetailResponse>
                .build();
    }
    @GetMapping("/allName")
    public ApiResponse<AllNameProductDetailResponse> getAllFilters() {
        return ApiResponse.<AllNameProductDetailResponse>builder()
                .message("Filter options fetched successfully")
                .data(productDetailService.getAllFilterOptions())
                .build();
    }


    //em tú hết làm

}
