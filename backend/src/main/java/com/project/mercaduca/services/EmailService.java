package com.project.mercaduca.services;

import com.project.mercaduca.models.Product;
import com.project.mercaduca.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("wmmartinezhdz@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendHtml(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendProductApprovalSummaryEmail(User user, List<Product> approved, List<Product> rejected, String remark) {
        String subject = "Resumen de revisión de productos";
        StringBuilder htmlBody = new StringBuilder();

        htmlBody.append("<h2>Hola ").append(user.getName()).append(",</h2>");
        htmlBody.append("<p>Este es el resumen de la revisión de tus productos:</p>");

        if (!approved.isEmpty()) {
            htmlBody.append("<h3 style='color:green;'>✅ Productos Aprobados:</h3><ul>");
            for (Product product : approved) {
                htmlBody.append("<li>").append(product.getName()).append("</li>");
            }
            htmlBody.append("</ul>");
        }

        if (!rejected.isEmpty()) {
            htmlBody.append("<h3 style='color:red;'>❌ Productos Rechazados:</h3><ul>");
            for (Product product : rejected) {
                htmlBody.append("<li>").append(product.getName()).append("</li>");
            }
            htmlBody.append("</ul>");
        }

        htmlBody.append("<p><strong>Observaciones generales:</strong><br>").append(remark).append("</p>");
        htmlBody.append("<p>Gracias por usar Mercaduca 🚀</p>");

        sendHtml(user.getMail(), subject, htmlBody.toString());
    }

}
