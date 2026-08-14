package com.poly.app.domain.admin.product.response.productdetail;

import com.poly.app.domain.admin.product.response.img.ImgResponse;
import com.poly.app.domain.client.response.PromotionResponse;
import com.poly.app.domain.model.Image;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    Integer id;

    String code;

    String productName;

    String brandName;

    String typeName;

    String colorName;

    String materialName;

    String sizeName;

    String soleName;

    String genderName;

    Integer quantity;

    Double price;

    Double weight;

    String description;

    Status status;

    long updateAt;

    String updateBy;

    PromotionResponse promotion;
    PromotionResponse promotionResponse;

    List<ImgResponse> image;

    public ProductDetailResponse(Integer id, String code, String productName, String brandName, String typeName, String colorName, String materialName, String sizeName, String soleName, String genderName, Integer quantity, Double price, Double weight, String description, Status status, long updateAt, String updateBy) {
        this.id = id;
        this.code = code;
        this.productName = productName;
        this.brandName = brandName;
        this.typeName = typeName;
        this.colorName = colorName;
        this.materialName = materialName;
        this.sizeName = sizeName;
        this.soleName = soleName;
        this.genderName = genderName;
        this.quantity = quantity;
        this.price = price;
        this.weight = weight;
        this.description = description;
        this.status = status;
        this.updateAt = updateAt;
        this.updateBy = updateBy;
    }


    public static  ProductDetailResponse fromEntity(ProductDetail  productDetail) {
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
                .build();
    }
}
