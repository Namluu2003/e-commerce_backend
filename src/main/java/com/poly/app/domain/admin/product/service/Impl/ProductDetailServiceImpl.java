package com.poly.app.domain.admin.product.service.Impl;

import com.poly.app.domain.admin.bill.service.WebSocketService;
import com.poly.app.domain.admin.product.request.img.ImgRequest;
import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.domain.admin.product.response.img.ImgResponse;
import com.poly.app.domain.admin.product.response.product.AllNameProductDetailResponse;
import com.poly.app.domain.admin.product.response.productdetail.FilterProductDetailWithPromotionDTO;
import com.poly.app.domain.admin.product.response.productdetail.ProductAtributeExistResponse;
import com.poly.app.domain.admin.product.service.CloundinaryService;
import com.poly.app.domain.client.repository.ProductViewRepository;
import com.poly.app.domain.client.response.PromotionResponse;
import com.poly.app.domain.model.*;
import com.poly.app.domain.repository.*;
import com.poly.app.domain.admin.product.request.productdetail.FilterRequest;
import com.poly.app.domain.admin.product.request.productdetail.ProductDetailRequest;
import com.poly.app.domain.admin.product.response.productdetail.FilterProductDetailResponse;
import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.domain.admin.product.service.ProductDetailService;
import com.poly.app.infrastructure.constant.Status;
import com.poly.app.infrastructure.exception.ApiError;
import com.poly.app.infrastructure.exception.ApiException;
import com.poly.app.infrastructure.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ProductDetailServiceImpl implements ProductDetailService {

    ProductDetailRepository productDetailRepository;

    ProductRepository productRepository;

    BrandRepository brandRepository;

    TypeRepository typeRepository;

    ColorRepository colorRepository;

    MaterialRepository materialRepository;

    SizeRepository sizeRepository;

    SoleRepository soleRepository;

    GenderRepository genderRepository;

    ImageRepository imageRepository;

    CloundinaryService cloundinaryService;

    WebSocketService webSocketService;

    ProductViewRepository productViewRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Override
    public ProductDetail createProductDetail(ProductDetailRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Type type = typeRepository.findById(request.getTypeId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Color color = colorRepository.findById(request.getColorId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Material material = materialRepository.findById(request.getMaterialId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Size size = sizeRepository.findById(request.getSizeId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Sole sole = soleRepository.findById(request.getSoleId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Gender gender = genderRepository.findById(request.getGenderId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));

        ProductDetail productDetail = ProductDetail.builder()
                .product(product)
                .brand(brand)
                .type(type)
                .color(color)
                .material(material)
                .size(size)
                .sole(sole)
                .gender(gender)
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .weight(request.getWeight())
                .descrition(request.getDescription())
                .status(request.getStatus())
                .build();
        return productDetailRepository.save(productDetail);
    }

    //    @Override
//    public ProductDetailResponse updateProductDetail(ProductDetailRequest request, int id) {
//        ProductDetail productDetail = productDetailRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko ton tai"));
//
//        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
//        Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
//        Type type = typeRepository.findById(request.getTypeId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
//        Color color = colorRepository.findById(request.getColorId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
//        Material material = materialRepository.findById(request.getMaterialId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
//        Size size = sizeRepository.findById(request.getSizeId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
//        Sole sole = soleRepository.findById(request.getSoleId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
//        Gender gender = genderRepository.findById(request.getGenderId()).orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
//
////        thêm url
//        List<ImgResponse> imgResponses = imageRepository.findByProductDetailId(productDetail.getId());
//        for (ImgRequest req : request.getImage()) {
//            boolean found = false; // Cờ kiểm tra xem publicId đã tồn tại chưa
//
//            for (ImgResponse resdb : imgResponses) {
//                if (req.getPublicId().equals(resdb.getPublicId())) {
//                    found = true; // Nếu tìm thấy publicId, đặt cờ thành true
//                    break; // Dừng vòng lặp khi tìm thấy
//                }
//            }
//
//            // Nếu publicId không tồn tại, thêm mới vào cơ sở dữ liệu
//            if (!found) {
////                set lại ảnh cho toàn bộ ảnh cho màu đó
//
//
//
//                imageRepository.save(Image.builder()
//                        .publicId(req.getPublicId())
//                        .url(req.getUrl())
//                        .status(Status.HOAT_DONG)
//                        .productDetail(productDetail)
//                        .build());
//            }
//        }
//
//        productDetail.setProduct(product);
//        productDetail.setBrand(brand);
//        productDetail.setType(type);
//        productDetail.setColor(color);
//        productDetail.setMaterial(material);
//        productDetail.setSize(size);
//        productDetail.setSole(sole);
//        productDetail.setGender(gender);
//        productDetail.setQuantity(request.getQuantity());
//        productDetail.setPrice(request.getPrice());
//        productDetail.setWeight(request.getWeight());
//        productDetail.setDescrition(request.getDescription());
//        productDetail.setStatus(request.getStatus());
//
//        productDetailRepository.save(productDetail);
//        return ProductDetailResponse.builder()
//                .id(productDetail.getId())
//                .code(productDetail.getCode())
//                .productName(productDetail.getProduct().getProductName())
//                .brandName(productDetail.getBrand().getBrandName())
//                .typeName(productDetail.getType().getTypeName())
//                .colorName(productDetail.getColor().getColorName())
//                .materialName(productDetail.getMaterial().getMaterialName())
//                .sizeName(productDetail.getSize().getSizeName())
//                .soleName(productDetail.getSole().getSoleName())
//                .genderName(productDetail.getGender().getGenderName())
//                .quantity(productDetail.getQuantity())
//                .price(productDetail.getPrice())
//                .weight(productDetail.getWeight())
//                .description(productDetail.getDescrition())
//                .status(productDetail.getStatus())
//                .updateAt(productDetail.getUpdatedAt())
//                .updateBy(productDetail.getUpdatedBy())
//                .build();
//    }
    @Transactional
    @Override
    public ProductDetailResponse updateProductDetail(ProductDetailRequest request, int id) {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id ko ton tai"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Type type = typeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Color color = colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Size size = sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Sole sole = soleRepository.findById(request.getSoleId())
                .orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));
        Gender gender = genderRepository.findById(request.getGenderId())
                .orElseThrow(() -> new IllegalArgumentException("id khong ton tai"));

//        nếu có một trường nào thay đổi cần kiểm tra để tránh trùng ctbanr ghi
        log.warn(request.getColorId().toString());
        log.warn(productDetail.getColor().getId().toString());
        if (productDetail.getProduct().getId() != request.getProductId()
            || productDetail.getColor().getId() != request.getColorId()
            || productDetail.getBrand().getId() != request.getBrandId()
            || productDetail.getGender().getId() != request.getGenderId()
            || productDetail.getMaterial().getId() != request.getMaterialId()
            || productDetail.getType().getId() != request.getTypeId()
            || productDetail.getSize().getId() != request.getSizeId()
            || productDetail.getSole().getId() != request.getSoleId()
        ) {
            ProductDetail existingProductDetail = productDetailRepository.findByProductIdAndSizeIdAndColorId(
                    request.getProductId(),
                    request.getSizeId(),
                    request.getColorId(),
                    request.getBrandId(),
                    request.getGenderId(),
                    request.getMaterialId(),
                    request.getTypeId(),
                    request.getSoleId()
            );
            if (existingProductDetail != null) {
                throw new ApiException(ErrorCode.PRODUCT_DETAIL_EXISTS);
            }
        }

        //  Cập nhật thông tin ProductDetail
        productDetail.setProduct(product);
        productDetail.setBrand(brand);
        productDetail.setType(type);
        productDetail.setColor(color);
        productDetail.setMaterial(material);
        productDetail.setSize(size);
        productDetail.setSole(sole);
        productDetail.setGender(gender);
        productDetail.setQuantity(request.getQuantity());
        productDetail.setPrice(request.getPrice());
        productDetail.setWeight(request.getWeight());
        productDetail.setDescrition(request.getDescription());
        productDetail.setStatus(request.getStatus());

        productDetailRepository.save(productDetail);


        //  Tìm danh sách tất cả ProductDetail có cùng productId và colorId
        List<ProductDetail> relatedProductDetails = productDetailRepository
                .findByProductIdAndColorId(request.getProductId(), request.getColorId());

        log.warn("đay la ds theo product id va color");
        relatedProductDetails.toString();


//          Xóa toàn bộ ảnh cũ của các ProductDetail liên quan
        for (ProductDetail pd : relatedProductDetails) {
            imageRepository.deleteByProductDetailId(pd.getId());
        }

        //  Thêm ảnh mới vào tất cả ProductDetail cùng productId & colorId
        for (ProductDetail pd : relatedProductDetails) {
            for (ImgRequest req : request.getImage()) {
                imageRepository.save(Image.builder()
                        .publicId(req.getPublicId())
                        .url(req.getUrl())
                        .status(Status.HOAT_DONG)
                        .productDetail(pd)
                        .build());
            }
        }
        String message = "Sản phẩm ID: " + productDetail.getId() + " cập nhật số lượng: " + productDetail.getPrice();
        // 🏷 Trả về response


        ProductDetailResponse productDetailResponse = ProductDetailResponse.builder()
                .id(productDetail.getId())
                .code(productDetail.getCode())
                .productName(productDetail.getProduct().getProductName())
                .brandName(productDetail.getBrand().getBrandName())
                .typeName(productDetail.getType().getTypeName())
                .colorName(productDetail.getColor().getColorName())
                .materialName(productDetail.getMaterial().getMaterialName())
                .sizeName(productDetail.getSize().getSizeName())
                .soleName(productDetail.getSole().getSoleName())
                .genderName(productDetail.getGender().getGenderName())
                .quantity(productDetail.getQuantity())
                .price(productDetail.getPrice())
                .weight(productDetail.getWeight())
                .description(productDetail.getDescrition())
                .status(productDetail.getStatus())
                .updateAt(productDetail.getUpdatedAt())
                .updateBy(productDetail.getUpdatedBy())
                .build();
        List<com.poly.app.domain.client.response.PromotionResponse> promotionResponses = productViewRepository.findPromotionByProductDetailId(productDetail.getId(), ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
        Optional<com.poly.app.domain.client.response.PromotionResponse> maxPromotion = promotionResponses.stream().max(Comparator.comparing(PromotionResponse::getDiscountValue));
        productDetailResponse.setPromotionResponse(maxPromotion.orElse(null));
        List<ImgResponse> imgResponseList = imageRepository.findByProductDetailId(productDetail.getId());
        productDetailResponse.setImage(imgResponseList);
        webSocketService.sendProductUpdate(productDetailResponse);
        return productDetailResponse;
    }


//    @Override
//    public ProductDetailResponse updateProductDetail(ProductDetailRequest request, int id) {
//        ProductDetail productDetail = productDetailRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id ko tồn tại"));
//
//        productDetail.setProductDetailName(request.getProductDetailName());
//        productDetail.setStatus(request.getStatus());
//
//        productDetailRepository.save(productDetail);
//
//        return ProductDetailResponse.builder()
//                .code(productDetail.getCode())
//                .id(productDetail.getId())
//                .productDetailName(productDetail.getProductDetailName())
//                .updateAt(productDetail.getCreatedAt())
//                .status(productDetail.getStatus())
//                .build();
//    }

    @Override
    public List<ProductDetailResponse> getAllProductDetail() {


        List<ProductDetailResponse> productDetails = productDetailRepository.getAllProductDetail();
        productDetails.forEach(pd -> {
            List<ImgResponse> images = imageRepository.findByProductDetailId(pd.getId());
            pd.setImage(images);
        });
        return productDetails;
    }


    @Override
    public String deleteProductDetail(int id) {
        if (!productDetailRepository.findById(id).isEmpty()) {
            productDetailRepository.deleteById(id);
            return "xóa thành công";
        } else {
            return "id ko tồn tại";
        }


    }

    @Override
    public ProductDetailResponse getProductDetail(int id) {
        ProductDetail productDetail = productDetailRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko ton tai"));
        List<ImgResponse> images = imageRepository.findByProductDetailId(productDetail.getId());

        List<PromotionResponse> promotionResponses = productViewRepository.findPromotionByProductDetailId(productDetail.getId(),ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
        Optional<PromotionResponse> maxPromotion = promotionResponses.stream().max(Comparator.comparing(PromotionResponse::getDiscountValue));

        return ProductDetailResponse.builder()
                .id(productDetail.getId())
                .code(productDetail.getCode())
                .productName(productDetail.getProduct().getProductName())
                .brandName(productDetail.getBrand().getBrandName())
                .typeName(productDetail.getType().getTypeName())
                .colorName(productDetail.getColor().getColorName())
                .materialName(productDetail.getMaterial().getMaterialName())
                .sizeName(productDetail.getSize().getSizeName())
                .soleName(productDetail.getSole().getSoleName())
                .genderName(productDetail.getGender().getGenderName())
                .quantity(productDetail.getQuantity())
                .price(productDetail.getPrice())
                .weight(productDetail.getWeight())
                .description(productDetail.getDescrition())
                .status(productDetail.getStatus())
                .updateAt(productDetail.getUpdatedAt())
                .updateBy(productDetail.getUpdatedBy())
                .promotionResponse(maxPromotion.orElse(null))

                .image(images)
                .build();
    }

    //detail theo tên
    @Override
    public List<ProductDetailResponse> getAllProductDetailName(String productName) {
        List<ProductDetailResponse> productDetails = productDetailRepository.getAllProductDetailByProductName(productName);
        if (productDetails.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm nào với tên: " + productName);
        }

        productDetails.forEach(detail -> {
            List<ImgResponse> images = imageRepository.findByProductDetailId(detail.getId());
            detail.setImage(images);
        });

        return productDetails;
    }


    //
//
//    @Override
//    public ProductDetailResponse getProductDetail(int id) {
//        ProductDetail productDetail = productDetailRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id ko tồn tại"));
//
//        return ProductDetailResponse.builder()
//                .code(productDetail.getCode())
//                .id(productDetail.getId())
//                .productDetailName(productDetail.getProductDetailName())
//                .updateAt(productDetail.getUpdatedAt())
//                .status(productDetail.getStatus())
//                .build();
//    }

    @Override
    public Page<ProductDetailResponse> getAllProductDetailPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDetailResponse> productDetails = productDetailRepository.getAllProductDetailPage(pageable);

        // Gán danh sách ảnh cho từng sản phẩm
        productDetails.forEach(pd -> {
            List<ImgResponse> images = imageRepository.findByProductDetailId(pd.getId());
            pd.setImage(images);
        });

        return productDetails;
    }



    @Override
    public Page<ProductDetailResponse> findByName(int page, int size, String productName) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDetailResponse> productDetails = productDetailRepository.findByName(
                String.format("%%%s%%", productName), pageable);

        // Duyệt qua từng ProductDetailResponse trong Page
        productDetails.forEach(pd -> {
            // Gán danh sách ảnh
            List<ImgResponse> images = imageRepository.findByProductDetailId(pd.getId());
            pd.setImage(images);

            // Tìm promotion có discount cao nhất
            List<PromotionResponse> promotionResponses = productViewRepository.findPromotionByProductDetailId(pd.getId(),ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
            PromotionResponse maxPromotion = promotionResponses.stream()
                    .max(Comparator.comparing(PromotionResponse::getDiscountValue))
                    .orElse(null);
            pd.setPromotionResponse(maxPromotion); // Nhớ thêm setPromotion trong ProductDetailResponse
        });

        log.info(productDetails.toString());
        return productDetails;
    }


    @Override
    public List<ProductDetailResponse> getAllProductDetailExportData() {
        return productDetailRepository.getAllProductDetail();
    }

    @Override
    public List<FilterProductDetailResponse> filterProductDetail(int page, int size, FilterRequest request) {

        log.info(request.toString());

        List<FilterProductDetailResponse> list = productDetailRepository.getFilterProductDetail(
                request.getProductName(),
                request.getBrandName(),
                request.getTypeName(),
                request.getColorName(),
                request.getMaterialName(),
                request.getSizeName(),
                request.getSoleName(),
                request.getGenderName(),
                request.getStatus(),
                request.getSortByQuantity(),
                request.getSortByPrice(),
                (page - 1) * size, size);

//        List<FilterProductDetailResponse> list = productDetailRepository.getFilterProductDetail(
//                null,
//                "nike",
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                "HOAT_DONG",
//                null,
//                null,
//                1,
//                1
//                );

        return list;
    }

    @Override
    public List<FilterProductDetailWithPromotionDTO>  filterDetailProductWithPromotion(int page, int size, FilterRequest request) {

        List<FilterProductDetailResponse> list = productDetailRepository.getFilterProductDetail(
                request.getProductName(),
                request.getBrandName(),
                request.getTypeName(),
                request.getColorName(),
                request.getMaterialName(),
                request.getSizeName(),
                request.getSoleName(),
                request.getGenderName(),
                request.getStatus(),
                request.getSortByQuantity(),
                request.getSortByPrice(),
                (page - 1) * size, size);



        List<FilterProductDetailWithPromotionDTO> filterProductDetailWithPromotionDTOS =
                list.stream().map(filterProductDetailResponse -> {

                            List<com.poly.app.domain.client.response.PromotionResponse> promotionResponses = productViewRepository.findPromotionByProductDetailId(filterProductDetailResponse.getId(),ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
                            com.poly.app.domain.client.response.PromotionResponse maxPromotion = promotionResponses.stream().max(Comparator.comparing(com.poly.app.domain.client.response.PromotionResponse::getDiscountValue))
                                    .orElse(null);
                            List<Image> images = imageRepository.findByProductDetail_Id(filterProductDetailResponse.getId());
                            return     FilterProductDetailWithPromotionDTO.fromEntity(filterProductDetailResponse, maxPromotion, images);
                        }

                    ).collect(Collectors.toList());

        return filterProductDetailWithPromotionDTOS;
    }

    @Override
    public Integer getFillterElement(FilterRequest request) {
        Integer totalElement = productDetailRepository.getInforpage(
                request.getProductName(),
                request.getBrandName(),
                request.getTypeName(),
                request.getColorName(),
                request.getMaterialName(),
                request.getSizeName(),
                request.getSoleName(),
                request.getGenderName(),
                request.getStatus()

        );
        log.info("tổng element" + Integer.toString(totalElement));
        return totalElement;
    }

    @Override
    public Page<ColorResponse> fillbyName(int page, int size, String name) {
        return null;
    }

    @Override
    public boolean existsByColorName(String brandName) {
        return false;
    }

    @Override
    public boolean existsByColorNameAndIdNot(String brandName, Integer id) {
        return false;
    }

    @Override
    public List<ProductDetailResponse> createProductDetailList(List<ProductDetailRequest> requests) {
        // Tạo một danh sách để lưu các đối tượng ProductDetailResponse
        List<ProductDetailResponse> productDetailResponses = new ArrayList<>();

        // Lặp qua tất cả các request trong danh sách
        for (ProductDetailRequest request : requests) {
            // Kiểm tra xem sản phẩm đã tồn tại trong cơ sở dữ liệu với productId, sizeId, và colorId
            ProductDetail existingProductDetail = productDetailRepository.findByProductIdAndSizeIdAndColorId(
                    request.getProductId(),
                    request.getSizeId(),
                    request.getColorId(),
                    request.getBrandId(),
                    request.getGenderId(),
                    request.getMaterialId(),
                    request.getTypeId(),
                    request.getSoleId()
            );

            if (existingProductDetail != null) {

                System.out.println("ở đoạn trùng---------------------------------------------------");
                // Nếu bản ghi đã tồn tại, cập nhật bản ghi hiện tại
                existingProductDetail.setQuantity(request.getQuantity() + existingProductDetail.getQuantity());
                existingProductDetail.setPrice(request.getPrice());
                existingProductDetail.setWeight(request.getWeight());
                existingProductDetail.setDescrition(request.getDescription());
                existingProductDetail.setStatus(Status.HOAT_DONG);

                // Lưu lại bản ghi đã được cập nhật

//                xóa ảnh
                List<ImgResponse> images = imageRepository.findByProductDetailId(existingProductDetail.getId());

                for (ImgResponse res : images
                ) {
                    try {
                        //                    xóa khỏi cloud
                        cloundinaryService.deleteImage(res.getPublicId());
//                    xóa khỏi bảng img
                        imageRepository.deleteById(res.getId());

                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new IllegalArgumentException("lỗi xóa ảnh");
                    }
                }
//                thêm lại ảnh vào bảng
                for (ImgRequest i : request.getImage()
                ) {
                    Image image = Image.builder()
                            .productDetail(existingProductDetail)
                            .publicId(i.getPublicId())
                            .url(i.getUrl())
                            .status(Status.HOAT_DONG)
                            .build();
                    imageRepository.save(image);

                }


                existingProductDetail = productDetailRepository.save(existingProductDetail);

                // Chuyển đổi ProductDetail thành ProductDetailResponse và thêm vào danh sách
                ProductDetailResponse response = ProductDetailResponse.builder()
                        .id(existingProductDetail.getId())
                        .productName(existingProductDetail.getProduct().getProductName())
                        .brandName(existingProductDetail.getBrand().getBrandName())
                        .typeName(existingProductDetail.getType().getTypeName())
                        .colorName(existingProductDetail.getColor().getColorName())
                        .materialName(existingProductDetail.getMaterial().getMaterialName())
                        .sizeName(existingProductDetail.getSize().getSizeName())
                        .soleName(existingProductDetail.getSole().getSoleName())
                        .genderName(existingProductDetail.getGender().getGenderName())
                        .quantity(existingProductDetail.getQuantity())
                        .price(existingProductDetail.getPrice())
                        .weight(existingProductDetail.getWeight())
                        .description(existingProductDetail.getDescrition())
                        .status(existingProductDetail.getStatus())
                        .updateAt(existingProductDetail.getUpdatedAt()) // Giả sử có trường updatedAt trong entity
                        .updateBy(existingProductDetail.getUpdatedBy()) // Giả sử có trường updatedBy trong entity
                        .build();

                productDetailResponses.add(response);
            } else {
                System.out.println("ở đoạn ko trùng---------------------------------------------------");

                // Nếu không tồn tại bản ghi, tạo mới bản ghi ProductDetail
                Product product = productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Product id không tồn tại"));
                Brand brand = brandRepository.findById(request.getBrandId())
                        .orElseThrow(() -> new IllegalArgumentException("Brand id không tồn tại"));
                Type type = typeRepository.findById(request.getTypeId())
                        .orElseThrow(() -> new IllegalArgumentException("Type id không tồn tại"));
                Color color = colorRepository.findById(request.getColorId())
                        .orElseThrow(() -> new IllegalArgumentException("Color id không tồn tại"));
                Material material = materialRepository.findById(request.getMaterialId())
                        .orElseThrow(() -> new IllegalArgumentException("Material id không tồn tại"));
                Size size = sizeRepository.findById(request.getSizeId())
                        .orElseThrow(() -> new IllegalArgumentException("Size id không tồn tại"));
                Sole sole = soleRepository.findById(request.getSoleId())
                        .orElseThrow(() -> new IllegalArgumentException("Sole id không tồn tại"));
                Gender gender = genderRepository.findById(request.getGenderId())
                        .orElseThrow(() -> new IllegalArgumentException("Gender id không tồn tại"));

                // Tạo đối tượng ProductDetail
                ProductDetail productDetail = ProductDetail.builder()
                        .product(product)
                        .brand(brand)
                        .type(type)
                        .color(color)
                        .material(material)
                        .size(size)
                        .sole(sole)
                        .gender(gender)
                        .quantity(request.getQuantity())
                        .price(request.getPrice())
                        .weight(request.getWeight())
                        .descrition(request.getDescription())
                        .status(Status.HOAT_DONG)
                        .build();

                // Lưu ProductDetail vào cơ sở dữ liệu
                productDetail = productDetailRepository.save(productDetail);
                log.info(productDetail.toString());
//                lưu ảnh

                for (ImgRequest i : request.getImage()
                ) {
                    Image image = Image.builder()
                            .productDetail(productDetail)
                            .publicId(i.getPublicId())
                            .url(i.getUrl())
                            .status(Status.HOAT_DONG)
                            .build();
                    imageRepository.save(image);

                }

                // Chuyển đổi ProductDetail thành ProductDetailResponse và thêm vào danh sách
                ProductDetailResponse response = ProductDetailResponse.builder()
                        .id(productDetail.getId())
                        .productName(productDetail.getProduct().getProductName())
                        .brandName(productDetail.getBrand().getBrandName())
                        .typeName(productDetail.getType().getTypeName())
                        .colorName(productDetail.getColor().getColorName())
                        .materialName(productDetail.getMaterial().getMaterialName())
                        .sizeName(productDetail.getSize().getSizeName())
                        .soleName(productDetail.getSole().getSoleName())
                        .genderName(productDetail.getGender().getGenderName())
                        .quantity(productDetail.getQuantity())
                        .price(productDetail.getPrice())
                        .weight(productDetail.getWeight())
                        .description(productDetail.getDescrition())
                        .status(productDetail.getStatus())
                        .updateAt(productDetail.getUpdatedAt())  // Giả sử có trường updatedAt trong entity
                        .updateBy(productDetail.getUpdatedBy())  // Giả sử có trường updatedBy trong entity

                        .build();

                productDetailResponses.add(response);
            }
        }

        // Trả về danh sách ProductDetailResponse
        return productDetailResponses;
    }

    @Override
    public boolean existsProductDetail(ProductDetailRequest request) {

        ProductDetail existingProductDetail = productDetailRepository.findByProductIdAndSizeIdAndColorId(
                request.getProductId(),
                request.getSizeId(),
                request.getColorId(),
                request.getBrandId(),
                request.getGenderId(),
                request.getMaterialId(),
                request.getTypeId(),
                request.getSoleId()
        );
//        log.warn(existingProductDetail.toString());
        return existingProductDetail != null ? true : false;
    }

    @Override
    public String switchStatus(Integer id, Status status) {
        ProductDetail brand = productDetailRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id ko tồn tại"));
        if (status.equals(Status.HOAT_DONG)) {
            brand.setStatus(Status.HOAT_DONG);
            productDetailRepository.save(brand);
            return "hoat dong";
        } else {
            brand.setStatus(Status.NGUNG_HOAT_DONG);
            productDetailRepository.save(brand);
            return "ngung hoat dong";

        }

    }

    @Override
    public ProductAtributeExistResponse getAttributeOfProductExist(Integer productId) {
        List<ProductAtributeExistResponse> list =  productDetailRepository.getAttributeOfProduct(productId);
        return !list.isEmpty()? list.get(0):null;
    }

    //Phầm em tú làm
    public List<ProductDetailResponse> getProductDetailsByProductId(Integer productId) {
        return productDetailRepository.findByProductId(productId);
    }
    @Override
    public AllNameProductDetailResponse getAllFilterOptions() {
        List<ProductDetail> details = productDetailRepository.findAll();

        return AllNameProductDetailResponse.builder()
                .brandNames(details.stream().map(d -> d.getBrand().getBrandName()).distinct().toList())
                .typeNames(details.stream().map(d -> d.getType().getTypeName()).distinct().toList())
                .colorNames(details.stream().map(d -> d.getColor().getColorName()).distinct().toList())
                .materialNames(details.stream().map(d -> d.getMaterial().getMaterialName()).distinct().toList())
                .sizeNames(details.stream().map(d -> d.getSize().getSizeName()).distinct().toList())
                .soleNames(details.stream().map(d -> d.getSole().getSoleName()).distinct().toList())
                .genderNames(details.stream().map(d -> d.getGender().getGenderName()).distinct().toList())
                .weight(details.stream().map(d -> d.getWeight().toString()).distinct().toList())
                .build();
    }


    //Em tú hết làm
}
