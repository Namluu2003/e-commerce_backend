package com.poly.app.domain.client.service.impl;

import com.poly.app.domain.admin.address.AddressRequest;
import com.poly.app.domain.admin.bill.request.BillDetailRequest;
import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.domain.admin.product.response.gender.GenderResponse;
import com.poly.app.domain.admin.product.response.img.ImgResponse;
import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.domain.admin.product.response.size.SizeResponse;
import com.poly.app.domain.admin.product.response.sole.SoleResponse;
import com.poly.app.domain.client.repository.ProductViewRepository;
import com.poly.app.domain.client.request.AddCart;
import com.poly.app.domain.client.request.CreateBillClientRequest;
import com.poly.app.domain.client.response.*;
import com.poly.app.domain.client.service.ClientService;
import com.poly.app.domain.client.service.ZaloPayService;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.common.Meta;
import com.poly.app.domain.model.*;
import com.poly.app.domain.repository.*;
import com.poly.app.infrastructure.constant.*;
import com.poly.app.infrastructure.email.Email;
import com.poly.app.infrastructure.email.EmailSender;
import com.poly.app.infrastructure.exception.ApiException;
import com.poly.app.infrastructure.exception.ErrorCode;
import com.poly.app.infrastructure.util.VoucherBest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ClientServiceImpl implements ClientService {
    ProductViewRepository productViewRepository;
    ImageRepository imageRepository;
    SizeRepository sizeRepository;
    ColorRepository colorRepository;
    GenderRepository genderRepository;
    MaterialRepository materialRepository;
    SoleRepository  soleRepository;
    ProductRepository productRepository;
    CustomerRepository customerRepository;
    VoucherRepository voucherRepository;
    AddressRepository addressRepository;
    BillRepository billRepository;
    ProductDetailRepository productDetailRepository;
    BillDetailRepository billDetailRepository;
    BillHistoryRepository billHistoryRepository;
    PaymentMethodsRepository paymentMethodsRepository;
    PaymentBillRepository paymentBillRepository;
    ZaloPayService zaloPayService;
    CartDetailRepository cartDetailRepository;
    CartRepository cartRepository;
    EmailSender emailSender;
    SimpMessagingTemplate messagingTemplate;
    AnnouncementRepository announcementRepository;
    FreeshipOrderRepository freeshipOrderRepository;
    CustomerVoucherRepository customerVoucherRepository;


    @Override
    public Page<ProductViewResponse> getAllProductHadPromotion(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productViewRepository.getAllProductHadPromotion(pageable);
    }

    @Override
    public Page<ProductViewResponse> getAllProductHadSoleDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productViewRepository.getAllProductHadSoleDesc(pageable);
    }

    @Override
    public Page<ProductViewResponse> getAllProductHadCreatedAtDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productViewRepository.getAllProductHadCreatedAtDesc(pageable);
    }

    @Override
    public Page<ProductViewResponse> getAllProductHadViewsDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productViewRepository.getAllProductHadViewsDesc(pageable);
    }

    @Override
    public ProductDetailResponse findProductDetailbyProductIdAndColorIdAndSizeId(int productId, int colorId, int sizeId,int genderId,int materialId,int soleId) {
        try {
            ProductDetailResponse productDetail = productViewRepository.findByProductAndColorAndSize(productId, colorId, sizeId, genderId,materialId,soleId).get(0);
            List<ImgResponse> images = imageRepository.findByProductDetailId(productDetail.getId());
            List<PromotionResponse> promotionResponses = productViewRepository.findPromotionByProductDetailId(productDetail.getId(), ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
            PromotionResponse maxPromotion = promotionResponses.stream().max(Comparator.comparing(PromotionResponse::getDiscountValue))
                    .orElse(null);
            productDetail.setImage(images);
            productDetail.setPromotion(maxPromotion);
            return productDetail;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("ko tìm thấy chi tiết sản phẩm này");
        }


    }

    @Override
    public List<SizeResponse> findSizesByProductId(Integer productId) {
        return sizeRepository.findSizesByProductId(productId);
    }

    @Override
    public List<SizeResponse> findSizesByProductIdAndColorId(Integer productId, Integer colorId,Integer soleId, Integer materialId, Integer genderId) {
        return sizeRepository.findSizesByProductIdAndColorId(productId, colorId,soleId,materialId,genderId);
    }

    @Override
    public List<ColorResponse> findColorsByProductId(Integer productId) {
        return colorRepository.findColorsByProductId(productId);
    }

    @Override
    public Integer addViewProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ApiException(ErrorCode.SANPHAM_NOT_FOUND));
        int viewCurrent = (product.getViews() != null) ? product.getViews() : 0;
        product.setViews(viewCurrent + 1);
        productRepository.save(product);
        return product.getViews();
    }

    @Override
    @Transactional
    public String createBillClient(CreateBillClientRequest request) {
        Customer customer = null;

        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        }
        Voucher voucher = null;
        if (request.getVoucherId() != null) {
            voucher = voucherRepository.findById(request.getVoucherId()).orElse(null);
//trừ voucher
//            trừ voucher tổng số
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherRepository.save(voucher);

            if (voucher.getVoucherType().equals(VoucherType.PRIVATE) && customer != null) {
//                trừ ở prv
                CustomerVoucher customerVoucher = customerVoucherRepository.findCustomerVouchersByVoucherAndCustomer_Id(voucher, customer.getId());
                customerVoucher.setQuantity(customerVoucher.getQuantity() - 1);
                customerVoucherRepository.save(customerVoucher);
            }


        }
        Address address = null;
        if (request.getDetailAddressShipping() != null && customer != null) {
            Address address1 = addressRepository.findFirstByCustomerIdAndProvinceIdAndDistrictIdAndWardIdAndSpecificAddress
                    (customer.getId(),
                            request.getDetailAddressShipping().getProvinceId(),
                            request.getDetailAddressShipping().getDistrictId(),
                            request.getDetailAddressShipping().getWardId(),
                            request.getDetailAddressShipping().getSpecificAddress());
            if (address1 == null) {
                Address newAddress = Address
                        .builder()
                        .wardId(request.getDetailAddressShipping().getWardId())
                        .specificAddress(request.getDetailAddressShipping().getSpecificAddress())
                        .provinceId(request.getDetailAddressShipping().getProvinceId())
                        .districtId(request.getDetailAddressShipping().getDistrictId())
                        .customer(customer)
                        .build();
                address = addressRepository.save(newAddress);
            } else {
                address = address1;
            }

        } else {
            Address newAddress = Address
                    .builder()
                    .wardId(request.getDetailAddressShipping().getWardId())
                    .specificAddress(request.getDetailAddressShipping().getSpecificAddress())
                    .provinceId(request.getDetailAddressShipping().getProvinceId())
                    .districtId(request.getDetailAddressShipping().getDistrictId())
                    .customer(customer)
                    .build();
            address = addressRepository.save(newAddress);
        }

        Bill bill = Bill
                .builder()
                .typeBill(TypeBill.ONLINE)
                .customer(customer)
                .customerName(customer != null ? customer.getFullName() : request.getRecipientName())
                .customerMoney(request.getCustomerMoney())
                .discountMoney(request.getDiscountMoney())
                .moneyAfter(request.getMoneyAfter())
                .totalMoney(request.getTotalMoney())
                .moneyBeforeDiscount(request.getMoneyBeforeDiscount())
                .isFreeShip(request.getIsFreeShip())
                .shipDate(request.getShipDate())
                .shipMoney(request.getShipMoney())
                .shippingAddress(address)
                .shipDate(request.getShipDate())
                .shipMoney(request.getShipMoney())
                .voucher(voucher)
                .status(customer != null ? BillStatus.CHO_XAC_NHAN : BillStatus.DANG_XAC_MINH)
                .email(request.getEmail())
                .notes(request.getNotes())
                .numberPhone(request.getRecipientPhoneNumber())
                .build();
        Bill billSave = billRepository.save(bill);

//tao cthd
        for (BillDetailRequest billDetailRequest : request.getBillDetailRequests()) {
            ProductDetail productDetail = productDetailRepository.findById(billDetailRequest.getProductDetailId())
                    .orElseThrow(() -> new ApiException(ErrorCode.SANPHAM_NOT_FOUND));

            if (productDetail != null) {
                BillDetail billDetail = BillDetail
                        .builder()
                        .image(billDetailRequest.getImage())
                        .productDetail(productDetail)
                        .bill(billSave)
                        .price(billDetailRequest.getPrice())
                        .totalMoney(billDetailRequest.getPrice() * billDetailRequest.getQuantity())
                        .quantity(billDetailRequest.getQuantity())
                        .status(Status.HOAT_DONG)
                        .build();
                billDetailRepository.save(billDetail);
            }
        }
//nếu có customer thì xóa sản phẩm đó khỏi giỏ hàng
        if (request.getCustomerId() != null) {
            List<CartResponse> cartResponses = cartDetailRepository.getAllByCustomerIdNoPage(request.getCustomerId());
//            tìm kiếm xem có productDetailId của  mảng request.getBillDetailRequests() có trong cartResponses không nếu có thì delete cartResponse đó
//            for (CartResponse i: cartResponses
//                 ) {
//                for (BillDetailRequest i2: request.getBillDetailRequests()
//                     ) {
//                    if (i2.getProductDetailId() == i.getProductDetailId() ) {
//                        cartDetailRepository.deleteById(i.getCartDetailId());
//                    }
//                }
//
//            }
            cartResponses.forEach(cartItem ->
                    request.getBillDetailRequests().forEach(billItem -> {
                        if (billItem.getProductDetailId().equals(cartItem.getProductDetailId())) {
                            cartDetailRepository.deleteById(cartItem.getCartDetailId());
                        }
                    })
            );

        }

//Lưu lại lịch sử

        BillHistory billHistory = billHistoryRepository.save(BillHistory
                .builder()
                .customer(customer)
                .bill(billSave)
                .description(customer != null ? "chờ bên admin xác nhận" : " chờ xác minh danh tính")
                .status(customer != null ? BillStatus.CHO_XAC_NHAN : BillStatus.DANG_XAC_MINH)
                .build());
        billHistory.setUpdatedBy(bill.getEmail());
        billHistoryRepository.save(billHistory);

//        lưu ptthanh toán
        switch (request.getPaymentMethodsType()) {
            case ZALO_PAY -> {
                try {
//                    List<ItemRequest> itemsList = request.getBillDetailRequests()
//                            .stream()
//                            .map(billDetail -> new ItemRequest(
//                                    billDetail.getProductDetailId().toString(),
//                                    "Sản phẩm " + billDetail.getProductDetailId(),
//                                    billDetail.getPrice().longValue(),
//                                    billDetail.getQuantity()
//                            ))
//                            .toList();
//                    lưu phương thức thanh toán
//                    tìm keiesm xem thấy phương thức thanh toán zalopay ko
//
                    PaymentMethods paymentMethods = paymentMethodsRepository
                            .findByPaymentMethodsTypeAndAndPaymentMethod(PaymentMethodsType.THANH_TOAN_TRUOC, PaymentMethodEnum.ZALO_PAY)
                            .orElseGet(() -> paymentMethodsRepository.save(PaymentMethods.builder()
                                    .paymentMethod(PaymentMethodEnum.ZALO_PAY)
                                    .paymentMethodsType(PaymentMethodsType.THANH_TOAN_TRUOC)
                                    .status(Status.HOAT_DONG)
                                    .build()));
//và lưu pttt
                    PaymentBill paymentBill = paymentBillRepository.save(PaymentBill.builder()
                            .bill(billSave)
                            .paymentMethods(paymentMethods)
                            .payMentBillStatus(PayMentBillStatus.CHUA_THANH_TOAN)
                            .build());

//                    if (customer == null) {
//                        veritifyBill(billSave.getEmail(), billSave, request.getPaymentMethodsType().toString());
//                        return "đang xác minh đơn hàng";
//                    }
                    sendMail(request.getEmail(), billSave);
                    Map<String, Object> zaloPayResponse = zaloPayService.createPayment(
                            customer != null ? customer.getId().toString() : "guest",
                            request.getMoneyAfter().longValue(),
                            billSave.getId().longValue()
                    );

                    if (zaloPayResponse == null || !zaloPayResponse.containsKey("orderurl")) {
                        throw new ApiException(ErrorCode.INVALID_KEY);
                    }
                    paymentBill.setTransactionCode(zaloPayResponse.get("apptransid").toString());
                    paymentBill.setTotalMoney(Double.valueOf(zaloPayResponse.get("amount").toString()));
//                    paymentBillRepository.save(paymentBill);
                    return (String) zaloPayResponse.get("orderurl"); // Trả về URL thanh toán ngay lập tức
                } catch (Exception e) {
                    log.error("Lỗi khi tạo đơn hàng ZaloPay", e);
                    throw new ApiException(ErrorCode.INVALID_KEY);
                }

            }
            case COD -> {
                PaymentMethods paymentMethods = paymentMethodsRepository
                        .findByPaymentMethodsTypeAndAndPaymentMethod(PaymentMethodsType.COD, PaymentMethodEnum.COD)
                        .orElseGet(() -> paymentMethodsRepository.save(PaymentMethods.builder()
                                .paymentMethod(PaymentMethodEnum.COD)
                                .paymentMethodsType(PaymentMethodsType.COD)
                                .status(Status.HOAT_DONG)
                                .build()));

                paymentBillRepository.save(PaymentBill.builder()
                        .bill(billSave)
                        .paymentMethods(paymentMethods)
                        .payMentBillStatus(PayMentBillStatus.CHUA_THANH_TOAN)
                        .build());
                if (customer == null) {
                    veritifyBill(billSave.getEmail(), billSave, request.getPaymentMethodsType().toString());
                    return "đang xác minh đơn hàng";
                }
                sendMail(request.getEmail(), billSave);
                if (billSave.getCustomer() != null) {

                    try {
                        // Gửi thông báo qua WebSocket đến customer
                        // Tạo thông báo với nội dung phù hợp về việc hủy đơn hàng
                        Announcement announcement = new Announcement();
                        announcement.setCustomer(billSave.getCustomer());
                        announcement.setAnnouncementContent("Đơn hàng #" + billSave.getBillCode() + "đã được tạo thành công");
                        announcementRepository.save(announcement);

                        messagingTemplate.convertAndSend(

                                "/topic/global-notifications/" + billSave.getCustomer().getId(),
                                new NotificationResponse(
                                        Long.valueOf(announcement.getId()),
                                        announcement.getAnnouncementContent(),
                                        new Date(announcement.getCreatedAt()),
                                        false
                                )
                        );
                        System.out.println("Đã gửi thông báo WebSocket tới user: " + bill.getCustomer().getId());
                    } catch (Exception e) {
                        System.err.println("Lỗi khi gửi thông báo WebSocket: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                return "tạo hóa đơn thành công";
            }
        }

        return "tạo hóa đơn thành công";
    }

    @Override
    public Page<CartResponse> getAllCartCustomerId(Integer customerId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return cartDetailRepository.getAllByCustomerId(customerId, pageable);
    }

    @Override
    public CartResponse addProductToCart(AddCart addCart) {
//        kiểm tra có giỏ hàng chưa
        Cart cart = cartRepository.getCart(addCart.getCustomerId());
        if (cart == null) {
            cart = cartRepository.save(Cart.builder()
                    .customerid(customerRepository.findById(addCart.getCustomerId()).get())
                    .build());
        }

        CartDetail cartDetail;
        cartDetail = cartDetailRepository.findByProductDetailId(addCart.getProductDetailId(), addCart.getCustomerId());
        if (cartDetail != null) {
            cartDetail.setQuantity(cartDetail.getQuantity() + addCart.getQuantityAddCart());
            cartDetailRepository.save(cartDetail);
        } else {
            cartDetail = cartDetailRepository.save(CartDetail.builder()
                    .cart(cart)
                    .price(addCart.getPrice())
                    .productDetailId(productDetailRepository.findById(addCart.getProductDetailId()).orElseThrow(() -> new ApiException(ErrorCode.INVALID_KEY)))
                    .imageUrl(addCart.getImage())
                    .quantity(addCart.getQuantityAddCart())
                    .productName(addCart.getProductName())
                    .build());

        }


        return CartResponse.builder()
                .cartDetailId(cartDetail.getId())
                .quantityAddCart(cartDetail.getQuantity())
                .image(cartDetail.getImageUrl())
                .build();
    }

    @Override
    public String deleteCartById(Integer cartDetailId) {
        cartDetailRepository.findById(cartDetailId).orElseThrow(() -> new ApiException(ErrorCode.INVALID_KEY));
        cartDetailRepository.deleteById(cartDetailId);
        return "xóa thành công";
    }

    @Override
    public List<CartResponse> getAllByCustomserIdNopage(Integer customerId) {
        return cartDetailRepository.getAllByCustomerIdNoPage(customerId);
    }

    @Override
    public VoucherBestResponse voucherBest(String customerId, String billValue) {
        List<Voucher> vouchers = voucherRepository.findValidVouchers(customerId, Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant()));
        VoucherBest voucherBest = new VoucherBest();
        return voucherBest.recommendBestVoucher(Double.valueOf(billValue), vouchers);
    }

    @Override
    public List<Voucher> findValidVouchers(String customerId) {
        return voucherRepository.findValidVouchers(customerId, Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant()));
    }

    @Override
    public Integer plus(Integer id) {
        CartDetail cartDetail = cartDetailRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.INVALID_KEY));
        cartDetail.setQuantity(cartDetail.getQuantity() + 1);
        cartDetailRepository.save(cartDetail);

        return cartDetail.getQuantity();
    }

    @Override
    public Integer subtract(Integer id) {
        CartDetail cartDetail = cartDetailRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.INVALID_KEY));
        cartDetail.setQuantity(cartDetail.getQuantity() > 0 ? cartDetail.getQuantity() - 1 : 0);
        cartDetailRepository.save(cartDetail);

        return cartDetail.getQuantity();
    }

    @Override
    public Integer setQuantityCart(Integer id, Integer quantity) {
        CartDetail cartDetail = cartDetailRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.INVALID_KEY));
//        Integer quantityProduct = productDetailRepository.findById(cartDetail.getProductDetailId().getId()
//        ).get().getQuantity();
//        if (quantity > quantityProduct) {
//            cartDetail.setQuantity(quantityProduct);
//            cartDetailRepository.save(cartDetail);
//            throw new ApiException(ErrorCode.INVALID_KEY);
//        }
        cartDetail.setQuantity(quantity);
        cartDetailRepository.save(cartDetail);

        return cartDetail.getQuantity();
    }

    @Override
    public List<RealPriceResponse> getRealPrice(List<AddCart> addCartList) {
        List<RealPriceResponse> realPriceResponses = new ArrayList<>();

        for (AddCart i : addCartList) {
            ProductDetail productDetail = productDetailRepository.findById(i.getProductDetailId())
                    .orElseThrow(() -> new ApiException(ErrorCode.INVALID_KEY));
//            tìm promotion
            List<PromotionResponse> promotionResponses = productViewRepository.findPromotionByProductDetailId(productDetail.getId(), ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
            PromotionResponse maxPromotion = promotionResponses.stream().max(Comparator.comparing(PromotionResponse::getDiscountValue))
                    .orElse(null);
            if (productDetail != null) {
                RealPriceResponse response = RealPriceResponse.builder()
                        .cartDetailId(null)
                        .quantity(productDetail.getQuantity())
                        .productDetailId(i.getProductDetailId())
                        .productName(productDetail.getProduct().getProductName())
                        .price(maxPromotion != null ? productDetail.getPrice() - (productDetail.getPrice() * maxPromotion.getDiscountValue() / 100)
                                : productDetail.getPrice())
                        .quantityAddCart(i.getQuantityAddCart())
                        .status(productDetail.getStatus())
                        .note(null)
                        .build();

                realPriceResponses.add(response);
            }
        }

        return realPriceResponses;
    }

    @Override
    public Optional<Object> findAdressDefaulCustomerId(Integer customerId) {
        Object o = addressRepository.findFirstByCustomerIdAndIsAddressDefault(customerId, true);
        return Optional.of(o);
    }

    @Override
    public SearchStatusBillResponse searchBill(String billCode) {
        Bill bill = billRepository.findByBillCode(billCode);
        if (bill == null) {
            throw new ApiException(ErrorCode.HOA_DON_NOT_FOUND);
        }
        List<BillDetail> billDetails = billDetailRepository.findByBillId(bill.getId());

        List<BillDetailResponse> productDetails =
                billDetails.stream()
                        .map(item -> new BillDetailResponse(item.getId(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getImage(),
                                item.getProductDetail().getProduct().getProductName() + " [" + item.getProductDetail().getColor().getColorName() + "-" + item.getProductDetail().getSize().getSizeName() + "]",
                                item.getProductDetail().getProduct().getId(),
                                item.getProductDetail().getColor().getId(),
                                item.getProductDetail().getSize().getId()))
                        .collect(Collectors.toList());
        PaymentBill paymentBill = paymentBillRepository.findByBill(bill).get(0);
        PaymentMethods paymentMethods = paymentMethodsRepository.findById(paymentBill.getPaymentMethods().getId()).orElse(null);
        String voucherCode = "";
        if (bill.getVoucher() != null) voucherCode = bill.getBillCode();
        SearchStatusBillResponse searchStatusBillResponse = SearchStatusBillResponse.builder()
                .id(bill.getId())
                .billCode(bill.getBillCode())
                .discountMoney(bill.getDiscountMoney())
                .shipMoney(bill.getShipMoney())
                .totalMoney(bill.getTotalMoney())
                .moneyAfter(bill.getMoneyAfter())
                .shippingAddress(bill.getShippingAddress().getId())
                .customerName(bill.getCustomerName())
                .numberPhone(bill.getNumberPhone())
                .email(bill.getEmail())
                .typeBill(bill.getTypeBill())
                .notes(bill.getNotes())
                .status(bill.getStatus())
                .payment(paymentMethods.getPaymentMethod().name())
                .voucher(voucherCode)
                .addressRequest(AddressRequest.builder()
                        .provinceId(bill.getShippingAddress().getProvinceId())
                        .districtId(bill.getShippingAddress().getDistrictId())
                        .wardId(bill.getShippingAddress().getWardId())
                        .specificAddress(bill.getShippingAddress().getSpecificAddress())
                        .build())
                .billDetailResponse(productDetails)
                .build();


        return searchStatusBillResponse;
    }

    @Override
    public String veritifyBill(String billCode) {
        Bill bill = billRepository.findByBillCode(billCode);
        if (bill == null) {
            throw new ApiException(ErrorCode.BILL_NOT_EXISTS);
        }
        PaymentBill paymentBill = paymentBillRepository.findByBillId(bill.getId());
        PaymentMethods paymentMethods = paymentMethodsRepository.findById(paymentBill.getPaymentMethods().getId()).orElse(null);

        if (paymentMethods.getPaymentMethod().equals(PaymentMethodEnum.COD)) {
            bill.setStatus(BillStatus.CHO_XAC_NHAN);
            billRepository.save(bill);
            sendMail(bill.getEmail(), bill);
            BillHistory billHistory = billHistoryRepository.save(BillHistory
                    .builder()
                    .customer(null)
                    .bill(bill)
                    .description("xác minh danh tính thành công")
                    .status(BillStatus.CHO_XAC_NHAN)
                    .build());
            billHistory.setCreatedBy(bill.getEmail());
            billHistoryRepository.save(billHistory);
            return "xác minh thành công";
        } else if (paymentMethods.getPaymentMethod().equals(PaymentMethodEnum.ZALO_PAY)) {
            bill.setStatus(BillStatus.CHO_XAC_NHAN);
            billRepository.save(bill);
            sendMail(bill.getEmail(), bill);
            try {
                Map<String, Object> zaloPayResponse = zaloPayService.createPayment(
                        bill.getCustomer() != null ? bill.getId().toString() : "guest",
                        bill.getTotalMoney().longValue(),
                        bill.getId().longValue()
                );
                if (zaloPayResponse == null || !zaloPayResponse.containsKey("orderurl")) {
                    throw new ApiException(ErrorCode.INVALID_KEY);
                }
                billHistoryRepository.save(BillHistory
                        .builder()
                        .customer(null)
                        .description("xác minh danh tính thành công")
                        .bill(bill)
                        .status(BillStatus.CHO_XAC_NHAN)
                        .build());
                paymentBill.setTransactionCode(zaloPayResponse.get("apptransid").toString());
                paymentBill.setTotalMoney(Double.valueOf(zaloPayResponse.get("amount").toString()));
                paymentBillRepository.save(paymentBill);
                return (String) zaloPayResponse.get("orderurl"); // Trả về URL thanh toán ngay lập tức
            } catch (Exception e) {
                log.error("Lỗi khi tạo đơn hàng ZaloPay", e);
                throw new ApiException(ErrorCode.INVALID_KEY);
            }
        }
        return null;
    }

    @Override
    public ApiResponse<List<SearchStatusBillResponse>> getAllBillOfCustomerid(Integer customerId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<SearchStatusBillResponse> searchStatusBillResponses = new ArrayList<>();
        Page<Bill> page1 = billRepository.findByCustomerId(customerId, pageable);
        if (page1 == null) {
            throw new ApiException(ErrorCode.HOA_DON_NOT_FOUND);
        }

        for (Bill b :
                page1) {
            List<BillDetail> billDetails = billDetailRepository.findByBillId(b.getId());

            List<BillDetailResponse> productDetails =
                    billDetails.stream()
                            .map(item -> new BillDetailResponse(item.getId(),
                                    item.getQuantity(),
                                    item.getPrice(),
                                    item.getImage()
                                    , item.getProductDetail().getProduct().getProductName() + " [" + item.getProductDetail().getColor().getColorName() + "-" + item.getProductDetail().getSize().getSizeName() + "]",
                                    item.getProductDetail().getProduct().getId(),
                                    item.getProductDetail().getColor().getId(),
                                    item.getProductDetail().getSize().getId()))
                            .collect(Collectors.toList());
//            PaymentBill paymentBill = paymentBillRepository.findByBillId(b.getId());
//            PaymentMethods paymentMethods = null;
//            if (paymentBill != null) {
//                paymentMethods = paymentMethodsRepository.findById(paymentBill.getPaymentMethods().getId()).orElse(null);
//
//            }
            String voucherCode = "";
            if (b.getVoucher() != null) voucherCode = b.getBillCode();
            SearchStatusBillResponse searchStatusBillResponse = SearchStatusBillResponse.builder()
                    .id(b.getId())
                    .billCode(b.getBillCode())
                    .discountMoney(b.getDiscountMoney())
                    .shipMoney(b.getShipMoney())
                    .totalMoney(b.getTotalMoney())
                    .moneyAfter(b.getMoneyAfter())
                    .shippingAddress(b.getShippingAddress() != null ? b.getShippingAddress().getId() : null)
                    .customerName(b.getCustomerName())
                    .numberPhone(b.getNumberPhone())
                    .email(b.getEmail())
                    .typeBill(b.getTypeBill())
                    .notes(b.getNotes())
                    .status(b.getStatus())
//                    .payment(paymentMethods.getPaymentMethod() != null ? paymentMethods.getPaymentMethod().name() : "")
                    .voucher(voucherCode)
                    .addressRequest(b.getShippingAddress() != null ? AddressRequest.builder()
                            .provinceId(b.getShippingAddress().getProvinceId())
                            .districtId(b.getShippingAddress().getDistrictId())
                            .wardId(b.getShippingAddress().getWardId())
                            .specificAddress(b.getShippingAddress().getSpecificAddress())
                            .build() : null)
                    .billDetailResponse(productDetails)
                    .build();
            searchStatusBillResponses.add(searchStatusBillResponse);
        }

        return ApiResponse.<List<SearchStatusBillResponse>>builder()
                .message("geta bill customer")
                .meta(Meta.builder()
                        .totalElement(page1.getTotalElements())
                        .currentPage(page1.getNumber() + 1)
                        .totalPages(page1.getTotalPages()).build())
                .data(searchStatusBillResponses)
                .build();
    }

    @Override
    public String cancelBill(Integer id, String description) throws Exception {
        Bill bill = billRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.INVALID_KEY));
        PaymentBill paymentBill = paymentBillRepository.findByBillId(bill.getId());
        if (paymentBill != null) {
            PaymentMethods paymentMethods = paymentMethodsRepository.findById(paymentBill.getPaymentMethods().getId()).orElse(null);
            // Xử lý logic hoàn tiền nếu cần
            switch (paymentMethods.getPaymentMethod()) {
                case COD -> {

                    bill.setStatus(BillStatus.DA_HUY);
                    billRepository.save(bill);


                    billHistoryRepository.save(BillHistory
                            .builder()
                            .customer(bill.getCustomer())
                            .description("Hủy đơn hàng: " + bill.getBillCode() + " Lý do: " + description)
                            .bill(bill)
                            .status(BillStatus.DA_HUY)
                            .build());

                    if (bill.getCustomer() != null) {

                        try {
                            // Gửi thông báo qua WebSocket đến customer
                            // Tạo thông báo với nội dung phù hợp về việc hủy đơn hàng
                            Announcement announcement = new Announcement();
                            announcement.setCustomer(bill.getCustomer());
                            announcement.setAnnouncementContent("Đơn hàng #" + bill.getBillCode() + " đã bị hủy. Lý do: " + description);
                            announcementRepository.save(announcement);

                            messagingTemplate.convertAndSend(

                                    "/topic/global-notifications/" + bill.getCustomer().getId(),
                                    new NotificationResponse(
                                            Long.valueOf(announcement.getId()), // Chắc chắn ID không null sau khi đã save
                                            announcement.getAnnouncementContent(),
                                            new Date(announcement.getCreatedAt()),
                                            false
                                    )
                            );
                            System.out.println("Đã gửi thông báo WebSocket tới user: " + bill.getCustomer().getId());
                        } catch (Exception e) {
                            System.err.println("Lỗi khi gửi thông báo WebSocket: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    // Gửi email thông báo hủy đơn hàng
                    cancelBill(bill.getEmail(), bill);

                }
                case ZALO_PAY -> {

                    try {
                        PaymentMethods paymentMethod = paymentMethodsRepository
                                .findByPaymentMethodsTypeAndAndPaymentMethod(PaymentMethodsType.HOAN_TIEN, PaymentMethodEnum.ZALO_PAY)
                                .orElseGet(() -> paymentMethodsRepository.save(PaymentMethods.builder()
                                        .paymentMethod(PaymentMethodEnum.ZALO_PAY)
                                        .paymentMethodsType(PaymentMethodsType.HOAN_TIEN)
                                        .status(Status.HOAT_DONG)
                                        .build()));
                        log.warn("chạy vao đây----------------------");
                        log.info("" + id);
                        log.info("" + bill.getMoneyAfter());
                        log.info("" + description);
                        Map<String, Object> refund = refundBill(bill.getId(), bill.getMoneyAfter().intValue(), "Hoan tien ");
                        bill.setStatus(BillStatus.DA_HUY);
                        billRepository.save(bill);

//                        paymentBill.setPayMentBillStatus(PayMentBillStatus.DA_HOAN_TIEN);
//                        paymentBillRepository.save(paymentBill);
//log.warn("sdfkfdsjfldsjfkdsjflsdfjsdlkfjdskfjdslkfjsdfsdfdsfdsfsfdsfdsfdfsdfsd"+refund.toString());
//                        ghi thêm một bản paymentBill
                        PaymentBill paymentBillRefund = PaymentBill.builder()
                                .payMentBillStatus(PayMentBillStatus.DA_HOAN_TIEN)
                                .bill(bill)
                                .paymentMethods(paymentMethod)
                                .transactionCode(refund.get("mrefundid").toString())
                                .totalMoney(bill.getMoneyAfter())
                                .build();
                        paymentBillRepository.save(paymentBillRefund);

//                        ghi lịch sử
                        billHistoryRepository.save(BillHistory
                                .builder()
                                .customer(bill.getCustomer())
                                .description("Hủy đơn hàng: " + bill.getBillCode() + "  Lý do: " + description + " mã truy vấn hoàn tiền: " + refund.get("mrefundid").toString())
                                .bill(bill)
                                .status(BillStatus.DA_HUY)
                                .build());
//                        nhảy tb
                        if (bill.getCustomer() != null) {

                            try {
                                // Gửi thông báo qua WebSocket đến customer
                                // Tạo thông báo với nội dung phù hợp về việc hủy đơn hàng
                                Announcement announcement = new Announcement();
                                announcement.setCustomer(bill.getCustomer());
                                announcement.setAnnouncementContent("Đơn hàng #" + bill.getBillCode() + " đã bị hủy. Lý do: "
                                                                    + description + " mã truy vấn hoàn tiền: " + refund);
                                announcementRepository.save(announcement);

                                messagingTemplate.convertAndSend(

                                        "/topic/global-notifications/" + bill.getCustomer().getId(),
                                        new NotificationResponse(
                                                Long.valueOf(announcement.getId()), // Chắc chắn ID không null sau khi đã save
                                                announcement.getAnnouncementContent(),
                                                new Date(announcement.getCreatedAt()),
                                                false
                                        )
                                );
                                System.out.println("Đã gửi thông báo WebSocket tới user: " + bill.getCustomer().getId());
                            } catch (Exception e) {
                                System.err.println("Lỗi khi gửi thông báo WebSocket: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        log.error("lỗi hoàn tiền");
                    }

                    cancelBillRefund(bill.getEmail(), bill);

                    return "hủy và hoàn tiền zalo pay";
                }
                default -> {
                    return "lỗi";
                }
            }
        }


        return "Hủy đơn hàng thành công. Lý do: " + description;
    }

    @Override
    public String buyBack(Integer billId, Integer customerId) {
        List<BillDetail> billDetail = billDetailRepository.findByBillId(billId);
        Cart cart = cartRepository.getCart(customerId);

        CartDetail cartDetail;
        for (BillDetail b :
                billDetail) {
            cartDetail = cartDetailRepository.findByProductDetailId(b.getProductDetail().getId(), customerId);
            if (cartDetail != null) {
                cartDetail.setQuantity(cartDetail.getQuantity() + b.getQuantity());
                cartDetailRepository.save(cartDetail);
            } else {
                cartDetail = cartDetailRepository.save(CartDetail.builder()
                        .cart(cart)
                        .price(b.getPrice())
                        .productDetailId(b.getProductDetail())
                        .imageUrl(b.getImage())
                        .quantity(b.getQuantity())
                        .productName(b.getProductDetail().getProduct().getProductName()
                                     + " [ " + b.getProductDetail().getColor().getColorName() + " - "
                                     + b.getProductDetail().getSize().getSizeName() + " ] ")
                        .build());

            }
        }

        return "thêm vào giỏ hàng thành coong";
    }

    @Override
    public Page<ProductViewResponse> findFilteredProducts(Long productId, Long brandId, Long genderId, Long typeId, Long colorId,
                                                          Long materialId, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productViewRepository.findFilteredProducts(productId, brandId, genderId, typeId, colorId, materialId, minPrice, maxPrice, pageable);
    }

    @Override
    public Map<String, Object> refund(Integer billId, Integer MoneyRefund, String description) throws Exception {
        return refundBill(billId, MoneyRefund, description);
    }

    private void sendMail(String sendToMail, Bill billCode) {
        Email email = new Email();
        String[] emailSend = {sendToMail};
        email.setToEmail(emailSend);
        email.setSubject("TheHands-Tạo Hóa Đơn Thành Công");
        email.setTitleEmail("");
        email.setBody("<!DOCTYPE html>\n" +
                      "<html lang=\"en\">\n" +
                      "<head>\n" +
                      "    <meta charset=\"UTF-8\">\n" +
                      "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                      "    <title>Hóa đơn TheHands</title>\n" +
                      "</head>\n" +
                      "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 50px;\">\n" +
                      "    <div style=\"max-width: 600px; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); margin: auto;\">\n" +
                      "        <h2 style=\"color: #333;\">🎉 Hóa đơn đã được tạo thành công! 🎉</h2>\n" +
                      "        <p style=\"color: #555;\">Cảm ơn bạn đã mua hàng tại <strong>TheHands</strong>. Dưới đây là thông tin đơn hàng của bạn:</p>\n" +
                      "        <hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\">\n" +
                      "        <p><strong>📧 Email:</strong> " + sendToMail + "</p>\n" +
                      "        <p><strong>🧾 Mã hóa đơn:</strong> <span style=\"color: #007bff; font-weight: bold;\">" + billCode.getBillCode() + "</span></p>\n" +
                      "        <hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\">\n" +
                      "        <p style=\"color: #555;\">Bạn có thể kiểm tra hóa đơn của mình bằng cách nhấn vào nút bên dưới:</p>\n" +
                      "        <a href=\"http://localhost:5173/searchbill?billcode=" + billCode.getBillCode() + "\" style=\"display: inline-block; background-color: #007bff; color: #ffffff; padding: 12px 20px; border-radius: 5px; text-decoration: none; font-weight: bold;\">🔍 Xem hóa đơn</a>\n" +
                      "        <p style=\"margin-top: 20px; font-size: 12px; color: #999;\">Nếu bạn không thực hiện giao dịch này, vui lòng bỏ qua email này.</p>\n" +
                      "    </div>\n" +
                      "</body>\n" +
                      "</html>");


        emailSender.sendEmail(email);
    }

    private void veritifyBill(String sendToMail, Bill billCode, String paymentMethod) {
        Email email = new Email();
        String[] emailSend = {sendToMail};
        email.setToEmail(emailSend);
        email.setSubject("TheHands-Xác Minh Đơn Hàng");
        email.setTitleEmail("");
        email.setBody(
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Xác Minh Đơn Hàng TheHands</title>
                        </head>
                        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 50px;">
                            <div style="max-width: 600px; background-color: #ffffff; padding: 30px; border-radius: 15px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin: auto;">
                                <h2 style="color: #333; font-size: 24px; margin-bottom: 10px;">✅ Xác Minh Đơn Hàng Của Bạn</h2>
                                <p style="color: #666; font-size: 16px; line-height: 1.5;">Cảm ơn bạn đã đặt hàng tại <strong>TheHands</strong>. Vui lòng xác minh đơn hàng của bạn để tiếp tục xử lý.</p>
                                <hr style="border: none; border-top: 1px dashed #ddd; margin: 25px 0;">
                                <p style="color: #555; font-size: 16px;"><strong>📧 Email:</strong> %s</p>
                                <p style="color: #555; font-size: 16px;"><strong>🧾 Mã đơn hàng:</strong> <span style="color: #007bff; font-weight: bold;">%s</span></p>
                                <hr style="border: none; border-top: 1px dashed #ddd; margin: 25px 0;">
                                <p style="color: #666; font-size: 16px; line-height: 1.5;">Để xác minh và xem chi tiết đơn hàng, hãy nhấp vào nút bên dưới:</p>
                                <a href="http://localhost:5173/veritify?billcode=%s&paymentmethod=%s" 
                                   style="display: inline-block; background-color: #007bff; color: #ffffff; padding: 12px 25px; border-radius: 8px; text-decoration: none; font-weight: bold; font-size: 16px; transition: background-color 0.3s;">
                                   ✅ Xác Minh Đơn Hàng
                                </a>
                                <p style="margin-top: 25px; color: #e74c3c; font-size: 14px; font-style: italic;">Lưu ý: Nếu không xác minh trong 24 giờ, đơn hàng có thể bị hủy.</p>
                                <p style="margin-top: 15px; font-size: 12px; color: #999; line-height: 1.4;">Nếu bạn không đặt đơn hàng này, vui lòng bỏ qua email hoặc liên hệ hỗ trợ qua <a href='mailto:support@thehands.com' style='color: #007bff;'>support@thehands.com</a>.</p>
                            </div>
                        </body>
                        </html>
                        """.formatted(sendToMail, billCode.getBillCode(), billCode.getBillCode(), paymentMethod)
        );


        emailSender.sendEmail(email);
    }

    private void cancelBill(String sendToMail, Bill billCode) {
        Email email = new Email();
        String[] emailSend = {sendToMail};
        email.setToEmail(emailSend);
        email.setSubject("TheHands-Thông Báo Hủy Đơn Hàng");
        email.setTitleEmail("");
        email.setBody(
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Hủy Đơn Hàng Thành Công - TheHands</title>
                        </head>
                        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 50px;">
                            <div style="max-width: 600px; background-color: #ffffff; padding: 30px; border-radius: 15px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin: auto;">
                                <h2 style="color: #333; font-size: 24px; margin-bottom: 10px;">❌ Hủy Đơn Hàng Thành Công</h2>
                                <p style="color: #666; font-size: 16px; line-height: 1.5;">Chúng tôi xin thông báo rằng đơn hàng của bạn tại <strong>TheHands</strong> đã được hủy thành công theo yêu cầu.</p>
                                <hr style="border: none; border-top: 1px dashed #ddd; margin: 25px 0;">
                                <p style="color: #555; font-size: 16px;"><strong>📧 Email:</strong> %s</p>
                                <p style="color: #555; font-size: 16px;"><strong>🧾 Mã đơn hàng:</strong> <span style="color: #e74c3c; font-weight: bold;">%s</span></p>
                                <hr style="border: none; border-top: 1px dashed #ddd; margin: 25px 0;">
                                <p style="color: #666; font-size: 16px; line-height: 1.5;">Nếu bạn cần hỗ trợ hoặc muốn đặt lại đơn hàng, vui lòng liên hệ với chúng tôi.</p>
                                <a href="mailto:support@thehands.com" 
                                   style="display: inline-block; background-color: #28a745; color: #ffffff; padding: 12px 25px; border-radius: 8px; text-decoration: none; font-weight: bold; font-size: 16px; transition: background-color 0.3s;">
                                   📩 Liên Hệ Hỗ Trợ
                                </a>
                                <p style="margin-top: 25px; font-size: 12px; color: #999; line-height: 1.4;">Cảm ơn bạn đã tin tưởng <strong>TheHands</strong>. Hy vọng sẽ được phục vụ bạn trong tương lai!</p>
                            </div>
                        </body>
                        </html>
                        """.formatted(sendToMail, billCode.getBillCode())
        );

        emailSender.sendEmail(email);
    }

    private void cancelBillRefund(String sendToMail, Bill billCode) {
        Email email = new Email();
        String[] emailSend = {sendToMail};
        email.setToEmail(emailSend);
        email.setSubject("TheHands-Thông Báo Hủy Đơn Hàng-Hoàn tiền giao dịch");
        email.setTitleEmail("");
        email.setBody(
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Hủy Đơn Hàng Thành Công - TheHands</title>
                        </head>
                        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 50px;">
                            <div style="max-width: 600px; background-color: #ffffff; padding: 30px; border-radius: 15px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin: auto;">
                                <h2 style="color: #333; font-size: 24px; margin-bottom: 10px;">❌ Hủy Đơn Hàng Thành Công</h2>
                                <p style="color: #666; font-size: 16px; line-height: 1.5;">Chúng tôi xin thông báo rằng đơn hàng của bạn tại <strong>TheHands</strong> đã được hủy thành công theo yêu cầu với số tiền: <strong>%s</strong>. tiền khi đã giao dịch của bạn sẽ được hoàn vào tài khoản, nếu trong vòng 24h tiền chưa được về tài khoản bạn có thể liên hệ tới cửa hàng theo số: 18008080 để được hỗ trợ</p>
                                <hr style="border: none; border-top: 1px dashed #ddd; margin: 25px 0;">
                                <p style="color: #555; font-size: 16px;"><strong>📧 Email:</strong> %s</p>
                                <p style="color: #555; font-size: 16px;"><strong>🧾 Mã đơn hàng:</strong> <span style="color: #e74c3c; font-weight: bold;">%s</span></p>
                                <hr style="border: none; border-top: 1px dashed #ddd; margin: 25px 0;">
                                <p style="color: #666; font-size: 16px; line-height: 1.5;">Nếu bạn cần hỗ trợ hoặc muốn đặt lại đơn hàng, vui lòng liên hệ với chúng tôi.</p>
                                <a href="mailto:support@thehands.com" 
                                   style="display: inline-block; background-color: #28a745; color: #ffffff; padding: 12px 25px; border-radius: 8px; text-decoration: none; font-weight: bold; font-size: 16px; transition: background-color 0.3s;">
                                   📩 Liên Hệ Hỗ Trợ
                                </a>
                                <p style="margin-top: 25px; font-size: 12px; color: #999; line-height: 1.4;">Cảm ơn bạn đã tin tưởng <strong>TheHands</strong>. Hy vọng sẽ được phục vụ bạn trong tương lai!</p>
                            </div>
                        </body>
                        </html>
                        """.formatted(billCode.getMoneyAfter(), sendToMail, billCode.getBillCode())
        );

        emailSender.sendEmail(email);
    }

    private Map<String, Object> refundBill(Integer billId, Integer moneyRefund, String description) throws Exception {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new ApiException(ErrorCode.INVALID_KEY));
        PaymentBill paymentBill = paymentBillRepository.findByBillId(billId);
//        /lấy status
        Map<String, Object> mapStatus = zaloPayService.getStatusByApptransid(paymentBill.getTransactionCode());
        log.info("1" + mapStatus.toString());
        Object zptransid = mapStatus.get("zptransid");
        if (!(Integer.valueOf(mapStatus.get("returncode").toString()) >= 1 && zptransid != null))
            return mapStatus;

//        hoàn tiền
        Map<String, Object> mapRefund = zaloPayService.refund
                (Long.valueOf(zptransid.toString()), moneyRefund.intValue(), description);
        Object refund = mapRefund.get("mrefundid");
        log.info("2" + mapRefund.toString());
        if (!(Integer.valueOf(mapStatus.get("returncode").toString()) >= 1 && refund != null)) {
            return mapRefund;

        } else {
            return mapRefund;

        }

//        Map<String, Object> mapStatusReFund = zaloPayService.getRefundStatus(refund.toString());
//        if (!(Integer.valueOf(mapStatusReFund.get("returncode").toString()) >= 1 && mapStatusReFund != null))
//            return mapStatusReFund.get("returnmessage").toString();
//
//        return mapStatusReFund.get("returnmessage").toString();

    }
//    lấy ra các đượt giảm giá theo productdetail để tìm ra khoảng giá thật sẽ giảm

    public List<ProductDetailDiscountDTO> getDiscountedProductDetails(Integer productId, Integer colorId, Integer genderId) {
        ZonedDateTime vietnamTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        List<Object[]> results = productViewRepository.findDiscountedProductDetails(productId, colorId, genderId, Timestamp.from(vietnamTime.toInstant()));

        // Ánh xạ và lọc
        return results.stream()
                .map(row -> new ProductDetailDiscountDTO(
                        (Integer) row[0],     // productId
                        (Integer) row[1],     // productDetailId
                        (Integer) row[2],     // colorId
                        (Integer) row[3],     // genderId
                        (Double) row[4],      // price
                        (Double) row[5],      // maxDiscount
                        (String) row[6]       // discountedPrice
                ))
                .filter(dto -> dto.getMaxDiscount() != null && dto.getMaxDiscount() > 0) // Lọc các bản ghi có giảm giá
                .collect(Collectors.toList());
    }

    /**
     * Chuyển đổi danh sách ProductDetailDiscountDTO thành PromotionView
     */
    public PromotionView getPromotionView(Integer productId, Integer colorId, Integer genderId) {
        List<ProductDetailDiscountDTO> discountedDetails = getDiscountedProductDetails(productId, colorId, genderId);

        if (discountedDetails.isEmpty()) {
            return PromotionView.builder()
                    .rangePriceRoot("0")
                    .rangePriceAfterPromotion("0")
                    .maxDiscount("0")
                    .build();
        }

        // Tính khoảng giá gốc
        Double minPrice = discountedDetails.stream()
                .map(ProductDetailDiscountDTO::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(0.0);
        Double maxPrice = discountedDetails.stream()
                .map(ProductDetailDiscountDTO::getPrice)
                .max(Comparator.naturalOrder())
                .orElse(0.0);
        String rangePriceRoot = minPrice.equals(maxPrice) ? String.valueOf(minPrice) : minPrice + " - " + maxPrice;

        // Tính khoảng giá sau giảm
        Double minDiscountedPrice = discountedDetails.stream()
                .map(dto -> Double.parseDouble(dto.getDiscountedPrice()))
                .min(Comparator.naturalOrder())
                .orElse(0.0);
        Double maxDiscountedPrice = discountedDetails.stream()
                .map(dto -> Double.parseDouble(dto.getDiscountedPrice()))
                .max(Comparator.naturalOrder())
                .orElse(0.0);
        String rangePriceAfterPromotion = minDiscountedPrice.equals(maxDiscountedPrice)
                ? String.valueOf(minDiscountedPrice)
                : minDiscountedPrice + " - " + maxDiscountedPrice;

        // Tìm giá trị giảm cao nhất
        Double maxDiscount = discountedDetails.stream()
                .map(ProductDetailDiscountDTO::getMaxDiscount)
                .max(Comparator.naturalOrder())
                .orElse(0.0);
        String maxDiscountStr = String.valueOf(maxDiscount.intValue());

        return PromotionView.builder()
                .rangePriceRoot(rangePriceRoot)
                .rangePriceAfterPromotion(rangePriceAfterPromotion)
                .maxDiscount(maxDiscountStr)
                .build();
    }

    @Override
    public Boolean hasBought(Integer customerId, Integer productId) {
        return productViewRepository.hasBought(customerId, productId) > 0;
    }

    @Override
    public List<GenderResponse> findGenderByProductId(Integer productId) {
        return genderRepository.findGendersByProductId(productId);
    }

    @Override
    public List<MaterialResponse> findMaterialByProductId(Integer productId) {
        return materialRepository.findMaterialsByProductId(productId);
    }

    @Override
    public List<SoleResponse> findSoleByProductId(Integer productId) {
        return soleRepository.findSolesByProductId(productId);
    }

    @Override
    public List<ColorResponse> findColorByProductIdAndSoleId(Integer productId, Integer soleId, Integer materialId, Integer genderId) {
        return colorRepository.findColorsByProductIdAndSoleId(productId,soleId,materialId,genderId);
    }

    @Override
    public List<SoleResponse> findSoleByProductIdAndMaterialId(Integer productId, Integer materialId,Integer genderId) {
        return soleRepository.findSolesByProductIdAndMaterialId(productId,materialId,genderId);
    }

    @Override
    public List<MaterialResponse> findMaterialByProductIdAndGenderId(Integer productId, Integer genderId) {
        return materialRepository.findMaterialsByProductIdAndGenderId(productId,genderId);
    }


}
