package com.poly.app.domain.admin.Statistical.Service;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BestSaleDTO {

        private String productName;
        private String brandName;
        private String typeName;
        private String colorName;
        private String materialName;
        private String sizeName;
        private String soleName;
        private String genderName;
        private int totalQuantitySold;
        private double price;
        public BestSaleDTO(String productName, String brandName, String typeName, String colorName,
                           String materialName, String sizeName, String soleName, String genderName,
                           Integer totalQuantitySold, Double price) {
                this.productName = productName;
                this.brandName = brandName;
                this.typeName = typeName;
                this.colorName = colorName;
                this.materialName = materialName;
                this.sizeName = sizeName;
                this.soleName = soleName;
                this.genderName = genderName;
                this.totalQuantitySold = totalQuantitySold;
                this.price = price;
        }

}
