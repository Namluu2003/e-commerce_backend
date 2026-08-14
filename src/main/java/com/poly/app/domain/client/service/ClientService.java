package com.poly.app.domain.client.service;

import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.domain.admin.product.response.gender.GenderResponse;
import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.domain.admin.product.response.size.SizeResponse;
import com.poly.app.domain.admin.product.response.sole.SoleResponse;
import com.poly.app.domain.client.request.AddCart;
import com.poly.app.domain.client.request.CreateBillClientRequest;
import com.poly.app.domain.client.response.*;
import com.poly.app.domain.common.ApiResponse;
import com.poly.app.domain.model.Voucher;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClientService {
    Page<ProductViewResponse> getAllProductHadPromotion(int page, int size);

    Page<ProductViewResponse> getAllProductHadSoleDesc(int page, int size);

    Page<ProductViewResponse> getAllProductHadCreatedAtDesc(int page, int size);

    Page<ProductViewResponse> getAllProductHadViewsDesc(int page, int size);

//    trang detail từng sản phẩm

    ProductDetailResponse findProductDetailbyProductIdAndColorIdAndSizeId(int productId, int colorId, int sizeId, int genderId,int materialId,int soleId);

    List<SizeResponse> findSizesByProductId(Integer productId);

    List<SizeResponse> findSizesByProductIdAndColorId(Integer productId, Integer colorId,Integer soleId, Integer materialId, Integer genderId);

    List<ColorResponse> findColorsByProductId(Integer productId);

    Integer addViewProduct(int productId);

    String createBillClient(CreateBillClientRequest request);

    Page<CartResponse> getAllCartCustomerId(Integer customerId, Integer page, Integer size);

    CartResponse addProductToCart(AddCart addCart);

    String deleteCartById(Integer cartDetailId);

    List<CartResponse> getAllByCustomserIdNopage(Integer customerId);

    //    tìm voucher valid
    VoucherBestResponse voucherBest(String customerId, String billValue);

    List<Voucher> findValidVouchers(String customerId);

    Integer plus(Integer id);

    Integer subtract(Integer id);

    Integer setQuantityCart(Integer id, Integer quantity);

    List<RealPriceResponse> getRealPrice(List<AddCart> addCartList);

    Optional<Object> findAdressDefaulCustomerId(Integer customerId);

    SearchStatusBillResponse searchBill(String billCode);

    String veritifyBill(String billCode);

    ApiResponse<List<SearchStatusBillResponse>> getAllBillOfCustomerid(Integer customerId, Integer page, Integer size);

    String cancelBill(Integer id, String description) throws Exception;

    String buyBack(Integer billId, Integer customerId);

    Page<ProductViewResponse> findFilteredProducts(Long productId, Long brandId, Long genderId, Long typeId, Long colorId, Long materialId,
                                                   Double minPrice,
                                                   Double maxPrice,
                                                   int page, int size);

    Map<String, Object> refund(Integer billId, Integer MoneyRefund, String description) throws Exception;

    List<ProductDetailDiscountDTO> getDiscountedProductDetails(Integer productId, Integer colorId, Integer genderId);

    PromotionView getPromotionView(Integer productId, Integer colorId, Integer genderId);

    Boolean hasBought(Integer customerId, Integer productId);

    //    thêm
    List<GenderResponse> findGenderByProductId(Integer productId);

    List<MaterialResponse> findMaterialByProductId(Integer productId);

    List<SoleResponse> findSoleByProductId(Integer productId);

    List<ColorResponse> findColorByProductIdAndSoleId(Integer productId,Integer soleId, Integer materialId, Integer genderId);

    List<SoleResponse> findSoleByProductIdAndMaterialId(Integer productId,Integer materialId,Integer genderId);

    List<MaterialResponse> findMaterialByProductIdAndGenderId(Integer productId,Integer genderId);


}
