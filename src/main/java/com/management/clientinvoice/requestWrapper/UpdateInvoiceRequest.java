package com.management.clientinvoice.requestWrapper;

import com.google.api.client.util.DateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInvoiceRequest extends InvoiceRequestWrapper {

    private Long id;

    Boolean readyToBilledToClient = false;
    
}
