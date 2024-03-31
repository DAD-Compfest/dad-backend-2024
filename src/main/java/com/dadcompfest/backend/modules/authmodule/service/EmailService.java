package com.dadcompfest.backend.modules.authmodule.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailVerification(String recipientEmail, String token, String content, String subject) throws MessagingException, MessagingException {
        // Membuat pesan email
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipientEmail);
        helper.setSubject(subject); // Subjek email

        String htmlContent = "<html>"
                + "<head>"
                + "<link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\">"
                + "</head>"
                + "<body class=\"font-sans bg-gray-100\">"
                + "<div class=\"max-w-2xl mx-auto py-8 px-4\">"
                + "<div class=\"bg-white shadow-md rounded-lg px-8 py-6\">"
                + "<h1 class=\"text-2xl font-bold mb-4\">Hai, " + recipientEmail + ".</h1>"
                + "<p>"+ content +"</p>"
                + "<p class=\"text-lg bg-indigo-200 rounded px-4 py-2\">" + token + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(htmlContent, true);

        // Mengirim pesan email
        javaMailSender.send(message);
    }
}

