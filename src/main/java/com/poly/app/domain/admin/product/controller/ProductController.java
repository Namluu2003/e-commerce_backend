package com.poly.app.domain.admin.product.controller;

import com.poly.app.domain.admin.product.response.img.ImgResponse;
import com.poly.app.domain.admin.product.response.product.IProductResponse;
import com.poly.app.domain.admin.product.response.product.ProductResponse2;
import com.poly.app.domain.admin.product.response.product.ProductResponseSelect;
import com.poly.app.domain.admin.product.response.productdetail.ProductAtributeExistResponse;
import com.poly.app.domain.admin.product.service.ProductDetailService;
import com.poly.app.domain.admin.product.service.ProductListService;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.domain.common.Meta;
import com.poly.app.domain.common.PageReponse;
import com.poly.app.domain.model.Image;
import com.poly.app.domain.model.Product;
import com.poly.app.domain.admin.product.request.product.ProductRequest;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.admin.product.response.product.ProductResponse;
import com.poly.app.domain.admin.product.service.ProductService;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.domain.repository.ImageRepository;
import com.poly.app.domain.repository.ProductRepository;
import com.poly.app.infrastructure.constant.DiscountType;
import com.poly.app.infrastructure.constant.Status;
import com.poly.app.infrastructure.constant.VoucherType;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class ProductController {
    ProductService productService;
    ProductDetailService productDetailService;
    ProductListService productListService;
    ImageRepository imageRepository;
    ProductRepository productRepository;

    @PostMapping("/add")
    public ApiResponse<Product> create(@RequestBody @Valid ProductRequest request) {
        return ApiResponse.<Product>builder()
                .message("craete product")
                .data(productService.createProduct(request))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ProductResponse> update(@RequestBody ProductRequest request, @PathVariable int id) {
        return ApiResponse.<ProductResponse>builder()
                .message("update product")
                .data(productService.updateProduct(request, id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<ProductResponse2>> getAllProduct(@RequestParam(value = "page", defaultValue = "1") int page,
                                                             @RequestParam(value = "size", defaultValue = "5") int product
    ) {

        Page<IProductResponse> page1 = productService.getAllProduct(page - 1, product);
        Page<ProductResponse2> productPage = page1.map(i -> ProductResponse2.builder()
                .id(i.getId())
                .code(i.getCode())
                .productName(i.getProductName())
                .totalQuantity(i.getTotalQuantity())
                .updateAt(i.getUpdatedAt())
                .status(i.getStatus())
                .image(imageRepository.findByProductId(i.getId())
                        .stream().map(ImgResponse::getUrl) // Nếu có ảnh, lấy URL
                        .findAny().orElse("https://placehold.co/100")) // Nếu không có, trả về ảnh mặc định
                .build()
        );

        return ApiResponse.<List<ProductResponse2>>builder()
                .message("list product")
                .data(productPage.getContent())
                .meta(Meta.builder()
                        .totalElement(page1.getTotalElements())
                        .currentPage(page1.getNumber() + 1)
                        .totalPages(page1.getTotalPages())
                        .build())
                .build();
    }

    @GetMapping("{id}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable int id) {
        return ApiResponse.<ProductResponse>builder()
                .message("get product by id")
                .data(productService.getProduct(id))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductResponse2>> getProduct(@RequestParam("name") String name,
                                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "size", defaultValue = "5") int product) {

        Page<IProductResponse> page1 = productService.fillbyProductName(page - 1, product, name);
        Page<ProductResponse2> productPage = page1.map(i -> ProductResponse2.builder()
                .id(i.getId())
                .code(i.getCode())
                .productName(i.getProductName())
                .totalQuantity(i.getTotalQuantity())
                .updateAt(i.getUpdatedAt())
                .status(i.getStatus())
                .image(imageRepository.findByProductId(i.getId())
                        .stream().map(ImgResponse::getUrl) // Nếu có ảnh, lấy URL
                        .findAny().orElse("https://placehold.co/100")) // Nếu không có, trả về ảnh mặc định
                .build()
        );
        return ApiResponse.<List<ProductResponse2>>builder()
                .message("get product by id")
                .data(productPage.getContent())
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
                .data(productService.delete(id))
                .build();
    }

    @GetMapping("/existsbyproductname")
    public ApiResponse<Boolean> existsByProductName(@RequestParam String productName) {
        return ApiResponse.<Boolean>builder()
                .message("existsByProductName")
                .data(productService.existsByProductName(productName))
                .build();
    }

    @GetMapping("/existsbyproductnameandidnot")
    public ApiResponse<Boolean> existsByProductNameAndIdNot(@RequestParam String productName, @RequestParam Integer id) {
        return ApiResponse.<Boolean>builder()
                .message("existsByProductName")
                .data(productService.existsByProductNameAndIdNot(productName, id))
                .build();
    }

    @GetMapping("/getallselect")
    public ApiResponse<List<ProductResponseSelect>> getAllSelect() {
        return ApiResponse.<List<ProductResponseSelect>>builder()
                .message("get all selected")
                .data(productService.getAll())
                .build();
    }

    @GetMapping("/getallselecthd")
    public ApiResponse<List<ProductResponseSelect>> getAllSelecthd() {
        return ApiResponse.<List<ProductResponseSelect>>builder()
                .message("get all selected hd")
                .data(productService.getAllhd())
                .build();
    }

    @GetMapping("/switchstatus")
    public ApiResponse<?> getAllSelect(@RequestParam("status") Status status,
                                       @RequestParam("id") int id
    ) {
        return ApiResponse.<String>builder()
                .message("get all selected")
                .data(productService.switchStatus(id, status))
                .build();
    }


    //

    @GetMapping("/hien")
    public ApiResponse<List<ProductResponse>> getAllVoucher() {
        return ApiResponse.<List<ProductResponse>>builder()
                .message("list voucher")
                .data(productListService.getAllIn())
                .build();
    }

    @GetMapping("/page")
    public ApiResponse<Page<ProductResponse>> phanTrang(@RequestParam(value = "page") Integer page,
                                                        @RequestParam(value = "size") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<ProductResponse> list = productListService.getAllIn(pageable);
        return ApiResponse.<Page<ProductResponse>>builder()
                .message("list voucher")
                .data(list)
                .build();
    }

    @GetMapping("/searchNameProduct/{productName}")
    public ApiResponse<List<Map<String, Object>>> getAllProductName(@PathVariable String productName) {
        return ApiResponse.<List<Map<String, Object>>>builder()
                .message("Lấy thông tin chi tiết sản phẩm theo tên")
                .data(productListService.searchProduct(productName))
                .build();
    }
    @GetMapping("/searchQuantityProduct/{minQuantity}")
    public ApiResponse<List<Map<String, Object>>> searchProduct(@PathVariable String minQuantity) {
        return ApiResponse.<List<Map<String, Object>>>builder()
                .message("Tìm kiếm sản phẩm theo số lượng")
                .data(productListService.searchProduct(minQuantity))
                .build();
    }
    @GetMapping("/get-attribute-of-product")
    public ApiResponse<ProductAtributeExistResponse> getAttributeOfProduct(@RequestParam(value = "productId") Integer productId) {
        return ApiResponse.<ProductAtributeExistResponse>builder()
                .message("Tìm")
                .data(productDetailService.getAttributeOfProductExist(productId))
                .build();
    }
    //
}
