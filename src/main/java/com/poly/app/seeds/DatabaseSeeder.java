package com.poly.app.seeds;


import com.poly.app.domain.model.Announcement;
import com.poly.app.domain.model.Bill;
import com.poly.app.domain.model.BillDetail;
import com.poly.app.domain.model.BillHistory;
import com.poly.app.domain.model.Brand;
import com.poly.app.domain.model.Color;
import com.poly.app.domain.model.Customer;
import com.poly.app.domain.model.Gender;
import com.poly.app.domain.model.Image;
import com.poly.app.domain.model.Material;
import com.poly.app.domain.model.PaymentMethods;
import com.poly.app.domain.model.Product;
import com.poly.app.domain.model.ProductDetail;
import com.poly.app.domain.model.Role;
import com.poly.app.domain.model.Size;
import com.poly.app.domain.model.Sole;
import com.poly.app.domain.model.Staff;
import com.poly.app.domain.model.StatusEnum;
import com.poly.app.domain.model.Type;
import com.poly.app.domain.model.Voucher;
import com.poly.app.infrastructure.constant.AccountStatus;
import com.poly.app.infrastructure.constant.DiscountType;
import com.poly.app.infrastructure.constant.PaymentMethodsType;
import com.poly.app.infrastructure.constant.Status;
import com.poly.app.infrastructure.constant.VoucherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.poly.app.domain.repository.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    GenderRepository genderRepository;
    @Autowired
    AnnouncementRepository announcementRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    SoleRepository soleRepository;
    @Autowired
    TypeRepository typeRepository;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    MaterialRepository materialRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    PaymentMethodsRepository paymentMethodsRepository;
    @Autowired
    PaymentBillRepository paymentBillRepository;
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    CustomerVoucherRepository customerVoucherRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {

        if(customerRepository.count() > 0) {
            return;
        }

        Role role = Role.builder().roleName("x").build();
        roleRepository.save(role);

        Role role1 = Role.builder().roleName("ROLE_STAFF").build();
        roleRepository.save(role1);

        Role role2 = Role.builder().roleName("ROLE_MANAGER").build();
        roleRepository.save(role2);

        Role role3 = Role.builder().roleName("ROLE_STAFF_SALE").build();
        roleRepository.save(role3);
        Gender genderMale = Gender.builder()
                .genderCode("M")
                .genderName("Nam")
                .status(Status.HOAT_DONG) // Status 1 có thể đại diện cho "active"
                .build();
        genderRepository.save(genderMale);

        Gender genderFemale = Gender.builder()
                .genderCode("F")
                .genderName("Nữ")
                .status(Status.HOAT_DONG)
                .build();
        genderRepository.save(genderFemale);

        Gender genderOther = Gender.builder()
                .genderCode("O")
                .genderName("Nam/Nữ")
                .status(Status.HOAT_DONG)
                .build();
        genderRepository.save(genderOther);

        Customer customer1 = Customer.builder()
                .code("CUST001")
                .fullName("Nguyễn Văn A")
                .dateBirth(LocalDateTime.now()) // Ngày sinh (dạng timestamp), ví dụ: 01/01/1990
                .CitizenId("038204037764")
                .phoneNumber("0365345875")
                .email("nguyenvana@example.com")
                .gender(true) // true = Nam, false = Nữ
                .password(passwordEncoder.encode("123")) // Nên mã hóa mật khẩu khi lưu thực tế
                .avatar("avatar1.jpg")
                .status(AccountStatus.HOAT_DONG) // Đã kích hoạt tài khoản
                .build();
        customerRepository.save(customer1);

        Customer customer2 = Customer.builder()
                .code("CUST002")
                .fullName("Trần Thị B")
                .dateBirth(LocalDateTime.now()) // Ngày sinh: 01/01/2000
                .CitizenId("9876543210")
                .phoneNumber("0987654321")
                .email("tranthib@example.com")
                .gender(false)
                .password(passwordEncoder.encode("123"))
                .avatar("avatar2.jpg")
                .status(AccountStatus.CHUA_KICH_HOAT) // Tài khoản chưa kích hoạt
                .build();
        customerRepository.save(customer2);

        Customer customerExits = customerRepository.findById(1).orElseThrow(() ->
                new RuntimeException("Customer with ID 1 not found"));

        Announcement announcement1 = Announcement.builder()
                .customer(customerExits)
                .announcementCode("ANN001")
                .announcementContent("Thông báo khuyến mãi 50% cho khách hàng VIP!")
                .build();
        announcementRepository.save(announcement1);

        Announcement announcement2 = Announcement.builder()
                .customer(customerExits)
                .announcementCode("ANN002")
                .announcementContent("Chúc mừng bạn đã nhận được voucher giảm giá 100k!")
                .build();
        announcementRepository.save(announcement2);
        Color color1 = Color.builder().colorName("Trắng").status(Status.HOAT_DONG).build();
        Color color2 = Color.builder().colorName("Đen").status(Status.HOAT_DONG).build();
        Color color3 = Color.builder().colorName("Hồng").status(Status.HOAT_DONG).build();
        Color color4 = Color.builder().colorName("Cam").status(Status.HOAT_DONG).build();
        Color color5 = Color.builder().colorName("Xám").status(Status.HOAT_DONG).build();
        Color color01 = colorRepository.save(color1);
        color01.setCode("#FFFFFF");
        colorRepository.save(color01);
        color2.setCode("#000000");
        colorRepository.save(color2);
        color3.setCode("#FFC0CB");
        colorRepository.save(color3);
        color4.setCode("#FFA500");
        colorRepository.save(color4);
        color5.setCode("#808080");
        colorRepository.save(color5);





        Size size1 = Size.builder().sizeName("36").status(Status.HOAT_DONG).build();
        Size size2 = Size.builder().sizeName("37").status(Status.HOAT_DONG).build();
        Size size3 = Size.builder().sizeName("38").status(Status.HOAT_DONG).build();
        Size size4 = Size.builder().sizeName("39").status(Status.HOAT_DONG).build();
        Size size5 = Size.builder().sizeName("40").status(Status.HOAT_DONG).build();
        // Save data to the database
        sizeRepository.save(size1);
        sizeRepository.save(size2);
        sizeRepository.save(size3);
        sizeRepository.save(size4);
        sizeRepository.save(size5);
        Sole sole1 = Sole.builder().soleName("Gỗ").status(Status.HOAT_DONG).build();
        Sole sole2 = Sole.builder().soleName("Cao su").status(Status.HOAT_DONG).build();
        Sole sole3 = Sole.builder().soleName("Nhựa").status(Status.HOAT_DONG).build();
        Sole sole4 = Sole.builder().soleName("Sốp").status(Status.HOAT_DONG).build();
        soleRepository.save(sole1);
        soleRepository.save(sole2);
        soleRepository.save(sole3);
        soleRepository.save(sole4);
        Type type1 = Type.builder().typeName("Thể thao").status(Status.HOAT_DONG).build();
        Type type2 = Type.builder().typeName("Thời trang").status(Status.HOAT_DONG).build();
        Type type3 = Type.builder().typeName("Chạy bộ").status(Status.HOAT_DONG).build();
        // Save data to the database
        typeRepository.save(type1);
        typeRepository.save(type2);
        typeRepository.save(type3);
        // Add seed data
        Brand brand1 = Brand.builder().brandName("Nike").status(Status.HOAT_DONG).build();
        Brand brand2 = Brand.builder().brandName("Adidas").status(Status.HOAT_DONG).build();
        Brand brand3 = Brand.builder().brandName("Puma").status(Status.HOAT_DONG).build();
        Brand brand4 = Brand.builder().brandName("Rick Owen").status(Status.HOAT_DONG).build();
        Brand brand5 = Brand.builder().brandName("New Balance").status(Status.HOAT_DONG).build();
        // Save data to the database
        brandRepository.save(brand1);
        brandRepository.save(brand2);
        brandRepository.save(brand3);
        brandRepository.save(brand4);
        brandRepository.save(brand5);
        // Add seed data
        Material material1 = Material.builder().materialName("Cao su").status(Status.HOAT_DONG).build();
        Material material2 = Material.builder().materialName("Vải").status(Status.HOAT_DONG).build();
        Material material3 = Material.builder().materialName("Da").status(Status.HOAT_DONG).build();
        Material material4 = Material.builder().materialName("Tổng hợp").status(Status.HOAT_DONG).build();
        Material material5 = Material.builder().materialName("Lưới").status(Status.HOAT_DONG).build();


        // Save data to the database
        materialRepository.save(material1);
        materialRepository.save(material2);
        materialRepository.save(material3);
        materialRepository.save(material4);
        materialRepository.save(material5);


        Product product1 = Product.builder().productName("Nike Air Max").status(Status.HOAT_DONG).build(); //màu trắng
        Product product2 = Product.builder().productName("Adidas UltraBoost").status(Status.HOAT_DONG).build();//màu hồng
        Product product3 = Product.builder().productName("Puma RS-X3").status(Status.HOAT_DONG).build();//màu cam
        Product product4 = Product.builder().productName("Rick Owen").status(Status.HOAT_DONG).build();//đen
        Product product5 = Product.builder().productName("New Balance 574").status(Status.HOAT_DONG).build();//xám

        // Save data to the database
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);

// 1. Nike Air Max 90 - Trắng
        ProductDetail productDetail6 = ProductDetail.builder()
                .product(product1) // Nike Air Max
                .brand(brand1) // Nike
                .type(type1) // Thể thao
                .color(color1) // Trắng
                .material(material4) // Tổng hợp
                .size(size4) // 39
                .sole(sole2) // Cao su
                .gender(genderMale) // Nam
                .productDetailCode("PD006")
                .quantity(120)
                .price(3200000.0)
                .weight(0.50)
                .descrition("Nike Air Max 90 với thiết kế kinh điển, đệm khí êm ái, mang lại sự thoải mái tối đa cho các hoạt động thể thao hoặc sử dụng hàng ngày. Màu trắng tinh tế, dễ dàng phối đồ.")
                .status(Status.HOAT_DONG)
                .build();
        productDetailRepository.save(productDetail6);

// 2. Rick Owens DRKSHDW - Đen
        ProductDetail productDetail7 = ProductDetail.builder()
                .product(product4) // Rick Owens
                .brand(brand4) // Rick Owens
                .type(type2) // Thời trang
                .color(color2) // Đen
                .material(material3) // Da
                .size(size5) // 40
                .sole(sole2) // Cao su
                .gender(genderOther) // Nam/Nữ
                .productDetailCode("PD007")
                .quantity(50)
                .price(12500000.0)
                .weight(0.65)
                .descrition("Rick Owens DRKSHDW mang phong cách avant-garde độc đáo, màu đen huyền bí, thiết kế đế dày mạnh mẽ. Phù hợp với những ai yêu thích thời trang cao cấp và cá tính.")
                .status(Status.HOAT_DONG)
                .build();
        productDetailRepository.save(productDetail7);

// 3. Adidas Ultraboost Wonder Taupe - Hồng
        ProductDetail productDetail8 = ProductDetail.builder()
                .product(product2) // Adidas Ultraboost
                .brand(brand2) // Adidas
                .type(type3) // Chạy bộ
                .color(color3) // Hồng
                .material(material5) // Lưới
                .size(size3) // 38
                .sole(sole2) // Cao su
                .gender(genderFemale) // Nữ
                .productDetailCode("PD008")
                .quantity(100)
                .price(4800000.0)
                .weight(0.40)
                .descrition("Adidas Ultraboost Wonder Taupe với sắc hồng phấn thời thượng, công nghệ đệm Boost mang lại cảm giác êm ái và phản hồi năng lượng vượt trội, lý tưởng cho chạy bộ hoặc phong cách năng động.")
                .status(Status.HOAT_DONG)
                .build();
        productDetailRepository.save(productDetail8);

// 4. Puma RS-Trck Metallic - Cam
        ProductDetail productDetail9 = ProductDetail.builder()
                .product(product3) // Puma RS-X3
                .brand(brand3) // Puma
                .type(type2) // Thời trang
                .color(color4) // Cam
                .material(material4) // Tổng hợp
                .size(size2) // 37
                .sole(sole3) // Nhựa
                .gender(genderMale) // Nam
                .productDetailCode("PD009")
                .quantity(80)
                .price(2900000.0)
                .weight(0.48)
                .descrition("Puma RS-Trck Metallic nổi bật với sắc cam rực rỡ, thiết kế chunky thời thượng, phù hợp với phong cách đường phố. Đệm RS êm ái, hỗ trợ tốt cho các hoạt động hàng ngày.")
                .status(Status.HOAT_DONG)
                .build();
        productDetailRepository.save(productDetail9);

// 5. New Balance 574 - Xám
        ProductDetail productDetail10 = ProductDetail.builder()
                .product(product5) // New Balance 574
                .brand(brand5) // New Balance
                .type(type2) // Thời trang
                .color(color5) // Xám
                .material(material3) // Da
                .size(size1) // 36
                .sole(sole2) // Cao su
                .gender(genderMale) // Nam
                .productDetailCode("PD010")
                .quantity(150)
                .price(2500000.0)
                .weight(0.45)
                .descrition("New Balance 574 với màu xám trung tính, thiết kế cổ điển kết hợp đệm ENCAP êm ái, phù hợp cho cả phong cách casual lẫn hoạt động nhẹ. Một lựa chọn hoàn hảo cho sự thoải mái và tinh tế.")
                .status(Status.HOAT_DONG)
                .build();
        productDetailRepository.save(productDetail10);

// 6. Nike Air Force 1 Low - Trắng
        ProductDetail productDetail11 = ProductDetail.builder()
                .product(product1) // Nike Air Max
                .brand(brand1) // Nike
                .type(type2) // Thời trang
                .color(color1) // Trắng
                .material(material3) // Da
                .size(size5) // 40
                .sole(sole2) // Cao su
                .gender(genderMale) // Nam
                .productDetailCode("PD011")
                .quantity(200)
                .price(2800000.0)
                .weight(0.52)
                .descrition("Nike Air Force 1 Low với màu trắng vượt thời gian, thiết kế đơn giản nhưng mang tính biểu tượng. Đệm Air êm ái, phù hợp cho phong cách đường phố và sử dụng hàng ngày.")
                .status(Status.HOAT_DONG)
                .build();
        productDetailRepository.save(productDetail11);

// 7. Adidas Superstar - Trắng
        ProductDetail productDetail12 = ProductDetail.builder()
                .product(product2) // Adidas Ultraboost
                .brand(brand2) // Adidas
                .type(type2) // Thời trang
                .color(color1) // Trắng
                .material(material3) // Da
                .size(size4) // 39
                .sole(sole2) // Cao su
                .gender(genderFemale) // Nữ
                .productDetailCode("PD012")
                .quantity(130)
                .price(2200000.0)
                .weight(0.47)
                .descrition("Adidas Superstar với thiết kế mũi giày shell-toe kinh điển, màu trắng tinh khôi. Phù hợp với phong cách casual, dễ dàng phối đồ cho mọi dịp.")
                .status(Status.HOAT_DONG)
                .build();
        productDetailRepository.save(productDetail12);

// 8. Puma Suede Classic - Đen
        ProductDetail productDetail13 = ProductDetail.builder()
                .product(product3) // Puma RS-X3
                .brand(brand3) // Puma
                .type(type2) // Thời trang
                .color(color2) // Đen
                .material(material3) // Da
                .size(size3) // 38
                .sole(sole2) // Cao su
                .gender(genderMale) // Nam
                .productDetailCode("PD013")
                .quantity(90)
                .price(2000000.0)
                .weight(0.46)
                .descrition("Puma Suede Classic với chất liệu da lộn cao cấp, màu đen sang trọng. Thiết kế đơn giản, phù hợp cho phong cách thời trang đường phố hoặc sự kiện casual.")
                .status(Status.HOAT_DONG)
                .build();
        productDetailRepository.save(productDetail13);

// 9. Rick Owens Low Sneaker - Đen
        ProductDetail productDetail14 = ProductDetail.builder()
                .product(product4) // Rick Owens
                .brand(brand4) // Rick Owens
                .type(type2) // Thời trang
                .color(color2) // Đen
                .material(material3) // Da
                .size(size2) // 37
                .sole(sole2) // Cao su
                .gender(genderOther) // Nam/Nữ
                .productDetailCode("PD014")
                .quantity(40)
                .price(10500000.0)
                .weight(0.60)
                .descrition("Rick Owens Low Sneaker với thiết kế tối giản, màu đen thời thượng, mang đậm phong cách high-fashion. Lý tưởng cho những ai muốn thể hiện cá tính mạnh mẽ.")
                .status(Status.HOAT_DONG)
                .build();
        productDetailRepository.save(productDetail14);

// 10. New Balance Fresh Foam - Xám
        ProductDetail productDetail15 = ProductDetail.builder()
                .product(product5) // New Balance 574
                .brand(brand5) // New Balance
                .type(type3) // Chạy bộ
                .color(color4) // Xám
                .material(material5) // Lưới
                .size(size4) // 39
                .sole(sole3) // Cao su
                .gender(genderMale) // Nam
                .productDetailCode("PD015")
                .quantity(110)
                .price(3200000.0)
                .weight(0.42)
                .descrition("New Balance Fresh Foam với công nghệ đệm êm ái, màu xám hiện đại. Phù hợp cho chạy bộ hoặc các hoạt động thể thao nhẹ, mang lại sự thoải mái tối ưu.")
                .status(Status.HOAT_DONG)
                .build();
        productDetailRepository.save(productDetail15);

// Lưu hình ảnh cho các ProductDetail
        List<String> imageUrls = Arrays.asList(
                "https://static.nike.com/a/images/t_PDP_936_v1/f_auto,q_auto:eco/fb902d98-985a-4968-8427-1cea006d12ee/WMNS+AIR+MAX+90.png", // Nike Trắng
                "https://bdsneaker.vn/uploads/source/rickowen-.png", // Rick Owens Đen
                "https://sneakerholicvietnam.vn/wp-content/uploads/2023/11/adidas-ultraboost-1-0-wonder-taupe-hq3855.jpg", // Adidas Hồng
                "https://cany.vn/image/catalog/tpa/19823/RS-Trck-Metallic-Sneakers_75.jpg", // Puma Cam
                "https://sneakerdaily.vn/wp-content/uploads/2023/08/httpssneakerdaily.vnsan-phamgiay-new-balance-574-grey-off-white-u574ul2.png" // New Balance Xám
        );

// Gán hình ảnh cho các ProductDetail
        List<ProductDetail> productDetails = Arrays.asList(
                productDetail6, productDetail7, productDetail8, productDetail9, productDetail10,
                productDetail11, productDetail12, productDetail13, productDetail14, productDetail15
        );

        for (int i = 0; i < productDetails.size(); i++) {
            Image image = Image.builder()
                    .productDetail(productDetails.get(i))
                    .url(imageUrls.get(Math.min(i, imageUrls.size() - 1))) // Sử dụng URL tương ứng hoặc mặc định nếu vượt quá
                    .isDefault(true)
                    .build();
            imageRepository.save(image);
        }
//        for (ProductDetail productDetail : productDetailRepository.findAll()) {
//            Image image = Image.builder().productDetail(productDetail)
//                    .url(imageUrls.get(imageUrls.size() - 1))
//                    .isDefault(true)
//                    .build();
//            imageRepository.save(image);
//        }
//
        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN");
        Role userRole = roleRepository.findByRoleName("ROLE_STAFF");

        // Nếu chưa có role, tạo mới
        if (adminRole == null) {
            adminRole = roleRepository.save(new Role("ROLE_ADMIN", Status.HOAT_DONG));
        }
        if (userRole == null) {
            userRole = roleRepository.save(new Role("USER", Status.HOAT_DONG));
        }

        // Tạo và lưu Staff
        Staff staff1 = Staff.builder()
                .code("STF001")
                .fullName("admin@fpt.com")
                .dateBirth(LocalDateTime.now()) // Ngày sinh (dạng timestamp)
                .citizenId("1234567890")
                .phoneNumber("0901234567")
                .email("admin@fpt.com")
                .gender(true) // Nam
                .password(passwordEncoder.encode("123")) // Mã hóa mật khẩu
                .avatar("avatar1.jpg")
                .status(AccountStatus.HOAT_DONG) // Đã kích hoạt tài khoản
                .role(adminRole) // Vai trò ADMIN
                .build();

        staffRepository.save(staff1);

        Staff staff2 = Staff.builder()
                .code("STF002")
                .fullName("Nhân viên A")
                .dateBirth(LocalDateTime.now()) // Ngày sinh (dạng timestamp)
                .citizenId("0987654321")
                .phoneNumber("0987654321")
                .email("nhanvien1@example.com")
                .gender(false) // Nữ
                .password(passwordEncoder.encode("123456"))
                .avatar("avatar2.jpg")
                .status(AccountStatus.CHUA_KICH_HOAT) // Tài khoản chưa kích hoạt
                .role(userRole) // Vai trò USER
                .build();

        staffRepository.save(staff2);


        PaymentMethods paymentMethod1 = PaymentMethods.builder()
                .paymentMethodsType(PaymentMethodsType.THANH_TOAN_TRUOC)
                .status(Status.HOAT_DONG) // Trạng thái (1: Hoạt động, 0: Không hoạt động)
                .build();


        paymentMethodsRepository.save(paymentMethod1);

        PaymentMethods paymentMethod2 = PaymentMethods.builder()
                .paymentMethodsType(PaymentMethodsType.THANH_TOAN_TRUOC)
                .status(Status.HOAT_DONG)
                .build();
        paymentMethodsRepository.save(paymentMethod2);

        PaymentMethods paymentMethod3 = PaymentMethods.builder()
                .paymentMethodsType(PaymentMethodsType.COD)
                .status(Status.HOAT_DONG) // Trạng thái không hoạt động
                .build();
        paymentMethodsRepository.save(paymentMethod3);




        Voucher voucher1 = Voucher.builder()
                .voucherCode("VOUCHER01")
                .statusVoucher(StatusEnum.dang_kich_hoat)
                .voucherType(VoucherType.PUBLIC)
                .discountType(DiscountType.MONEY)
                .discountValue(10.0) // Giảm 10%
                .discountMaxValue(50.0) // Giảm tối đa 50.000đ
                .billMinValue(200.0) // Giá trị hóa đơn tối thiểu 200.000đ
                .startDate(LocalDateTime.now()) // Ngày bắt đầu là ngày hiện tại
                .endDate(LocalDateTime.now()) // Ngày kết thúc là Status.HOAT_DONG ngày sau
                .build();

        Voucher voucher2 = Voucher.builder()
                .voucherCode("VOUCHER02")
                .statusVoucher(StatusEnum.dang_kich_hoat)
                .voucherType(VoucherType.PUBLIC)
                .discountType(DiscountType.MONEY)
                .discountValue(200000.0) // Giảm 200.000đ
                .discountMaxValue(200.0)
                .billMinValue(500.0) // Hóa đơn tối thiểu 500.000đ
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now()) // Status.HOAT_DONG ngày sau
                .build();

        // Lưu các phiếu giảm giá vào cơ sở dữ liệu
        voucherRepository.save(voucher1);
        voucherRepository.save(voucher2);
    }

}