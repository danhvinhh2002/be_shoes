package com.shoescms.common.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final Logger mailLogger = LoggerFactory.getLogger(this.getClass());

    @Value("noreply@animenews.life")
    String from;

    public boolean sendMail(String to, String subject, String content) {
        MimeMessagePreparator preparator = mimeMessage -> {
            try {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setSubject(subject, "utf-8");

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setText(content, "utf-8", "html");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                mimeMessage.setContent(multipart);
            } catch (MessagingException e) {
                log.error("MessagingException", e);
            }
        };

        try {
            javaMailSender.send(preparator);
            return true;
        } catch (MailException me) {
            log.error("MailException", me);
            return false;
        }
    }

    public void sendEmail(String templatePath, String to, String subject, Map<String, Object> content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;

        try {
            mailLogger.info("Bắt đầu gửi email");

            // Kiểm tra giá trị null
            Objects.requireNonNull(templatePath, "Đường dẫn mẫu không được null");
            Objects.requireNonNull(to, "Người nhận không được null");
            Objects.requireNonNull(subject, "Chủ đề không được null");
            Objects.requireNonNull(content, "Bản đồ nội dung không được null");

            messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            // Thêm biến
            Context context = new Context();
            context.setVariables(content);

            mailLogger.info("templates/html", templatePath);
            String contentHtml = templateEngine.process(templatePath, context);

            // Đặt người nhận, chủ đề, nội dung
            messageHelper.setFrom("noreply@animenews.life");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(contentHtml, true);

            javaMailSender.send(message); // Gửi email
            mailLogger.info("Gửi email đến {} thành công!", to);
        } catch (Exception e) {
            // Xử lý ngoại lệ (ghi log hoặc ném lại nếu cần thiết)
            mailLogger.error("Lỗi khi gửi email: {}", e.getMessage(), e);
            throw new MessagingException("Lỗi khi gửi email", e);
        }
    }
}
