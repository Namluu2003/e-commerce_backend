package com.poly.app.domain.admin.Statistical.Service;

import com.poly.app.domain.admin.Statistical.Repository.ChartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChartService {

    private final ChartRepository chartRepository;

    @Transactional(readOnly = true)
    public ChartDTO getTodayChartData() {
        log.info("Fetching today's chart data...");
        return mapToChartDTO(chartRepository.getChartToday());
    }

    @Transactional(readOnly = true)
    public ChartDTO getChartThisWeek() {
        log.info("Fetching this week's chart data...");
        return mapToChartDTO(chartRepository.getChartThisWeek());
    }

    @Transactional(readOnly = true)
    public ChartDTO getChartThisMonth() {
        log.info("Fetching this month's chart data...");
        return mapToChartDTO(chartRepository.getChartThisMonth());
    }

    @Transactional(readOnly = true)
    public ChartDTO getChartThisYear() {
        log.info("Fetching this year's chart data...");
        return mapToChartDTO(chartRepository.getChartThisYear());
    }

    @Transactional(readOnly = true)
    public ChartDTO getChartCustom(String startDate, String endDate) {
        log.info("Fetching custom chart data from {} to {}", startDate, endDate);
        return mapToChartDTO(chartRepository.getChartCustom(startDate, endDate));
    }

    private ChartDTO mapToChartDTO(List<Object[]> results) {
        if (results == null || results.isEmpty() || results.get(0) == null) {
            log.warn("Chart data is empty or null, returning default values.");
            return new ChartDTO(0, 0, 0, 0, 0, 0, 0);
        }

        Object[] row = results.get(0);
        return new ChartDTO(
                getDoubleValue(row, 0), // choXacNhanPercent
                getDoubleValue(row, 1), // daXacNhanPercent
                getDoubleValue(row, 2), // choVanChuyenPercent
                getDoubleValue(row, 3), // dangVanChuyenPercent
                getDoubleValue(row, 4), // daThanhToanPercent
                getDoubleValue(row, 5), // daHoanThanhPercent
                getDoubleValue(row, 6) // daHuyPercent
        );
    }

    private double getDoubleValue(Object[] row, int index) {
        if (row == null || row.length <= index || !(row[index] instanceof Number)) {
            return 0.0;
        }
        return ((Number) row[index]).doubleValue();
    }
}
