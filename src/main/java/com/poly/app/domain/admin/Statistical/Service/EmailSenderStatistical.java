package com.poly.app.domain.admin.Statistical.Service;

import com.poly.app.infrastructure.constant.MailConstant;
import com.poly.app.infrastructure.email.Email;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class EmailSenderStatistical {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("vietne263204@gmail.com")

    private String sender;
    @Async
    public void sendEmailWithExcel(Email email) {
        String htmlBody = MailConstant.BODY_STARTS +
                email.getTitleEmail() +
                MailConstant.BODY_BODY +
                email.getBody() +
                MailConstant.BODY_END;
        sendMailWithExcel(email.getToEmail(), htmlBody, email.getSubject(), email.getExcelFile(), email.getFileName());
        log.info("sendEmailWithExcel");
    }

    private void sendMailWithExcel(String[] recipients, String msgBody, String subject,
                                   File excelFile, String fileName
    ) {
        log.info("sendMailWithExcel");
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
            ClassPathResource resource = new ClassPathResource(MailConstant.LOGO_PATH);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setBcc(recipients);
            mimeMessageHelper.setText(msgBody, true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.addInline("logoImage", resource);

            if (excelFile != null && fileName != null && excelFile.exists()) {
                mimeMessageHelper.addAttachment(fileName+".xlsx", excelFile);
            }
            javaMailSender.send(mimeMessage);
            log.info("Gửi mail thành công ");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
