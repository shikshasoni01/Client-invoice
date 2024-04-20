package com.biz4solutions.clientinvoice.service.Impl;


import com.biz4solutions.clientinvoice.domain.PaymentMode;
import com.biz4solutions.clientinvoice.dto.PaymentModeDTO;
import com.biz4solutions.clientinvoice.repository.PaymentModeRepository;
import com.biz4solutions.clientinvoice.service.PaymentModeService;
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
