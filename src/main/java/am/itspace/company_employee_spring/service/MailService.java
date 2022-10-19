package am.itspace.company_employee_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendHtmlEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage mailMsg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMsg, false);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        mailSender.send(mailMsg);
    }
}
