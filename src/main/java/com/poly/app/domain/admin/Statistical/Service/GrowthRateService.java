package com.poly.app.domain.admin.Statistical.Service;

import com.poly.app.domain.admin.Statistical.Repository.GrowthRateProductRepository;
import com.poly.app.domain.admin.Statistical.Repository.GrowthRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class GrowthRateService {
    @Autowired
    private GrowthRateRepository growthRateRepository;
    @Autowired
    private GrowthRateProductRepository growthRateProductRepository;

    public List<GrowthRateDTOY> getGroethRateYear() {
        List<Object[]> rawData = growthRateRepository.getGroethRateYear();
        List<GrowthRateDTOY> resulty = new ArrayList<>();

        for (Object[] row : rawData) {
            int year = (row[0] != null) ? ((Number) row[0]).intValue() : 0;
            double revenue = (row[1] != null) ? ((Number) row[1]).doubleValue() : 0.0;
            double lastYearRevenue = (row[2] != null) ? ((Number) row[2]).doubleValue() : 0.0;
            double difference = (row[3] != null) ? ((Number) row[3]).doubleValue() : 0.0;
            String percentageChange = (row[4] != null) ? row[4].toString() : "";

            resulty.add(new GrowthRateDTOY(year, revenue, lastYearRevenue, difference, percentageChange));
        }
        return resulty;
    }


    public List<GrowthRateDTOM> getGrowthRateMonth() {
        List<Object[]> rawData = growthRateRepository.getGrowthRateMonth();
        List<GrowthRateDTOM> result = new ArrayList<>();

        for (Object[] row : rawData) {
            int year = (row[0] != null) ? ((Number) row[0]).intValue() : 0;
            int month = (row[1] != null) ? ((Number) row[1]).intValue() : 0;
            double revenue = (row[2] != null) ? ((Number) row[2]).doubleValue() : 0.0;
            double lastMonthRevenue = (row[3] != null) ? ((Number) row[3]).doubleValue() : 0.0;
            double difference = (row[4] != null) ? ((Number) row[4]).doubleValue() : 0.0;
            String percentageChange = (row[5] != null) ? row[5].toString() : "";

            result.add(new GrowthRateDTOM(year, month, revenue, lastMonthRevenue, difference, percentageChange));
        }
        return result;
    }
    //sp tháng
    public List<GrowthRateDTOSSM> getSSProductMonth() {
        List<Object[]> rawData = growthRateProductRepository.getSSProductMonth();
        List<GrowthRateDTOSSM> result = new ArrayList<>();

        if (rawData != null) {
            for (Object[] row : rawData) {
                int year = (row[0] != null) ? ((Number) row[0]).intValue() : 0;  // Năm
                int month = (row[1] != null) ? ((Number) row[1]).intValue() : 0;  // Tháng
                int quantityMonth = (row[2] != null) ? ((Number) row[2]).intValue() : 0;  // Tổng sản phẩm bán trong tháng
                int lastMonthSold = (row[3] != null) ? ((Number) row[3]).intValue() : 0;  // Tổng sản phẩm bán tháng trước
                int difference = (row[4] != null) ? ((Number) row[4]).intValue() : 0;  // Số lượng chênh lệch giữa tháng này và tháng trước
                String percentageChange = (row[5] != null) ? row[5].toString() : "0%";  // Tăng trưởng %

                result.add(new GrowthRateDTOSSM(year, month, quantityMonth, lastMonthSold, difference, percentageChange));
            }
        }

        return result;
    }


    // Thống kê số lượng sản phẩm bán trong năm nay so với năm trước
    public List<GrowthRateDTOSSY> getSSProductYear() {
        List<Object[]> rawData = growthRateProductRepository.getSSProductYear();
        List<GrowthRateDTOSSY> result = new ArrayList<>();

        if (rawData != null) {
            for (Object[] row : rawData) {
                int year = (row[0] != null) ? ((Number) row[0]).intValue() : 0;  // year
                int quantityYear = (row[1] != null) ? ((Number) row[1]).intValue() : 0;  // total_sold_this_year
                int lastYearSold = (row[2] != null) ? ((Number) row[2]).intValue() : 0;  // last_year_sold
                int difference = (row[3] != null) ? ((Number) row[3]).intValue() : 0;  // difference
                String yearPercentageChange = (row[4] != null) ? row[4].toString() : "0%";  // yearPercentageChange

                result.add(new GrowthRateDTOSSY(year, quantityYear, lastYearSold, difference, yearPercentageChange));
            }
        }

        return result;
    }





}
