package com.poly.app.domain.admin.Statistical.Service;

import com.poly.app.domain.admin.Statistical.Repository.BestSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BestSaleServie {
    @Autowired
     BestSaleRepository bestSaleRepository;

    // ✅ Phương thức chung để chuyển dữ liệu sang BestSaleDTO
    private List<BestSaleDTO> mapToBestSaleDTOList(List<Object[]> results) {
        return results.stream()
                .map(row -> new BestSaleDTO(
                        (String) row[0],  // product_name
                        (String) row[1],  // brand_name
                        (String) row[2],  // type_name
                        (String) row[3],  // color_name
                        (String) row[4],  // material_name
                        (String) row[5],  // size_name
                        (String) row[6],  // sole_name
                        (String) row[7],  // gender_name
                        ((Number) row[8]).intValue(), // total_quantity_sold
                        ((Number) row[9]).doubleValue() // price
                ))
                .collect(Collectors.toList());
    }

    // ✅ Lấy top 5 sản phẩm bán chạy nhất hôm nay
    public List<BestSaleDTO> getTopSellingProductToday() {
        List<Object[]> results = bestSaleRepository.findTop5SellingProductsToday();
        return mapToBestSaleDTOList(results);
    }

    // ✅ Lấy top 5 sản phẩm bán chạy nhất tuần này
    public List<BestSaleDTO> getTopSellingProductThisWeek() {
        List<Object[]> results = bestSaleRepository.findTop5SellingProductsThisWeek();
        return mapToBestSaleDTOList(results);
    }

    // ✅ Lấy top 5 sản phẩm bán chạy nhất tháng này
    public List<BestSaleDTO> getTopSellingProductThisMonth() {
        List<Object[]> results = bestSaleRepository.findTop5SellingProductsThisMonth();
        return mapToBestSaleDTOList(results);
    }
    // ✅ Lấy top 5 sản phẩm bán chạy nhất năm nay
    public List<BestSaleDTO> getTopSellingProductThisYear() {
        List<Object[]> results = bestSaleRepository.findTop5SellingProductsThisYear();
        return mapToBestSaleDTOList(results);
    }

    // ✅ Lấy top 5 sản phẩm bán chạy theo khoảng ngày tùy chỉnh
    public List<BestSaleDTO> getTopSellingProductByCustomDateRange(String startDate, String endDate) {
        List<Object[]> results = bestSaleRepository.findTop5SellingProductsByCustomDateRange(startDate, endDate);
        return mapToBestSaleDTOList(results);
    }

}
