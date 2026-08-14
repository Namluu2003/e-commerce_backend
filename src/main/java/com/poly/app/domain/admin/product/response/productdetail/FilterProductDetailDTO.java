package com.poly.app.domain.admin.product.response.productdetail;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterProductDetailDTO {

    @Id
    private Integer id;  // Thêm @Id để đánh dấu trường id là khóa chính

    private String code;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "brand_name")
    private String brandName;
    @Column(name = "type_name")
    private String typeName;
    @Column(name = "color_name")
    private String colorName;
    @Column(name = "material_name")
    private String materialName;
    @Column(name = "size_name")
    private String sizeName;
    @Column(name = "sole_name")
    private String soleName;
    @Column(name = "gender_name")
    private String genderName;
    private Integer quantity;
    private Double price;
    private Double weight;
    private String descrition;
    private String status;
    @Column(name = "updatedat")
    private Long updatedAt;
    @Column(name = "updatedby")
    private String updatedBy;


}
