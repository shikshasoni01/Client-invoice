package com.biz4solutions.clientinvoice.service;

import com.biz4solutions.clientinvoice.dto.PaymentTermDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentTermService {

    List<PaymentTermDTO> getAllPaymentTerm();

}
