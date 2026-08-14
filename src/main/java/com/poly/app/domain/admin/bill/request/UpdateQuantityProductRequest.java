package com.poly.app.domain.admin.bill.request;

import com.poly.app.domain.admin.product.request.productdetail.ProductDetailRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UpdateQuantityProductRequest {
    List<BillProductDetailRequest> productDetailRequests;
}
