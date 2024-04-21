package com.management.clientinvoice.service;

import com.management.clientinvoice.dto.PaymentTermDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentTermService {

    List<PaymentTermDTO> getAllPaymentTerm();

}
