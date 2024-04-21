package com.management.clientinvoice.service.Impl;


import com.management.clientinvoice.domain.PaymentMode;
import com.management.clientinvoice.dto.PaymentModeDTO;
import com.management.clientinvoice.repository.PaymentModeRepository;
import com.management.clientinvoice.service.PaymentModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentModeServiceImpl implements PaymentModeService {


    @Autowired
    private PaymentModeService paymentModeService;

    @Autowired
    private PaymentModeRepository paymentModeRepository;


    @Override
    public List<PaymentModeDTO> getAllPaymentMode() {
        List<PaymentMode> paymentModes = paymentModeRepository.findAll();
        List<PaymentModeDTO> paymentModeDTOS = new ArrayList<>();
        for(PaymentMode paymentMode : paymentModes){
            PaymentModeDTO paymentModeDTO = new PaymentModeDTO();

            paymentModeDTO.setId(paymentMode.getId());
            paymentModeDTO.setDataType(paymentMode.getDataType());
            paymentModeDTO.setValue(paymentMode.getValue());

           paymentModeDTOS.add(paymentModeDTO);
        }
        return paymentModeDTOS;
    }



}
