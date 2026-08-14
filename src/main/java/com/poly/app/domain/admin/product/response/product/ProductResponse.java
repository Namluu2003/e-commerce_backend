package com.poly.app.domain.admin.product.response.product;

import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.domain.model.Product;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.domain.model.Voucher;
import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Integer id;
    String code;
    String productName;
    Integer totalQuantity;
    Long updateAt;
    Status status;
    String image;



    public static ProductResponse fromEntity(Product product) {
        if (product == null) {
            return null;
        }

        // Tính tổng số lượng từ danh sách ProductDetail
        int totalQuantity = product.getProductDetails() != null
                ? product.getProductDetails().stream()
                .mapToInt(detail -> detail.getQuantity() != null ? detail.getQuantity() : 0)
                .sum()
                : 0;

        return ProductResponse.builder()
                .id(product.getId())                    // ID của sản phẩm
                .code(product.getCode())                // Mã sản phẩm
                .productName(product.getProductName())  // Tên sản phẩm
                .totalQuantity(totalQuantity)           // Tổng số lượng sản phẩm
                .updateAt(product.getUpdatedAt())       // Ngày cập nhật sản phẩm
                .status(product.getStatus())            // Trạng thái sản phẩm
                .build();
    }
}
