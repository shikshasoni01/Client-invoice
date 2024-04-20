package com.biz4solutions.clientinvoice.requestWrapper;

import com.biz4solutions.clientinvoice.domain.InvoiceItem;
import com.google.api.client.util.DateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInvoiceRequest extends InvoiceRequestWrapper {

    private Long id;

    Boolean readyToBilledToClient = false;
    
}
