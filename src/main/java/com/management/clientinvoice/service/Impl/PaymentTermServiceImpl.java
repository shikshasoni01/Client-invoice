package com.management.clientinvoice.service.Impl;

import com.management.clientinvoice.domain.PaymentTerm;
import com.management.clientinvoice.dto.PaymentTermDTO;
import com.management.clientinvoice.repository.PaymentTermRepository;
import com.management.clientinvoice.service.PaymentTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentTermServiceImpl implements  PaymentTermService{

    @Autowired
    private PaymentTermService paymentTermService;

    @Autowired
    private PaymentTermRepository paymentTermRepository;


    @Override
    public List<PaymentTermDTO> getAllPaymentTerm() {
        List<PaymentTerm> paymentTerms = paymentTermRepository.findAllPaymentTermOrderById();
        List<PaymentTermDTO> paymentTermDTOS = new ArrayList<>();
        for(PaymentTerm paymentTerm : paymentTerms){
            PaymentTermDTO paymentTermDTO = new PaymentTermDTO();

            paymentTermDTO.setId(paymentTerm.getId());
            paymentTermDTO.setDataType(paymentTerm.getDataType());
            paymentTermDTO.setValue(paymentTerm.getValue());

            paymentTermDTOS.add(paymentTermDTO);
        }
        return paymentTermDTOS;
    }
}
