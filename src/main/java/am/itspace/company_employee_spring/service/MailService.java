package am.itspace.company_employee_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final MailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage mailMsg = new SimpleMailMessage();
        mailMsg.setTo(to);
        mailMsg.setSubject(subject);
        mailMsg.setText(text);

        mailSender.send(mailMsg);
    }
}
