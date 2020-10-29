package project.ee.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import project.ee.models.notificationEmail.NotificationEmail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailSendingServiceTest {

    @Mock
    JavaMailSender mailSender;

    @Mock
    MailContentBuilderService service;

    @InjectMocks
    MailSendingService sendingService;

    NotificationEmail email;

    MimeMessagePreparator messagePreparator;

    @BeforeEach
    void setUp() {
        email = new NotificationEmail("hello","gk@ee.com","body");
        messagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("ee@email.com");
            helper.setTo(email.getRecipient());
            helper.setSubject(email.getSubject());
            helper.setText(service.build(email.getBody(),email.getSubject()));
        };
    }

    @Test
    void sendMail() {
        sendingService.sendMail(email);
        verify(mailSender,times(1)).send(any(MimeMessagePreparator.class));
    }
}