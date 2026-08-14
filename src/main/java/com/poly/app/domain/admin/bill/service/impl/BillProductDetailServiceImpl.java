package com.poly.app.domain.admin.bill.service.impl;

import com.poly.app.domain.admin.bill.request.AddProductToBillRequest;
import com.poly.app.domain.admin.bill.response.BillProductDetailResponse;
import com.poly.app.domain.admin.bill.response.BillProductResponse;
import com.poly.app.domain.admin.bill.service.BillProductDetailService;
import com.poly.app.domain.admin.bill.service.BillService;
import com.poly.app.domain.model.Bill;
import com.poly.app.domain.model.BillDetail;
import com.poly.app.domain.model.Brand;
import com.poly.app.domain.model.Color;
import com.poly.app.domain.model.Gender;
import com.poly.app.domain.model.Image;
import com.poly.app.domain.model.Material;
import com.poly.app.domain.model.Product;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.domain.model.Size;
import com.poly.app.domain.model.Sole;
import com.poly.app.domain.model.Type;
import com.poly.app.domain.repository.BillDetailRepository;
import com.poly.app.domain.repository.BillRepository;
import com.poly.app.domain.repository.ImageRepository;
import com.poly.app.domain.repository.ProductDetailRepository;
import com.poly.app.domain.repository.ProductRepository;
import com.poly.app.infrastructure.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillProductDetailServiceImpl implements BillProductDetailService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    BillDetailRepository billDetailRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    private BillService billService;


    @Override
    public List<BillProductDetailResponse> getBillProductDetailResponse(String billCode) {

        Bill bill = billRepository.findByBillCode(billCode);

        List<BillDetail> billDetails = billDetailRepository.findByBill(bill);


        List<BillProductDetailResponse> billProductDetailResponseList =
                billDetails.stream().map(billDetail ->
                                BillProductDetailResponse
                                        .builder()
                                        .ProductDetailId(billDetail.getProductDetail().getId())
                                        .productName(billDetail.getProductDetail().getProduct().getProductName())
                                        .color(billDetail.getProductDetail().getColor().getColorName())
                                        .size(billDetail.getProductDetail().getSize().getSizeName())
                                        .sizeName(billDetail.getProductDetail().getSize().getSizeName())
                                        .colorName(billDetail.getProductDetail().getColor().getColorName())
                                        .materialName(billDetail.getProductDetail().getMaterial().getMaterialName())
                                        .soleName(billDetail.getProductDetail().getSole().getSoleName())
                                        .price(billDetail.getPrice())
                                        .quantity(billDetail.getQuantity())
                                        .totalPrice(billDetail.getTotalMoney())
                                        .createdAt(billDetail.getCreatedAt())
                                        .urlImage(
                                                Optional.ofNullable(
                                                                imageRepository.findDistinctFirstByProductDetail(billDetail.getProductDetail())
                                                        )
                                                        .map(Image::getUrl) // Lấy URL nếu không null
                                                        .orElse(null)
                                        )
                                        .build())
                        .collect(Collectors.toList());

        return billProductDetailResponseList;
    }


    @Override
    public Page<BillProductResponse> getBillProductResponsePage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        List<BillProductResponse> billProductResponses = productPage.getContent().stream()
                .map(this::convertProductToBillProductResponse)
                .collect(Collectors.toList());

        // 4. Tạo Page<BillProductResponse> từ kết quả đã ánh xạ
        return new PageImpl<>(billProductResponses, pageable, productPage.getTotalElements());
    }

    @Override
    public void addProductToBill(AddProductToBillRequest request) {
        log.info("fin by bill code {}", request.getBillCode());
        Bill bill = billRepository.findByBillCode(request.getBillCode());
        if (bill == null) {
            throw new RestApiException("Hóa đơn không tồn tại", HttpStatus.NOT_FOUND);
        }
        ProductDetail productDetail = productDetailRepository.findById(request.getProductDetailId())
                .orElseThrow(() -> new RestApiException("Sản phẩm không tồn tại", HttpStatus.BAD_REQUEST));

        // Kiểm tra số lượng tồn
        if (productDetail.getQuantity() < request.getQuantity()) {
            throw new RestApiException("Số lượng sản phẩm không đủ", HttpStatus.BAD_REQUEST);
        }

        Optional<BillDetail> existingBillDetail = billDetailRepository.findByBillAndProductDetail(bill, productDetail);
        BillDetail billProductDetail;

        if (existingBillDetail.isPresent() &&
            existingBillDetail.get().getPrice().equals(productDetail.getPrice())) {
            // Nếu sản phẩm đã tồn tại và có cùng giá -> cập nhật số lượng
            billProductDetail = existingBillDetail.get();
            billProductDetail.setQuantity(billProductDetail.getQuantity() + request.getQuantity());
        } else {
            // Nếu sản phẩm chưa tồn tại hoặc khác giá -> tạo mới
            billProductDetail = new BillDetail();
            billProductDetail.setBill(bill);
            billProductDetail.setProductDetail(productDetail);
            billProductDetail.setQuantity(request.getQuantity());
        }

        billProductDetail.setPrice(productDetail.getPrice());

        // Tính tổng tiền cho sản phẩm này
        Double totalAmount = productDetail.getPrice() * request.getQuantity();
        billProductDetail.setTotalMoney(totalAmount);

        // Lưu chi tiết hóa đơn
        billDetailRepository.save(billProductDetail);

        // Cập nhật tổng tiền của hóa đơn
        updateBillTotalAmount(bill);

    }

    private void updateBillTotalAmount(Bill bill) {
        Double totalAmount = billDetailRepository.findByBill(bill)
                .stream()
                .mapToDouble(BillDetail::getTotalMoney)
                .sum();

        bill.setTotalMoney(totalAmount);
        billRepository.save(bill);
    }

    private BillProductResponse convertProductToBillProductResponse(Product product) {
        List<ProductDetail> productDetails = product.getProductDetails();
        List<Size> sizes = new ArrayList<>();
        List<Color> colors = new ArrayList<>();
        List<Gender> genders = new ArrayList<>();
        List<Material> materials = new ArrayList<>();
        List<Sole> soles = new ArrayList<>();
        List<Image> images = new ArrayList<>();
        List<Type> types = new ArrayList<>();
        List<Brand> brands = new ArrayList<>();
        for (ProductDetail productDetail : productDetails) {
            brands.add(productDetail.getBrand());
            colors.add(productDetail.getColor());
            sizes.add(productDetail.getSize());
            types.add(productDetail.getType());
            materials.add(productDetail.getMaterial());
            soles.add(productDetail.getSole());
        }
        return BillProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .productName(product.getProductName())
                .totalQuantity(product.getProductDetails().stream().mapToInt(ProductDetail::getQuantity).sum())
                .colors(new HashSet<>(colors))
                .genders(genders)
                .materials(materials)
                .sizes(sizes)
                .soles(soles)
                .types(types)
                .brand(!brands.isEmpty() ? brands.get(0) : null)
                .build();
    }


}
