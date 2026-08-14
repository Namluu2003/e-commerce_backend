package com.poly.app.domain.admin.Statistical.Service;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MinProductDTO {
        private int productId;
        private String productName;
        private String typeName;
        private String colorName;
        private String sizeName;
        private String soleName;
        private String genderName;
        private double price;
        private int quantity;
    }


