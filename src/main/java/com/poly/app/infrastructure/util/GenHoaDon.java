package com.poly.app.infrastructure.util;


import com.cloudinary.provisioning.Account;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.poly.app.domain.model.Bill;
import com.poly.app.domain.model.BillDetail;
import com.poly.app.domain.model.BillHistory;
import com.poly.app.domain.model.Customer;
import com.poly.app.domain.repository.ProductRepository;
import com.poly.app.infrastructure.constant.BillStatus;
import com.poly.app.infrastructure.constant.Constants;
import com.poly.app.infrastructure.constant.MailConstant;
import com.poly.app.infrastructure.constant.TypeBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class GenHoaDon {
    @Autowired
    private ProductRepository productDetailRepository;

    public File genHoaDon(Bill bill,
                          List<BillDetail> billDetails, BillHistory billHistory
    ) {
        Document document = new Document();
        File pdfFile = null;

        try {
            pdfFile = new File(bill.getCode() + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            BaseFont unicodeFont = BaseFont.createFont(MailConstant.FONT_INVOICE, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(unicodeFont, 25, Font.BOLD);
            Font headerFont = new Font(unicodeFont, 12, Font.BOLD);
            Font normalFont = new Font(unicodeFont, 12);

            ClassPathResource resource = new ClassPathResource(MailConstant.LOGO_PATH);
            Image img = Image.getInstance(resource.getURL());
            float pageWidth = document.getPageSize().getWidth();
            float pageHeight = document.getPageSize().getHeight();
            float imageWidth = img.getWidth();
            float imageHeight = img.getHeight();
            float aspectRatio = imageWidth / imageHeight;
            float newWidth = 100;
            float newHeight = newWidth / aspectRatio;

            // Đặt vị trí và kích thước mới cho ảnh



            img.setAbsolutePosition(30, 750);
            img.scaleAbsolute(newWidth, newHeight);

            // Thêm ảnh vào tài liệu
            document.add(img);

            // Tạo mã QR Code và thêm vào tài liệu
            String qrContent = "Mã hóa đơn: " + bill.getBillCode() + 
                              "\nNgày tạo: " + DateUtil.converDateTimeString(bill.getCreatedAt()) + 
                              "\nTổng tiền: " + (bill.getMoneyAfter() != null ? bill.getMoneyAfter().longValue() :
                              (bill.getTotalMoney() != null ? bill.getTotalMoney().longValue() : 0));
            
            BarcodeQRCode qrcode = new BarcodeQRCode(qrContent, 1, 1, null);
            Image qrcodeImage = qrcode.getImage();
            qrcodeImage.scaleAbsolute(newWidth, newWidth);  // Sử dụng cùng kích thước với logo
            qrcodeImage.setAbsolutePosition(pageWidth - 30 - newWidth, 750); // Đặt đối xứng với logo
            document.add(qrcodeImage);

            // Đầu trang: F-Shoes, Số điện thoại, Email, Địa chỉ, QR Code
            Paragraph fShoes = new Paragraph("The Hands", titleFont);
            fShoes.setAlignment(Element.ALIGN_CENTER);
            fShoes.setSpacingAfter(10f);
            document.add(fShoes);

            Paragraph contactInfo = new Paragraph("Số điện thoại: 0357345935", normalFont);
            Paragraph contactInfo2 = new Paragraph("Email: thehandsshoesweb@gmail.com", normalFont);
            Paragraph contactInfo3 = new Paragraph("Địa chỉ: Tòa nhà FPT Polytechnic, đường Trịnh Văn Bô, Phương Canh, Nam Từ Liêm, Hà Nội ", normalFont);
            contactInfo.setAlignment(Element.ALIGN_CENTER);
            contactInfo2.setAlignment(Element.ALIGN_CENTER);
            contactInfo3.setAlignment(Element.ALIGN_CENTER);
            document.add(contactInfo);
            document.add(contactInfo2);
            document.add(contactInfo3);

            // Thêm dòng trống
            document.add(new Paragraph(""));

            // Thông tin hóa đơn
            Paragraph invoiceHeader = new Paragraph("HÓA ĐƠN BÁN HÀNG", new Font(unicodeFont, 20, Font.BOLD));
            invoiceHeader.setAlignment(Element.ALIGN_CENTER);
            invoiceHeader.setSpacingBefore(10f);
            invoiceHeader.setSpacingAfter(10f);
            document.add(invoiceHeader);

            PdfPTable invoiceTable = new PdfPTable(2);
            invoiceTable.setWidthPercentage(100);

            String fullName;
            if (bill.getCustomerName() != null && !bill.getCustomerName().isEmpty()) {
                fullName = bill.getCustomerName();
            } else {
                fullName = "Khách lẻ";
            }

            PdfPCell cell3 = new PdfPCell(new Paragraph("Tên khách hàng: " + fullName, normalFont));
            cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell3.setBorder(Rectangle.NO_BORDER);
            invoiceTable.addCell(cell3);
            PdfPCell cell1 = new PdfPCell(new Paragraph("Mã Hóa Đơn: " + bill.getBillCode(), normalFont));
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell1.setBorder(Rectangle.NO_BORDER);
            invoiceTable.addCell(cell1);
//            PdfPCell cell4 = new PdfPCell(new Paragraph("Địa chỉ nhận hàng: " +
//                    (bill.getTypeBill() == TypeBill.OFFLINE ? "Tại cửa hàng" : bill.getShippingAddress()), normalFont));
//            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
//            cell4.setBorder(Rectangle.NO_BORDER);
//            invoiceTable.addCell(cell4);

            PdfPCell cell2 = new PdfPCell(new Paragraph("Ngày tạo: " + DateUtil.converDateTimeString(bill.getCreatedAt()), normalFont));
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell2.setBorder(Rectangle.NO_BORDER);
            invoiceTable.addCell(cell2);

//            PdfPCell cell5 = new PdfPCell(new Paragraph("Nhân viên: " +
//                    (bill.getStaff() != null ? bill.getStaff().getFullName() : "Nhân viên"), normalFont));
//            cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
//            cell5.setBorder(Rectangle.NO_BORDER);
//            invoiceTable.addCell(cell5);

            PdfPCell cell6 = new PdfPCell(new Paragraph("Trạng thái: " + 
                (bill.getStatus() == BillStatus.DA_HOAN_THANH ? "Hoàn thành" :
                 bill.getStatus() == BillStatus.CHO_XAC_NHAN ? "Chờ xử lý" :
                 bill.getStatus() == BillStatus.DA_HUY ? "Đã hủy" : "Đang xử lý"), normalFont));
            cell6.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell6.setBorder(Rectangle.NO_BORDER);
            invoiceTable.addCell(cell6);

            // Thêm bảng vào tài liệu
            document.add(invoiceTable);

            Paragraph invoiceHeader2 = new Paragraph("DANH SÁCH SẢN PHẨM", new Font(unicodeFont, 15, Font.BOLD));
            invoiceHeader2.setAlignment(Element.ALIGN_CENTER);
            invoiceHeader2.setSpacingBefore(5f);
            invoiceHeader2.setSpacingAfter(5f);
            document.add(invoiceHeader2);
            // Thêm bảng sản phẩm
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            DecimalFormat decimalFormat = new DecimalFormat("###,###.## VND");

            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            int sttCounter = 1;
            PdfPCell cell = new PdfPCell(new Phrase("STT", headerFont));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Tên sản phẩm", headerFont));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Số lượng", headerFont));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Đơn giá", headerFont));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Thành tiền", headerFont));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Trạng thái", headerFont));
            table.addCell(cell);
            for (BillDetail billDetail : billDetails) {
                // Thêm dữ liệu sản phẩm
                table.addCell(String.valueOf(sttCounter));
                table.addCell(billDetail.getProductDetail().getProduct().getProductName());
                table.addCell(String.valueOf(billDetail.getQuantity()));
                table.addCell(decimalFormat.format(billDetail.getPrice()));
                table.addCell(decimalFormat.format(billDetail.getPrice() * billDetail.getQuantity()));
                table.addCell(bill.getStatus() == BillStatus.DA_HOAN_THANH ? "Hoàn thành" :
                            bill.getStatus() == BillStatus.CHO_XAC_NHAN ? "Chờ xác nhận" :
                            bill.getStatus() == BillStatus.DA_THANH_TOAN ? "Đã thanh toán" :
                            bill.getStatus() == BillStatus.CHO_VAN_CHUYEN ? "Chờ vận chuyển" :
                            bill.getStatus() == BillStatus.DANG_VAN_CHUYEN ? "Đang vận chuyển" :
                            bill.getStatus() == BillStatus.DA_HUY ? "Đã hủy" : "Đang xử lý");

                sttCounter++;
            }


            // Thêm bảng vào tài liệu
            document.add(table);

            // Thêm tổng cộng
            PdfPTable invoiceTable3 = new PdfPTable(2);
            invoiceTable3.setWidthPercentage(100);

            PdfPCell totalcell = new PdfPCell(new Paragraph("Tổng tiền hàng:", normalFont));
            totalcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalcell.setBorder(Rectangle.NO_BORDER);
            invoiceTable3.addCell(totalcell);
            PdfPCell totalcell2 = new PdfPCell(new Paragraph(decimalFormat.format(bill.getMoneyBeforeDiscount() != null ? bill.getMoneyBeforeDiscount().longValue() : 0), headerFont));
            totalcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalcell2.setBorder(Rectangle.NO_BORDER);
            invoiceTable3.addCell(totalcell2);

            PdfPCell totalcell3 = new PdfPCell(new Paragraph("Giảm giá:", normalFont));
            totalcell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalcell3.setBorder(Rectangle.NO_BORDER);
            invoiceTable3.addCell(totalcell3);
            if (bill.getDiscountMoney() != null) {
                PdfPCell totalcell4 = new PdfPCell(new Paragraph(decimalFormat.format(bill.getDiscountMoney().longValue()), headerFont));
                totalcell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalcell4.setBorder(Rectangle.NO_BORDER);
                invoiceTable3.addCell(totalcell4);
            } else {
                PdfPCell totalcell4 = new PdfPCell(new Paragraph(decimalFormat.format(0), headerFont));
                totalcell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalcell4.setBorder(Rectangle.NO_BORDER);
                invoiceTable3.addCell(totalcell4);
            }

            PdfPCell totalcell5 = new PdfPCell(new Paragraph("Phí giao hàng:", normalFont));
            totalcell5.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalcell5.setBorder(Rectangle.NO_BORDER);
            invoiceTable3.addCell(totalcell5);
            if (bill.getShipMoney() != null) {
                PdfPCell totalcell6 =
                        new PdfPCell(new Paragraph(decimalFormat.format(bill.getIsFreeShip() ? 0 :bill.getShipMoney().longValue()), headerFont));
                totalcell6.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalcell6.setBorder(Rectangle.NO_BORDER);
                invoiceTable3.addCell(totalcell6);
            } else {
                PdfPCell totalcell6 = new PdfPCell(new Paragraph(decimalFormat.format(0), headerFont));
                totalcell6.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalcell6.setBorder(Rectangle.NO_BORDER);
                invoiceTable3.addCell(totalcell6);
            }

            PdfPCell totalcell7 = new PdfPCell(new Paragraph("Tổng tiền cần thanh toán:", normalFont));
            totalcell7.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalcell7.setBorder(Rectangle.NO_BORDER);
            invoiceTable3.addCell(totalcell7);
            PdfPCell totalcell8 = new PdfPCell(
                    new Paragraph(decimalFormat.format(bill.getMoneyAfter() != null ? bill.getMoneyAfter().longValue() : 
                        (bill.getTotalMoney() != null ? bill.getTotalMoney().longValue() : 0)), headerFont));
            totalcell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalcell8.setBorder(Rectangle.NO_BORDER);
            invoiceTable3.addCell(totalcell8);
            document.add(invoiceTable3);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return pdfFile;
    }


}
