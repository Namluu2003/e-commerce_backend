package com.poly.app.infrastructure.util;

import com.poly.app.domain.model.Staff;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelHelper {
    public static boolean hasExcelFormat(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Staff> excelToEmployees(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            List<Staff> employees = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                Staff emp = new Staff();
                emp.setFullName(row.getCell(0).getStringCellValue());

                // Đọc ngày sinh
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String dobStr = row.getCell(1).getStringCellValue(); // ví dụ: "09/04/2024"
                LocalDate date = LocalDate.parse(dobStr, formatter);
                LocalDateTime localDateTime = date.atStartOfDay(); // 00:00 giờ
                emp.setDateBirth(localDateTime);

                emp.setCitizenId(row.getCell(2).getStringCellValue());

                // Giới tính: đọc và chuyển về Boolean nếu bạn dùng kiểu boolean
                String gender = row.getCell(3).getStringCellValue();
                emp.setGender("Nam".equalsIgnoreCase(gender) ? Boolean.TRUE : Boolean.FALSE);

                emp.setEmail(row.getCell(4).getStringCellValue());
                emp.setPhoneNumber(row.getCell(5).getStringCellValue());

                employees.add(emp);
            }

            workbook.close();
            return employees;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi xử lý file: " + e.getMessage());
        }
    }

}
