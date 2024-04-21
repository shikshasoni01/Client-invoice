package com.management.clientinvoice.service;

import com.management.clientinvoice.dto.PaymentModeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentModeService {
    List<PaymentModeDTO> getAllPaymentMode();

}
