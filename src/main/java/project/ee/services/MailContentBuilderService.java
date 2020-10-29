package project.ee.services;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service

public class MailContentBuilderService {

    private final TemplateEngine templateEngine;

    public MailContentBuilderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    String build(String message,String title){
        Context context = new Context();
        context.setVariable("message",message);
        context.setVariable("title",title);
        return templateEngine.process("mailTemplate",context);
    }

}
