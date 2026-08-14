package com.poly.app.domain.admin.product.response.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllNameProductDetailResponse {
    private List<String> brandNames;
    private List<String> typeNames;
    private List<String> colorNames;
    private List<String> materialNames;
    private List<String> sizeNames;
    private List<String> soleNames;
    private List<String> genderNames;
    private List<String> weight;

}
