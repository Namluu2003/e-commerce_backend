package com.poly.app.domain.admin.product.response.productdetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poly.app.infrastructure.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

public interface FilterProductDetailResponse {
    Integer getId();

    String getCode();

    String getProductName();

    String getBrandName();

    String getTypeName();

    String getColorName();

    String getMaterialName();

    String getSizeName();

    String getSoleName();

    String getGenderName();

    Integer getQuantity();

    Double getPrice();

    Double getWeight();

    String getDescrition();

    @JsonIgnore
    String getStatusRoot();

    String getUpdatedAt();

    String getUpdatedBy();
    String getImage();



    default String getStatus() {
        return "0".equals(getStatusRoot()) ? "HOAT_DONG" : "NGUNG_HOAT_DONG";
    }
}
