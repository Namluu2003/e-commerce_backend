package com.poly.app.domain.admin.bill.service.impl;

import com.poly.app.domain.admin.bill.request.BillDetailRequest;
import com.poly.app.domain.admin.bill.request.BillProductDetailRequest;
import com.poly.app.domain.admin.bill.request.CreateBillRequest;
import com.poly.app.domain.admin.bill.request.UpdateProductBillRequest;
import com.poly.app.domain.admin.bill.request.UpdateQuantityVoucherRequest;
import com.poly.app.domain.admin.bill.request.UpdateStatusBillRequest;
import com.poly.app.domain.admin.bill.response.BillResponse;
import com.poly.app.domain.admin.bill.response.UpdateBillRequest;
import com.poly.app.domain.admin.bill.service.BillHistoryService;
import com.poly.app.domain.admin.bill.service.BillService;
import com.poly.app.domain.admin.bill.service.WebSocketService;
import com.poly.app.domain.admin.product.request.productdetail.ProductDetailRequest;
import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.domain.admin.staff.response.AddressReponse;
import com.poly.app.domain.admin.voucher.response.VoucherReponse;
import com.poly.app.domain.admin.voucher.service.VoucherService;
import com.poly.app.domain.auth.service.AuthenticationService;
import com.poly.app.domain.client.response.NotificationResponse;
import com.poly.app.domain.model.*;
import com.poly.app.domain.repository.*;
import com.poly.app.infrastructure.constant.BillStatus;
import com.poly.app.infrastructure.constant.PayMentBillStatus;
import com.poly.app.infrastructure.constant.PaymentMethodEnum;
import com.poly.app.infrastructure.constant.PaymentMethodsType;
import com.poly.app.infrastructure.constant.Status;
import com.poly.app.infrastructure.constant.TypeBill;
import com.poly.app.infrastructure.constant.VoucherType;
import com.poly.app.infrastructure.email.Email;
import com.poly.app.infrastructure.email.EmailSender;
import com.poly.app.infrastructure.exception.ApiException;
import com.poly.app.infrastructure.exception.ErrorCode;
import com.poly.app.infrastructure.exception.RestApiException;
import com.poly.app.infrastructure.security.Auth;
import com.poly.app.infrastructure.util.BillStatusFormatter;
import com.poly.app.infrastructure.util.DateUtil;
import com.poly.app.infrastructure.util.GenHoaDon;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    BillRepository billRepository;


    @Autowired
    BillHistoryRepository billHistoryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    AddressRepository addressRepository;


    @Autowired
    BillHistoryService billHistoryService;

    @Autowired
    GenHoaDon genHoaDon;
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;
    @Autowired
    private PaymentBillRepository paymentBillRepository;
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private Auth auth;
    @Autowired
    private CustomerVoucherRepository customerVoucherRepository;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    EmailSender emailSender;

    @Autowired
    AnnouncementRepository announcementRepository;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Page<BillResponse> getPageBill(Integer size, Integer page,
                                          BillStatus statusBill,
                                          TypeBill typeBill,
                                          String search,
                                          String startDate,
                                          String endDate
    ) {
        Sort sort = null;
        if (statusBill == BillStatus.CHO_XAC_NHAN) {
            sort = Sort.by(Sort.Direction.ASC, "createdAt");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        // Sử dụng Specification hoặc Predicate để xây dựng các điều kiện động
        Specification<Bill> spec = Specification.where(null);

        // Thêm điều kiện lọc theo `statusBill`
        if (statusBill != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), statusBill));
        }

        // Thêm điều kiện lọc theo `typeBill`
        if (typeBill != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("typeBill"), typeBill));
        }

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                // Thực hiện join tới thực thể Customer
                Join<Object, Object> customerJoin = root.join("customer", JoinType.LEFT);
                // Thêm điều kiện tìm kiếm
                return criteriaBuilder.or(
                        criteriaBuilder.like(customerJoin.get("fullName"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("billCode"), "%" + search + "%")
                );
            });
        }

        if (startDate != null && !startDate.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), DateUtil.parseDateLong(startDate)));
        }
        if (endDate != null && !endDate.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), DateUtil.parseDateLong(endDate)));
        }
        Page<Bill> billPage = billRepository.findAll(spec, pageable);
        List<BillResponse> billResponses = billPage.getContent().stream()
                .map(this::convertBillToBillResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(billResponses, pageable, billPage.getTotalElements());
    }


    @Override
    public BillResponse getBillResponseByBillCode(String billCode) {
        Bill bill = billRepository.findByBillCode(billCode);
        return convertBillToBillResponse(bill);
    }

    @Override
    @Transactional
    public Map<String, ?> updateStatusBill(String billCode, UpdateStatusBillRequest request) {

        Bill bill = billRepository.findByBillCode(billCode);

        if (bill == null) {
            throw new ApiException(ErrorCode.HOA_DON_NOT_FOUND);
        }
        if (request.getStatus() == BillStatus.DA_HUY
                && (bill.getStatus() == BillStatus.DA_XAC_NHAN ||
                bill.getStatus() == BillStatus.CHO_XAC_NHAN)) {
            // Kiểm tra đã thanh toán chưa. Nếu đã thanh toán thì tạo ra 1 payment hoàn tiền
            Boolean isPayment = billHistoryRepository.existsByBillAndStatusDaThanhToan(bill);
            if (isPayment) {
                PaymentMethods paymentMethods = PaymentMethods
                        .builder()
                        .paymentMethod(request.getPaymentMethodEnum())
                        .paymentMethodsType(PaymentMethodsType.HOAN_TIEN)
                        .build();
                paymentMethodsRepository.save(paymentMethods);

                PaymentBill paymentBill = PaymentBill
                        .builder()
                        .payMentBillStatus(PayMentBillStatus.DA_HOAN_TIEN)
                        .paymentMethods(paymentMethods)
                        .bill(bill)
                        .notes(request.getNote())
                        .totalMoney(bill.getTotalMoney())
                        .transactionCode(request.getTransactionCode())
                        .build();
                paymentBillRepository.save(paymentBill);
            }

            // Hoàn lại số lượng sản phẩm
            if(bill.getStatus() == BillStatus.DA_XAC_NHAN) {
                List<BillDetail> billDetailsRollBack = billDetailRepository.findByBill(bill);
                for (BillDetail billDetail : billDetailsRollBack) {
                    ProductDetail productDetail = billDetail.getProductDetail();
                    productDetail.setQuantity(productDetail.getQuantity() + billDetail.getQuantity());
                    productDetailRepository.save(productDetail);
                }
            }
        }

        if(request.getStatus() == BillStatus.DA_THANH_TOAN) {
            BillHistory billHistory =   BillHistory
                    .builder()
                    .bill(bill)
                    .status(BillStatus.DA_GIAO_HANG)
                    .description(request.getNote())
                    .build();
            billHistoryRepository.save(billHistory);

            PaymentBill paymentBill = paymentBillRepository.findDistinctFirstByBill(bill);
            paymentBill.setTotalMoney(bill.getMoneyAfter());
            paymentBill.setPayMentBillStatus(PayMentBillStatus.DA_THANH_TOAN);
            paymentBillRepository.save(paymentBill);

            bill.setCustomerMoney(bill.getMoneyAfter());
        }
        //econg
        if (request.getStatus() == BillStatus.DA_HOAN_THANH) {
            List<BillDetail> billDetails = billDetailRepository.findByBill(bill);
//            log.warn(billDetails.toString());
            for (BillDetail i :
                    billDetails) {
//                log.warn(i.toString());
                ProductDetail productDetail = productDetailRepository.findById(i.getProductDetail().getId()).get();
                productDetail.setSold((productDetail.getSold()!=null?productDetail.getSold():0) + i.getQuantity());
                productDetailRepository.save(productDetail);
            }

        }

//        econg het

        if (request.getStatus() == BillStatus.DA_XAC_NHAN) {

            // Check xem t
            PaymentBill paymentBill = paymentBillRepository.findDistinctFirstByBill(bill);
            if(paymentBill.getPaymentMethods().getPaymentMethod() == PaymentMethodEnum.ZALO_PAY
                    &&
                    paymentBill.getPayMentBillStatus() == PayMentBillStatus.CHUA_THANH_TOAN
            ) {
                PaymentMethods paymentMethods = paymentMethodsRepository
                        .findByPaymentMethodsTypeAndAndPaymentMethod
                                (PaymentMethodsType.COD, PaymentMethodEnum.COD).orElse(null);

                if (paymentMethods == null) {
                    PaymentMethods paymentMethodsNew = PaymentMethods.builder()
                            .paymentMethod(PaymentMethodEnum.COD)
                            .paymentMethodsType(PaymentMethodsType.COD)
                            .status(Status.HOAT_DONG)
                            .build();
                  paymentMethods =   paymentMethodsRepository.save(paymentMethodsNew);
                }
                paymentBill.setPaymentMethods(paymentMethods);
                paymentBill.setTransactionCode(null);
                paymentBillRepository.save(paymentBill);
            }
            // Trừ số lượng sản phẩm khi xác nhận
            // Xử lí trừ số lượng san phẩm
            List<BillDetail> billDetails = billDetailRepository.findByBill(bill);
            for (BillDetail billDetail : billDetails) {
                ProductDetail productDetail = billDetail.getProductDetail();
                if(billDetail.getQuantity() > productDetail.getQuantity()) {
                    throw new RestApiException("Số lượng sản phẩm "
                            + productDetail.getProduct().getProductName()+"-"
                            +productDetail.getColor().getColorName()+"- size: "+productDetail.getSize().getSizeName()+"-" + "không đủ!", HttpStatus.BAD_REQUEST);
                }
                productDetail.setQuantity(productDetail.getQuantity() - billDetail.getQuantity());
                productDetailRepository.save(productDetail);
            }



            sendMailUpdateSanPhamAsync(bill.getEmail(), bill,
                    "Trạng thái đơn hàng",
                    "Đơn hàng của bạn đã được xác nhận");
        }
        if (request.getStatus() == BillStatus.DANG_VAN_CHUYEN) {
            sendMailUpdateSanPhamAsync(bill.getEmail(), bill,
                    "Trạng thái đơn hàng",
                    "Đơn hàng của bạn đã được giao cho đơn vị vận chuyển");
        }

        bill.setStatus(request.getStatus());
        Bill billUpdate = billRepository.save(bill);
//e công chọc vào đây

        try {
            // Gửi thông báo qua WebSocket đến customer
            // Tạo thông báo với nội dung phù hợp về việc hủy đơn hàng
            Announcement announcement = new Announcement();
            announcement.setCustomer(bill.getCustomer());
            announcement.setAnnouncementContent("Đơn hàng #" + bill.getBillCode()+"chuyển trạng thái: "+BillStatusFormatter.format(request.getStatus()));
            announcementRepository.save(announcement);

            simpMessagingTemplate.convertAndSend(
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
        BillHistory billHistory = BillHistory.builder()
                .status(billUpdate.getStatus()).
                description(request.getNote())
                .bill(billUpdate).build();
        billHistoryRepository.save(billHistory);

        return Map.of(
                "bill", billUpdate,
                "billHistory", billHistoryService.findBillHistoryResponseBuilderListByBillCode(billCode));

    }

    @Override
    @Transactional // Bên admin
    public BillResponse updateBillInfo(String billCode, UpdateBillRequest request) {
        Bill bill = billRepository.findByBillCode(billCode);
        bill.setNumberPhone(request.getCustomerPhone());
        bill.setNotes(request.getNote());
        bill.setCustomerName(request.getCustomerName());
        bill.setEmail(request.getEmail());
        sendMailUpdateSanPhamAsync(bill.getEmail(), bill, "Cập nhật thông tin đơn hàng", "Cập nhật thông tin đơn hàng thành công");
        Address newAddress = bill.getShippingAddress();
        if (newAddress == null) {
            newAddress = new Address();
        }
        newAddress.setDistrictId(request.getDistrictId());
        newAddress.setProvinceId(request.getProvinceId());
        newAddress.setSpecificAddress(request.getSpecificAddress());
        newAddress.setWardId(request.getWardId());
        bill.setShippingAddress(newAddress);
        bill.setShipMoney(request.getFeeShipping());

        if (newAddress.getId() == null) {
            addressRepository.save(newAddress);  // Lưu mới nếu chưa có ID
        }
        Staff staff = authenticationService.getStaffAuth();
        BillHistory billHistory = BillHistory
                .builder()
                .staff(staff)
                .bill(bill)
                .status(bill.getStatus())
                .description("Cập nhật thông tin khách hàng")
                .build();
        billHistoryRepository.save(billHistory);
        billRepository.save(bill);
        return convertBillToBillResponse(bill);
    }

    @Override
    public File printBillById(String billCode) {
//        Bill bill = billRepository.findByBillCode(billCode);
//        BillHistory billHistory = billHistoryRepository.findDistinctFirstByBillOrderByCreatedAtDesc(bill);
//        List<BillDetail> lstBillDetail = billDetailRepository.findByBill(bill);
        return genPdf(billCode);
    }

    private File genPdf(String billCode) {
        Bill bill = billRepository.findByBillCode(billCode);
        BillHistory billHistory = billHistoryRepository.findDistinctFirstByBillOrderByCreatedAtDesc(bill);
        List<BillDetail> lstBillDetail = billDetailRepository.findByBill(bill);
        return genHoaDon.genHoaDon(bill, lstBillDetail, billHistory);
    }

    @Override
    @Transactional
    public BillResponse createBill(CreateBillRequest request) {
        Customer customer = null;

        Staff staffAuth = auth.getStaffAuth();
        Customer customerAuth = auth.getCustomerAuth();

        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        }
        Voucher voucher = null;
        if (request.getVoucherId() != null) {
            voucher = voucherRepository.findById(request.getVoucherId()).orElse(null);
            if (voucher != null) {
                voucher.setQuantity(voucher.getQuantity() - 1);
                voucherRepository.save(voucher);
                if (voucher.getVoucherType() == VoucherType.PRIVATE) {
                    CustomerVoucher customerVoucher = customerVoucherRepository.findCustomerVouchersByVoucherAndCustomer_Id(voucher, request.getCustomerId());
                    customerVoucher.setQuantity(customerVoucher.getQuantity() - 1);
                    customerVoucherRepository.save(customerVoucher);
                }
            }
        }
        Address address = null;
        if (request.getShippingAddressId() != null) {
            address = addressRepository.findById(request.getShippingAddressId()).orElseThrow(()
                    -> new ApiException(ErrorCode.HOA_DON_NOT_FOUND));
        }


        if (request.getAddress() != null) {
            Address newAddress = Address
                    .builder()
                    .wardId(request.getAddress().getWardId())
                    .specificAddress(request.getAddress().getSpecificAddress())
                    .provinceId(request.getAddress().getProvinceId())
                    .districtId(request.getAddress().getDistrictId())
                    .customer(customer) // Khách hàng -> có thể k phải là khách hàng đăng nhập
                    .build();
            address = addressRepository.save(newAddress);
        }
        Double shippingFee = request.getShippingFee();
        if(shippingFee != null) {
            shippingFee = request.getShippingFee();
            if (request.getIsFreeShip() != null && request.getIsFreeShip()) {
                shippingFee = (double) 0;
            }
        }  else {
            shippingFee = (double) 0;
        }

        Bill bill = Bill
                .builder()
                .typeBill(request.getTypeBill())
                .customer(customer) // Cũng có thể là khách hàng đang đăng nhập hoặc chưa đăng nhập
                .customerMoney(request.getCustomerMoney()) // Tiền khách đưa cho cửa hàng => lấy ra tiền thừa = tiền khách đưa trừ cho tiền cần thanh toán
                .discountMoney(request.getDiscountMoney())
                .moneyAfter(request.getMoneyBeforeDiscount() - request.getDiscountMoney() + shippingFee)
                .totalMoney(request.getMoneyBeforeDiscount() - request.getDiscountMoney()) // Sẽ là tổng tiền hàng trừ cho giảm giá
                .moneyBeforeDiscount(request.getMoneyBeforeDiscount()) // Tiền trước khi được giảm giá
                .shipDate(request.getShipDate())
                .shipMoney(request.getShipMoney())
                .completeDate(request.getCompleteDate())
                .shippingAddress(address)
                .numberPhone(request.getNumberPhone())
                .shipMoney(request.getShipMoney())
                .isFreeShip(request.getIsFreeShip() ? request.getIsFreeShip() : false)
                .voucher(voucher)
                .build();
        // Nếu đặt hàng online thì ===

        if (customer != null) {
            bill.setEmail(customer.getEmail());
            bill.setCustomerName(customer.getFullName());
        }

        // Trường hợp người dùng ship
        if (request.getIsShipping()) {
            if (request.getIsCOD()) {
                bill.setStatus(BillStatus.DA_XAC_NHAN);
            } else {
                bill.setStatus(BillStatus.DA_XAC_NHAN);
            }
        } else {
            bill.setStatus(BillStatus.DA_HOAN_THANH);
        }

        Bill billSave = billRepository.save(bill);

        for (BillDetailRequest billDetailRequest : request.getBillDetailRequests()) {
            ProductDetail productDetail = productDetailRepository.findById(billDetailRequest.getProductDetailId()).orElse(null);
            if(bill.getStatus() == BillStatus.DA_HOAN_THANH) {
                // Cộng số lượng đã bán
                productDetail.setSold((productDetail.getSold()!=null?productDetail.getSold():0) +
                        billDetailRequest.getQuantity());

            }
            List<Image> images = imageRepository.findByProductDetail_Id(productDetail.getId());
            String imageUrl = "";
            if(images.size() > 0) {
                imageUrl = images.get(0).getUrl();
            }

            if (productDetail != null) {
                BillDetail billDetail = BillDetail
                        .builder()
                        .productDetail(productDetail)
                        .bill(billSave)
                        .price(billDetailRequest.getPrice())
                        .totalMoney(billDetailRequest.getPrice() * billDetailRequest.getQuantity())
                        .quantity(billDetailRequest.getQuantity())
                        .image(imageUrl)
                        .build();
                billDetailRepository.save(billDetail);
            }
        }

        if(request.getIsCOD()) {
            PaymentMethods cashPaymentMethods = createAndSavePaymentMethodCOD(
                    PaymentMethodEnum.COD
            );
            savePaymentBillCOD(
                    billSave,
                    cashPaymentMethods,
                    request.getCashCustomerMoney(),
                    "",
                    request.getNotes()
            );
        } else {
            if (request.getIsCashAndBank()) {
                PaymentMethods cashPaymentMethods = createAndSavePaymentMethod(
                        PaymentMethodEnum.TIEN_MAT
                );
                PaymentMethods bankPaymentMethods = createAndSavePaymentMethod(
                        PaymentMethodEnum.CHUYEN_KHOAN);
                savePaymentBill(
                        billSave,
                        cashPaymentMethods,
                        request.getCashCustomerMoney(),
                        "",
                        request.getNotes()
                );
                savePaymentBill(billSave,
                        bankPaymentMethods,
                        request.getBankCustomerMoney(),
                        request.getTransactionCode(),
                        request.getNotes());
            } else if (request.getCashCustomerMoney() != null) {
                PaymentMethods cashPaymentMethods =
                        createAndSavePaymentMethod(PaymentMethodEnum.TIEN_MAT);
                savePaymentBill(
                        billSave,
                        cashPaymentMethods,
                        request.getCashCustomerMoney(), "", request.getNotes()
                );
            } else if (request.getBankCustomerMoney() != null) {
                PaymentMethods bankPayment =
                        createAndSavePaymentMethod(PaymentMethodEnum.CHUYEN_KHOAN);
                savePaymentBill(billSave, bankPayment,
                        request.getCashCustomerMoney(), request.getTransactionCode(),
                        request.getNotes());
            }
        }
        handleSaveBillHistory(billSave, customer, staffAuth, request, customerAuth);


        return convertBillToBillResponse(billSave);
    }

    private void handleSaveBillHistory(Bill billSave,
                                       Customer customer,
                                       Staff staff,
                                       CreateBillRequest request,
                                       Customer customerAuth
    ) {
        // TH 1 Đặt hàng online
        // TH 2 MUA HÀNG TẠI QUẦY
        if (request.getIsCOD()) {
            BillHistory billHistory = BillHistory
                    .builder()
                    .customer(customer)
                    .staff(staff)
                    .bill(billSave)
                    .status(BillStatus.DA_XAC_NHAN)
                    .build();

            billHistoryRepository.save(billHistory);
            return;
        }

        BillHistory billHistory_1 = BillHistory
                .builder()
                .customer(customer)
                .staff(staff)
                .bill(billSave)
                .status(BillStatus.DA_THANH_TOAN)
                .build();
        BillHistory billHistory_2;
        if (request.getIsShipping()) {
            billHistory_2 = BillHistory
                    .builder()
                    .customer(customer)
                    .staff(staff)
                    .bill(billSave)
                    .status(BillStatus.DA_XAC_NHAN)
                    .build();
        } else {
            billHistory_2 = BillHistory
                    .builder()
                    .customer(customer)
                    .staff(staff)
                    .bill(billSave)
                    .status(BillStatus.DA_HOAN_THANH)
                    .build();
        }
        billHistoryRepository.saveAll(List.of(billHistory_1, billHistory_2));

    }

    @Override
    @Transactional
    public void updateProductQuantity(List<BillProductDetailRequest> requests) {
        requests.forEach(request -> {
            ProductDetail productDetail = productDetailRepository.
                    findById(request.getId()).orElse(null);
            if (productDetail != null) {
                productDetail.setQuantity(request.getQuantity());
                ProductDetail productDetailSave = productDetailRepository.save(productDetail);

                webSocketService.sendProductUpdate(ProductDetailResponse.fromEntity(productDetailSave));
            }
        });

    }

    @Override
    public List<VoucherReponse> getAllVoucherResponse() {
        LocalDateTime now = LocalDateTime.now();
        List<Voucher> vouchers = voucherRepository
                .findByStartDateBeforeAndEndDateAfterAndQuantityGreaterThanAndVoucherType(now, now, 0, VoucherType.PUBLIC);
        List<VoucherReponse> voucherReponses = vouchers.stream().map(voucher -> VoucherReponse.formEntity(voucher)).toList();
        return voucherReponses;
    }

    @Override
    public List<VoucherReponse> getAllVoucherResponseByCustomerId(Integer customerId) {
        List<CustomerVoucher> customerVouchers = Optional.ofNullable(
                customerVoucherRepository.findCustomerVouchersByCustomerId(customerId)
        ).orElse(List.of()); // Nếu null thì trả về danh sách rỗng tránh NullPointerException
        //
        List<Voucher> validVouchers = customerVouchers.stream()
                .map((customerVoucher -> {
                    Voucher voucher =  customerVoucher.getVoucher();
                    voucher.setQuantity(customerVoucher.getQuantity());
                    return voucher;
                }))
                .filter(voucher -> voucher.getQuantity() > 0)
                .filter(voucher -> voucher.getStartDate().isBefore(LocalDateTime.now()) || voucher.getStartDate().isEqual(LocalDateTime.now()))
                .filter(voucher -> voucher.getEndDate().isAfter(LocalDateTime.now()) || voucher.getEndDate().isEqual(LocalDateTime.now()))
                .filter(voucher -> voucher.getStatusVoucher() == StatusEnum.dang_kich_hoat)
                .collect(Collectors.toList());

        List<VoucherReponse> voucherReponses = validVouchers.stream()
                .map(voucher ->
                        VoucherReponse.formEntity(voucher))
                .collect(Collectors.toList());

        List<VoucherReponse> voucherReponsePublic = Optional.ofNullable(
                getAllVoucherResponse()
        ).orElse(List.of()); // Xử lý trường hợp billService trả về null
        // Hợp hai danh sách
        List<VoucherReponse> mergedVouchers = new ArrayList<>(voucherReponses);
        mergedVouchers.addAll(voucherReponsePublic);

        return mergedVouchers;
    }

    @Override
    public VoucherReponse updateQuantityVoucher(UpdateQuantityVoucherRequest request) {
        Voucher voucher = voucherRepository.findById(request.getId()).orElseThrow(
                () -> new RuntimeException("voucher not found"));
        voucher.setQuantity(request.getQuantity());
        Voucher voucherSave = voucherRepository.save(voucher);
        VoucherReponse voucherReponse = VoucherReponse.formEntity(voucher);
        return voucherReponse;
    }

    // Để nguyên
    private PaymentMethods createAndSavePaymentMethod(PaymentMethodEnum methodEnum) {
        PaymentMethods paymentMethods = paymentMethodsRepository
                .findByPaymentMethodsTypeAndAndPaymentMethod(PaymentMethodsType.THANH_TOAN_TRUOC,methodEnum)
                .orElseGet(() -> paymentMethodsRepository.save(PaymentMethods.builder()
                        .paymentMethod(methodEnum)
                        .paymentMethodsType(PaymentMethodsType.THANH_TOAN_TRUOC)
                        .status(Status.HOAT_DONG)
                        .build()));

        return paymentMethods;
    }

    private PaymentMethods createAndSavePaymentMethodCOD(PaymentMethodEnum methodEnum) {

        PaymentMethods paymentMethods = paymentMethodsRepository
                .findByPaymentMethodsTypeAndAndPaymentMethod(PaymentMethodsType.COD,methodEnum)
                .orElseGet(() -> paymentMethodsRepository.save(PaymentMethods.builder()
                        .paymentMethod(methodEnum)
                        .paymentMethodsType(PaymentMethodsType.COD)
                        .status(Status.HOAT_DONG)
                        .build()));

        return paymentMethods;
    }

    private void savePaymentBill(Bill bill, PaymentMethods paymentMethods,
                                 Double totalMoney,
                                 String transactionCode,
                                 String notesPayment
    ) {
        if (paymentMethods == null) return;

        PaymentBill paymentBill = PaymentBill.builder()
                .bill(bill)
                .totalMoney(totalMoney)
                .transactionCode(transactionCode)
                .notes(notesPayment)
                .paymentMethods(paymentMethods)
                .payMentBillStatus(PayMentBillStatus.DA_THANH_TOAN)
                .build();

        paymentBillRepository.save(paymentBill);
    }

    private void savePaymentBillCOD(Bill bill, PaymentMethods paymentMethods,
                                 Double totalMoney,
                                 String transactionCode,
                                 String notesPayment
    ) {
        if (paymentMethods == null) return;

        PaymentBill paymentBill = PaymentBill.builder()
                .bill(bill)
                .totalMoney(totalMoney)
                .transactionCode(transactionCode)
                .notes(notesPayment)
                .paymentMethods(paymentMethods)
                .payMentBillStatus(PayMentBillStatus.CHUA_THANH_TOAN)
                .build();

        paymentBillRepository.save(paymentBill);
    }



    private BillResponse convertBillToBillResponse(Bill bill) {
        AddressReponse addressReponse;
        if (bill.getShippingAddress() == null) {
            addressReponse = null;
        } else {
            addressReponse = new AddressReponse(bill.getShippingAddress());
        }
        VoucherReponse voucher = null;
        if (bill.getVoucher() != null) {
            voucher = VoucherReponse.formEntity(bill.getVoucher());
        }

        return BillResponse.builder()
                .billCode(bill.getBillCode())
                .customerName(bill.getCustomerName())
                .customerPhone(bill.getNumberPhone())
                .customerMoney(bill.getCustomerMoney())
                .discountMoney(bill.getDiscountMoney())
                .shipMoney(bill.getShipMoney())
                .totalMoney(bill.getTotalMoney())
                .moneyBeforeDiscount(bill.getMoneyBeforeDiscount())
                .billType(bill.getTypeBill().toString())
                .notes(bill.getNotes())
                .completeDate(bill.getCompleteDate())
                .confirmDate(bill.getConfirmDate())
                .desiredDateOfReceipt(bill.getDesiredDateOfReceipt())
                .shipDate(bill.getShipDate())
                .address(bill.getShippingAddress())
                .addressReponse(addressReponse)
                .email(bill.getEmail())
                .voucherReponse(voucher)
                .isFreeShip(bill.getIsFreeShip())
                .status(bill.getStatus() != null ? bill.getStatus().toString() : null)
                .createAt(bill.getCreatedAt())
                .moneyAfter(bill.getMoneyAfter())
                .build();
    }

    @Override
    public List<Map<String, Object>> getBillCountByStatus() {
        List<Object[]> results = billRepository.countOrdersByStatus();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] result : results) {
            BillStatus status = (BillStatus) result[0];
            Long count = (Long) result[1];

            Map<String, Object> map = new HashMap<>();
            map.put("name", status.name());
            map.put("value", count);
            response.add(map);
        }
        Long countAll = billRepository.count();
        response.add(Map.of("name", "all", "value", countAll));
        return response;
    }

    @Transactional
    @Override
    public void updateProductBill(String billCode, UpdateProductBillRequest request) {
        try {
            // 1️⃣ Lấy thông tin hóa đơn và cập nhật các thông tin cơ bản
            Bill bill = billRepository.findByBillCode(billCode);
            Voucher voucher = voucherRepository.findVoucherByVoucherCode(request.getVoucherCode());
            bill.setTotalMoney(request.getTotalMoney());
            bill.setMoneyBeforeDiscount(request.getMoneyBeforeDiscount());
            bill.setDiscountMoney(request.getDiscountMoney());
            bill.setShipMoney(request.getShipMoney());
            bill.setIsFreeShip(request.getIsFreeShip());
            bill.setVoucher(voucher);
            if(request.getIsFreeShip() != null && request.getIsFreeShip()) {
                bill.setIsFreeShip(true);
                bill.setMoneyAfter(request.getMoneyBeforeDiscount() - request.getDiscountMoney());  // Tiền khach can thanh toan
            }else {
                bill.setIsFreeShip(false);
                bill.setMoneyAfter(request.getMoneyBeforeDiscount() + request.getShipMoney() - request.getDiscountMoney());  // Tiền khach can thanh toan
            }

            // 2️⃣ Ghi lịch sử cập nhật đơn hàng
            BillHistory billHistory = BillHistory
                    .builder()
                    .bill(bill)
                    .status(bill.getStatus())
                    .staff(authenticationService.getStaffAuth())
                    .description("Thay đổi sản phẩm trong đơn hàng")
                    .build();
            billHistoryRepository.save(billHistory);

            // 3️⃣ Lấy danh sách sản phẩm trong hóa đơn
            List<BillDetail> billDetails = billDetailRepository.findByBillId(bill.getId());
            List<BillProductDetailRequest> productDetailRequestList = request.getProductDetailRequestList();

            // 🔹 Chuyển danh sách sản phẩm hiện tại thành Map có key là "productDetailId-price"
            Map<String, BillDetail> billDetailMap = billDetails.stream()
                    .collect(Collectors.toMap(
                            billDetail -> billDetail.getProductDetail().getId() + "-" + billDetail.getPrice(),
                            Function.identity()
                    ));

            // 🔹 Chuyển danh sách sản phẩm trong request thành Map có key là "productDetailId-price"
            Map<String, BillProductDetailRequest> requestMap = productDetailRequestList.stream()
                    .collect(Collectors.toMap(
                            requestDetail -> requestDetail.getProductDetailId() + "-" + requestDetail.getPrice(),
                            Function.identity()
                    ));

            List<BillDetail> toUpdate = new ArrayList<>();
            List<BillDetail> toDelete = new ArrayList<>();
            List<BillDetail> toInsert = new ArrayList<>();

            // 4️⃣ Kiểm tra sản phẩm có trong DB nhưng không có trong request → XÓA
            for (BillDetail billDetail : billDetails) {
                String key = billDetail.getProductDetail().getId() + "-" + billDetail.getPrice();
                if (!requestMap.containsKey(key)) {
                    toDelete.add(billDetail);
                }
            }

            // 5️⃣ Kiểm tra sản phẩm có trong cả hai danh sách → CẬP NHẬT hoặc THÊM MỚI
            for (BillProductDetailRequest requestDetail : productDetailRequestList) {
                String key = requestDetail.getProductDetailId() + "-" + requestDetail.getPrice();
                BillDetail existingBillDetail = billDetailMap.get(key);
                ProductDetail productDetail = productDetailRepository.findById(requestDetail.getProductDetailId())
                        .orElseThrow(() -> new RestApiException("Product detail not found", HttpStatus.NOT_FOUND));

                if (existingBillDetail != null) {
                    // Nếu có thay đổi về số lượng, cập nhật
                    if (!existingBillDetail.getQuantity().equals(requestDetail.getQuantity())) {
                        existingBillDetail.setQuantity(requestDetail.getQuantity());
                        existingBillDetail.setTotalMoney(requestDetail.getPrice() * requestDetail.getQuantity());
                        toUpdate.add(existingBillDetail);
                    }
                } else {
                    // Nếu sản phẩm chưa tồn tại, thêm mới
                    BillDetail newBillDetail = new BillDetail();
                    newBillDetail.setBill(bill);
                    newBillDetail.setProductDetail(productDetail);
                    newBillDetail.setQuantity(requestDetail.getQuantity());
                    newBillDetail.setPrice(requestDetail.getPrice());
                    newBillDetail.setTotalMoney(requestDetail.getPrice() * requestDetail.getQuantity());
                    toInsert.add(newBillDetail);
                }
            }

            // 6️⃣ Thực hiện thao tác với repository
            billDetailRepository.deleteAll(toDelete);
            billDetailRepository.saveAll(toUpdate);
            billDetailRepository.saveAll(toInsert);

            // 7️⃣ Gửi email thông báo cập nhật hóa đơn
            sendMailUpdateSanPhamAsync(bill.getEmail(), bill, "Cập nhật hóa đơn",
                    "Thay đổi số lượng sản phẩm thành công");

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RestApiException("Update product failed", HttpStatus.BAD_REQUEST);
        }
    }


    public CompletableFuture<Void> sendMailUpdateSanPhamAsync(String sendToMail, Bill bill,
                                                              String title, String subTitle) {
        return CompletableFuture.runAsync(() -> sendMail(sendToMail, bill, title, subTitle));
    }


    private void sendMail(String sendToMail, Bill bill,
                          String title, String subTitle) {
//        File pdfFile = genPdf(bill.getBillCode());
        Email email = new Email();
        String[] emailSend = {sendToMail};
        email.setToEmail(emailSend);
        email.setSubject("TheHands-" + title);
        email.setTitleEmail("");
//        email.setPdfFile(pdfFile);
//        email.setFileName(bill.getBillCode());
        // Tạo nội dung email
        email.setBody("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Hóa đơn TheHands</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 50px;\">\n" +
                "    <div style=\"max-width: 600px; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); margin: auto;\">\n" +
                "        <h2 style=\"color: #333;\">🎉 " + subTitle + " 🎉</h2>\n" +
                "        <p style=\"color: #555;\">Cảm ơn bạn đã mua hàng tại <strong>TheHands</strong>. Dưới đây là thông tin đơn hàng của bạn:</p>\n" +
                "        <hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\">\n" +
                "        <p><strong>📧 Email:</strong> " + sendToMail + "</p>\n" +
                "        <p><strong>🧾 Mã hóa đơn:</strong> <span style=\"color: #007bff; font-weight: bold;\">" + bill.getBillCode() + "</span></p>\n" +
                "        <hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\">\n" +
                "        <h3 style=\"color: #007bff;\">Danh sách sản phẩm</h3>\n" +
                "        <hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\">\n" +
                "        <p style=\"color: #555;\">Bạn có thể kiểm tra hóa đơn của mình bằng cách nhấn vào nút bên dưới:</p>\n" +
                "        <a href=\"http://localhost:5173/searchbill?billcode=" + bill.getBillCode() + "\" style=\"display: inline-block; background-color: #007bff; color: #ffffff; padding: 12px 20px; border-radius: 5px; text-decoration: none; font-weight: bold;\">🔍 Xem hóa đơn</a>\n" +
                "        <p style=\"margin-top: 20px; font-size: 12px; color: #999;\">Nếu bạn không thực hiện giao dịch này, vui lòng bỏ qua email này.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>");

        emailSender.sendEmail(email);
    }


}
