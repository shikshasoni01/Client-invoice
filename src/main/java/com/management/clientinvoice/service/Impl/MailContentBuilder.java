package com.biz4solutions.clientinvoice.service.Impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Map;


@Service
public class MailContentBuilder {

    @Autowired
    private SpringTemplateEngine springTemplateEngine;


    public String build(String message, IWebContext context) {
        return springTemplateEngine.process("mailTemplate", context);
    }

    public String build(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        //final IWebContext webContext=(IWebContext)context;
        return springTemplateEngine.process("mailTemplate", context);
    }

    public String build(Map<String, Object> map, String mailType) {
        Context context = new Context();
        context.setVariables(map);
        String template;
        switch (mailType) {
            /*case "SIGNUP":
                template = springTemplateEngine.process("WelcomeMailTemplate", context);
                break;*/

            case "EMAIL_OTP":
                template = springTemplateEngine.process("ForgotOtpMailTemplate", context);
                break;

            case "EMAIL_VERIFICATION":
                template = springTemplateEngine.process("MailVerificationTemplate", context);
                break;

            case "DOMESTIC_SAME_STATE_ATTACHMENT":
                template = springTemplateEngine.process("invoice", context);
                break;

            case "DOMESTIC_DIFF_STATE_ATTACHMENT":
                template = springTemplateEngine.process("domesticDifferentState", context);
                break;

                case "INTERNATIONAL_ATTACHMENT":
                template = springTemplateEngine.process("InvoiceInternationalTemplate", context);
                break;

                case "LLC_ATTACHMENT":
                template = springTemplateEngine.process("InvoiceLLCTemplate", context);
                break;

            case "COMMON_MAIL_TEMPLATE":
                template = springTemplateEngine.process("CommonTemplate", context);
                break;

            case "BILLED_TO_CLIENT_TEMPLATE":
                template = springTemplateEngine.process("BilledToClientTemplate", context);
                break;

            case "FULL_PAYMENT_TEMPLATE":
                template = springTemplateEngine.process("fullPaymentTemplate", context);
                break;

            case "READY_TO_BILL_TEMPLATE":
                template = springTemplateEngine.process("readyToBillTemplate", context);
                break;

            case "VOID_TEMPLATE":
                template = springTemplateEngine.process("voidTemplate", context);
                break;

            case "INVOICE_NUMBER_UPDATE_MAIL_TEMPLATE":
                template = springTemplateEngine.process("InvoiceNumberUpdateTemplate", context);
                break;

            default:
                template = springTemplateEngine.process("ForgotPasswordMailTemplate", context);

        }
        return template;
    }
}

