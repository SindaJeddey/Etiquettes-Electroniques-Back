package project.ee.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import project.ee.models.notificationEmail.NotificationEmail;

@Service
@Slf4j
public class MailSendingService {

    private final JavaMailSender javaMailSender;
    private final MailContentBuilderService mailContentBuilderService;

    public MailSendingService(JavaMailSender javaMailSender,
                              MailContentBuilderService mailContentBuilderService) {
        this.javaMailSender = javaMailSender;
        this.mailContentBuilderService = mailContentBuilderService;
    }

    @Async
    public void sendMail (NotificationEmail email){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("ee@email.com");
            helper.setTo(email.getRecipient());
            helper.setSubject(email.getSubject());
            helper.setText(mailContentBuilderService.build(email.getBody()));
        };
        try{
            javaMailSender.send(messagePreparator);
            log.info("Mail Sent !");
        }
        catch (MailException e){
            throw new RuntimeException("Exception occurred when sending mail to "+email.getRecipient());
        }
    }
}
