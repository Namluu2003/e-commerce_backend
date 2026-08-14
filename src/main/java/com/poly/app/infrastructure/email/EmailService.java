package com.poly.app.infrastructure.email;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailSender emailSender;


    // Gửi mail kích họat
    @Async
    public void sendRegistrationEmail(String emailAddress, String token) {


        String activationLink = "http://localhost:5173/activate?token=" + token;

        Email email = new Email();
        String[] emailSend = {emailAddress};
        email.setToEmail(emailSend);
        email.setSubject("Tạo tài khoản thành công");
        email.setTitleEmail("");
        email.setBody("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; margin: 50px;\">\n" +
                "    <div class=\"success-message\" style=\"background-color: #FFFFF; color: black; padding: 20px; border-radius: 10px; margin-top: 50px;\">\n" +
                "        <h2 style=\"color: #333;\">Tài khoản đã được tạo thành công!" +
                "            Ấn vào đường link phía dưới để có thể kích hoạt tài khoản</h2>\n" +
                "        <p style=\"color: #555;\">Cảm ơn bạn đã đăng ký tại TheHands. Dưới đây là thông tin đăng nhập của bạn:</p>\n" +
                "        <p><strong>Email:</strong> " + emailAddress + "</p>\n" +
                "     <a href=\"http://localhost:5173/activate/"+token+"\"\n" +
                "   style=\"display: inline-block; padding: 10px 20px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px;\">\n" +
                "   Kích hoạt tài khoản\n" +
                "</a>" +
                "        <p style=\"color: #555;\">Kích hoạt và Đăng nhập ngay để trải nghiệm!</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n");

        emailSender.sendEmail(email);
    }


    // Gửi mail kích họat
    @Async
    public void sendForgotPasswordEmail(String emailAddress, String token) {

        Email email = new Email();
        String[] emailSend = {emailAddress};
        email.setToEmail(emailSend);
        email.setSubject("Yêu cầu đặt lại mật khẩu - TheHands");
        email.setTitleEmail("");
        email.setBody("<!DOCTYPE html>\n" +
                "<html lang=\"vi\">\n" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; margin: 50px;\">\n" +
                "    <div style=\"background-color: #ffffff; color: #333; padding: 30px; border-radius: 10px; max-width: 600px; margin: auto;\">\n" +
                "        <h2 style=\"color: #333;\">Yêu cầu đặt lại mật khẩu</h2>\n" +
                "        <p style=\"color: #555;\">Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn tại <strong>TheHands</strong>.</p>\n" +
                "        <p><strong>Email:</strong> " + emailAddress + "</p>\n" +
                "        <p style=\"color: #555;\">Nhấn vào nút bên dưới để đặt lại mật khẩu của bạn:</p>\n" +
                "        <a href=\"http://localhost:5173/reset-password/" + token + "\"\n" +
                "           style=\"display: inline-block; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; font-weight: bold;\">\n" +
                "            Đặt lại mật khẩu\n" +
                "        </a>\n" +
                "        <p style=\"color: #999; margin-top: 20px; font-size: 12px;\">Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n");

        emailSender.sendEmail(email);
    }

    @Async
    public void sendForgotAdminPasswordEmail(String emailAddress, String token) {

        Email email = new Email();
        String[] emailSend = {emailAddress};
        email.setToEmail(emailSend);
        email.setSubject("Yêu cầu đặt lại mật khẩu - TheHands");
        email.setTitleEmail("");
        email.setBody("<!DOCTYPE html>\n" +
                "<html lang=\"vi\">\n" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; margin: 50px;\">\n" +
                "    <div style=\"background-color: #ffffff; color: #333; padding: 30px; border-radius: 10px; max-width: 600px; margin: auto;\">\n" +
                "        <h2 style=\"color: #333;\">Yêu cầu đặt lại mật khẩu</h2>\n" +
                "        <p style=\"color: #555;\">Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn tại <strong>TheHands</strong>.</p>\n" +
                "        <p><strong>Email:</strong> " + emailAddress + "</p>\n" +
                "        <p style=\"color: #555;\">Nhấn vào nút bên dưới để đặt lại mật khẩu của bạn:</p>\n" +
                "        <a href=\"http://localhost:5173/reset-admin-password/" + token + "\"\n" +
                "           style=\"display: inline-block; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; font-weight: bold;\">\n" +
                "            Đặt lại mật khẩu\n" +
                "        </a>\n" +
                "        <p style=\"color: #999; margin-top: 20px; font-size: 12px;\">Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n");

        emailSender.sendEmail(email);
    }
}
