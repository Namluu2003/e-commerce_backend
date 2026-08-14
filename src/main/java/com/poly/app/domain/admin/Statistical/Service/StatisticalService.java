package com.poly.app.domain.admin.Statistical.Service;

import com.poly.app.domain.admin.Statistical.Repository.StatisticalRepository;
import com.poly.app.infrastructure.email.Email;
import com.poly.app.infrastructure.email.EmailSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticalService {
    @Autowired
    StatisticalRepository statisticalRepository;
    @Autowired
    EmailSenderStatistical emailSenderStatistical;

    //Doanh thu
    private List<NameData> mapToSummaryDTO(List<Object[]> results) {
        return results.stream().map(row -> new NameData(
                row[0].toString(),
                ((Number) row[1]).doubleValue(),
                ((Number) row[2]).intValue(),
                ((Number) row[3]).intValue(),
                ((Number) row[4]).intValue(),
                ((Number) row[5]).intValue()
        )).collect(Collectors.toList());
    }

    public List<NameData> getSumDay() {
        return mapToSummaryDTO(statisticalRepository.getSumDay());
    }

    public List<NameData> getSumWeek() {
        return mapToSummaryDTO(statisticalRepository.getSumWeek());
    }

    public List<NameData> getSumMonth() {
        return mapToSummaryDTO(statisticalRepository.getSumMonth());
    }

    public List<NameData> getSumYear() {
        return mapToSummaryDTO(statisticalRepository.getSumYear());
    }

    public List<NameData> getSumByCustomDate(String startDate, String endDate) {
        return mapToSummaryDTO(statisticalRepository.getSumByCustomDate(startDate, endDate));
    }

    //Sản phẩm bán chạy
    public List<Object[]> getBestSellingProduct(int page, int size) {
        int offset = page * size; // Tính toán offset để phân trang
        return statisticalRepository.findBestSellingProduct(offset, size);
    }

    // mail doanh thu ngày

    public File generateDailyRevenueExcelReport() throws IOException {
        List<NameData> data = getSumDay();
        if (data.isEmpty()) {
            throw new IllegalStateException("Không có dữ liệu thống kê ngày hôm nay");
        }
        // Lấy ngày hiện tại và định dạng theo DD/MM/YYYY
        LocalDate today = LocalDate.now();
        String todayDate = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        NameData reportData = data.get(0);

        // Tạo workbook Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DoanhThu");

        // Tạo style cho header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Dòng tiêu đề
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Báo cáo doanh thu ngày " + todayDate);
        titleCell.setCellStyle(headerStyle);

        Cell dateRangeCell = titleRow.createCell(1);
        dateRangeCell.setCellValue("Ngày " + todayDate);

        // Dòng trống
        sheet.createRow(1);

        // Dòng tiêu đề cột
        Row headerRow = sheet.createRow(2);
        String[] headers = {
                "Tổng doanh thu (VNĐ)",
                "Tổng số đơn",
                "Số sản phẩm đã vận chuyển",
                "Đơn thành công",
                "Đơn hủy",
                "Đơn hoàn"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Dòng dữ liệu
        Row dataRow = sheet.createRow(3);
//        dataRow.createCell(0).setCellValue(1);
        dataRow.createCell(0).setCellValue(reportData.getTotalRevenue());
        CellStyle rightAlignStyle = workbook.createCellStyle();
        rightAlignStyle.setAlignment(HorizontalAlignment.RIGHT);
        Cell revenueCell = dataRow.createCell(0);
        long roundedRevenue = Math.round(reportData.getTotalRevenue());
        String formattedRevenue = NumberFormat.getInstance(new Locale("vi", "VN")).format(roundedRevenue);
        revenueCell.setCellValue(formattedRevenue + " VNĐ");
        revenueCell.setCellStyle(rightAlignStyle);
        dataRow.createCell(1).setCellValue(reportData.getTotalOrders());
        dataRow.createCell(2).setCellValue(reportData.getTotalProductsSold());
        dataRow.createCell(3).setCellValue(reportData.getSuccessfulOrders());
        dataRow.createCell(4).setCellValue(reportData.getCancelledOrders());

        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        // Lưu vào file tạm
        String fileName = "BaoCaoDoanhThuNgay_" + today.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "_" + UUID.randomUUID().toString();
        File tempFile = File.createTempFile(fileName, ".xlsx");
        FileOutputStream fileOut = new FileOutputStream(tempFile);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();

        return tempFile;
    }


    //      Gửi báo cáo doanh thu ngày qua email
    public void sendDailyRevenueReportEmail() {
        log.info("Đang gửi báo cáo doanh thu ngày qua email");
        try {
            // Tạo file Excel
            File excelFile = generateDailyRevenueExcelReport();
            // Lấy ngày hiện tại và định dạng theo DD/MM/YYYY
            LocalDate today = LocalDate.now();
            String todayDate = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));


            // Chuẩn bị email
            Email email = new Email();
            String subject = "Báo cáo doanh thu ngày " + todayDate;
            String titleEmail = "Báo Cáo Doanh Thu Ngày";

            email.setSubject(subject);
            email.setTitleEmail(titleEmail);
            email.setBody(
                    "Kính gửi,<br><br>" +
                            "Đính kèm là báo cáo doanh thu ngày " + todayDate + ". " +
                            "Vui lòng xem file đính kèm để biết chi tiết.<br><br>" +
                            "Trân trọng,<br>Hệ thống thống kê"
            );

            // Thiết lập email nhận - cập nhật với địa chỉ email thực tế
            email.setToEmail(new String[]{"binhtdph46147@fpt.edu.vn"});

            // Thêm file Excel vào email
            email.setExcelFile(excelFile);
            email.setFileName("BaoCaoDoanhThuNgay_" + today.format(DateTimeFormatter.ofPattern("ddMMyyyy")));

            // Gửi email sử dụng service EmailSenderStatistical
            emailSenderStatistical.sendEmailWithExcel(email);
            log.info("Gửi báo cáo doanh thu ngày thành công");
        } catch (Exception e) {
            log.error("Lỗi khi gửi báo cáo doanh thu ngày: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể gửi báo cáo doanh thu ngày", e);

        }
    }

// Công việc định kỳ để gửi báo cáo doanh thu ngày vào lúc 0h00

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledDailyRevenueReport() {
        log.info("Bắt đầu gửi báo cáo doanh thu ngày theo lịch");
        try {
            sendDailyRevenueReportEmail();
        } catch (Exception e) {
            log.error("Lỗi khi thực hiện gửi báo cáo theo lịch: {}", e.getMessage(), e);
        }
    }
}



