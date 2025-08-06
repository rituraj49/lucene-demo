package cl.lcd.service.mailing;

import cl.lcd.util.HelperUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void  sendEmail(List<String> toEmailsList, String subject, String templateName, Object object) {
        log.info("Sending email to: {}, subject: {}, template: {}", toEmailsList, subject, templateName);
        Context context = new Context();
        context.setVariable("booking", object);
//        String body = templateEngine.process(templateName, context);
        String body = HelperUtil.getEmailBody(templateEngine, templateName, context);

        for(String to: toEmailsList) {
            MimeMessage message = mailSender.createMimeMessage();
            try {
                var helper = new org.springframework.mail.javamail.MimeMessageHelper(message, true);
//                helper.setTo(to);
                helper.setTo("rthakur.0211@gmail.com");
                helper.setSubject(subject);
                helper.setText(body, true);
                mailSender.send(message);
                System.out.println("mail sent to " + to + " with subject: " + subject);
            } catch (Exception e) {
                log.error("Error sending email to {}: {}", to, e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
