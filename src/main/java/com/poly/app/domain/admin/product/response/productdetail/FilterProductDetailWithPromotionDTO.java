package com.poly.app.domain.admin.product.response.productdetail;
import com.poly.app.domain.client.response.PromotionResponse;
import com.poly.app.domain.model.Image;
import com.poly.app.domain.repository.ImageRepository;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Getter
@Setter
@Builder
public class FilterProductDetailWithPromotionDTO {
    private Integer id;
    private String code;
    private String productName;
    private String brandName;
    private String typeName;
    private String colorName;
    private String materialName;
    private String sizeName;
    private String soleName;
    private String genderName;
    private Integer quantity;
    private Double price;
    private Double weight;
    private String description;
    private String status;
    private String updatedAt;
    private String updatedBy;
    List<Image> image;
    private PromotionResponse promotionResponse; // client


    // Constructor nhận từ interface
    public static FilterProductDetailWithPromotionDTO fromEntity(FilterProductDetailResponse response,

                                                                 PromotionResponse promotionResponse,     List<Image> image) {
        log.info("FilterProductDetailWithPromotionDTO fromEntity {}", response.getImage());

        return FilterProductDetailWithPromotionDTO.builder()
                .id(response.getId())
                .code(response.getCode())
                .productName(response.getProductName())
                .brandName(response.getBrandName())
                .typeName(response.getTypeName())
                .colorName(response.getColorName())
                .materialName(response.getMaterialName())
                .sizeName(response.getSizeName())
                .soleName(response.getSoleName())
                .genderName(response.getGenderName())
                .quantity(response.getQuantity())
                .price(response.getPrice())
                .weight(response.getWeight())
                .description(response.getDescrition()) // Chú ý lỗi chính tả: nên là getDescription()
                .status(response.getStatus())
                .updatedAt(response.getUpdatedAt())
                .updatedBy(response.getUpdatedBy())
                .image(image)
                .promotionResponse(promotionResponse)
                .build();
    }

}
