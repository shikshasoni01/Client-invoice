package com.biz4solutions.clientinvoice.service;

import com.biz4solutions.clientinvoice.dto.InvoiceDTO;

import com.biz4solutions.clientinvoice.dto.PaymentModeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentModeService {
    List<PaymentModeDTO> getAllPaymentMode();

}
