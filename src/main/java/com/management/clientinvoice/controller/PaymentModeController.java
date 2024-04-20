package com.biz4solutions.clientinvoice.controller;

import com.biz4solutions.clientinvoice.constant.WebConstants;
import com.biz4solutions.clientinvoice.dto.PaymentModeDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.repository.PaymentModeRepository;
import com.biz4solutions.clientinvoice.service.ICommonService;
import com.biz4solutions.clientinvoice.service.PaymentModeService;
import com.biz4solutions.clientinvoice.util.ResponseFormatter;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping(value = "/api/v1/user/paymentMode")
public class PaymentModeController {


    private static final Logger LOGGER = Logger.getLogger(PaymentModeController.class);

    @Autowired
    private PaymentModeService paymentModeService;

    @Autowired
    private PaymentModeRepository paymentModeRepository;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/getAllPaymentMode", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getAlPaymentMode(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
                                                    @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
    ) throws InvoiceManagementException {

        LOGGER.info("get all PaymentMode Data Start");

        List<PaymentModeDTO> paymentModeDTO = paymentModeService.getAllPaymentMode();
        String message = messageSource.getMessage("allPaymentModeDataGetSuccess", null, new Locale(acceptLanguage));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, paymentModeDTO);

        LOGGER.info("get all PaymentMode Data end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
