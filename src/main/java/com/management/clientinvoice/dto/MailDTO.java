package com.biz4solutions.clientinvoice.dto;


import com.biz4solutions.clientinvoice.enumerator.MailType;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MailDTO {

    private String mailFrom;

    private String[] mailTo;

    private String mailToo;

    private String mailCc;

    private String mailBcc;

    private String mailSubject;

    private String mailContent;

    private String contentType;

    private List<Object> attachments;

    private File fileAttachment;

    private Map<String, Object> model;

    private MailType emailType;

    private String fileName;
    public MailDTO() {
        contentType = "text/plain";
    }

    public void setMailTo(String[] email) {
    }

    public void fileName(String invoiceId) {
    }
}

