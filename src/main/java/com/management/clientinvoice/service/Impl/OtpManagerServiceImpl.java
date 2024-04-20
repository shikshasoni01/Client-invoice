package com.biz4solutions.clientinvoice.service.Impl;

import com.biz4solutions.clientinvoice.constant.AppConfigConstants;
import com.biz4solutions.clientinvoice.domain.*;
import com.biz4solutions.clientinvoice.dto.MailDTO;
import com.biz4solutions.clientinvoice.dto.OtpRequestDTO;
import com.biz4solutions.clientinvoice.dto.UserDTO;
import com.biz4solutions.clientinvoice.enumerator.MailType;
import com.biz4solutions.clientinvoice.enumerator.RoleType;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.repository.*;
import com.biz4solutions.clientinvoice.requestWrapper.CommonRequestHeaders;
import com.biz4solutions.clientinvoice.requestWrapper.LoginRequestWrapper;
import com.biz4solutions.clientinvoice.requestWrapper.OtpVerifyRequesWrapper;
import com.biz4solutions.clientinvoice.requestWrapper.RequestOtpRequestWrapper;
import com.biz4solutions.clientinvoice.service.ICommonService;
import com.biz4solutions.clientinvoice.service.IOtpManagerService;
import com.biz4solutions.clientinvoice.util.CommonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class OtpManagerServiceImpl implements IOtpManagerService {

    private final static Logger logger = Logger.getLogger(OtpManagerServiceImpl.class);

    @Autowired
    private ICommonService commonService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private OtpTransactionRepository otpTransactionRepository;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private RoleRepository roleRepository;



    @Transactional
    @Override
    public OtpRequestDTO requestOtp(RequestOtpRequestWrapper requestOtpRequestWrapper, String acceptLanguage) throws InvoiceManagementException, MessagingException {

        UserIdentity userIdentity = userIdentityRepository.findOneByEmailIgnoreCaseAndRole_RoleType(requestOtpRequestWrapper.getEmail(), RoleType.USER);
        if (userIdentity == null) {
            userIdentity = new UserIdentity();
            userIdentity.setEmail(requestOtpRequestWrapper.getEmail());
//            Role role = roleRepository.findByRoleType(RoleType.valueOf(requestOtpRequestWrapper.getRole().toUpperCase())).orElseThrow(() -> new InvoiceManagementException("role"));
//            userIdentity.setRole(role);
//            JobGlobalConfig jobGlobalConfig = jobGlobalConfigService.getJobGlobalConfig();
//            userIdentity.setVerificationStatus(VerificationStatus.NONE);
//            userIdentity = userIdentityRepository.saveAndFlush(userIdentity);
        }

        Long count = otpTransactionRepository.countAllByUserIdentityAndCreatedAtAfter(userIdentity, DateUtils.addHours(new Date(), -24));
        AppConfig appConfig = appConfigRepository.findOneByKey(AppConfigConstants.FORGOT_PWD_MAX_RETRIES);
        if (null != count && appConfig != null && count >= Long.parseLong(appConfig.getValue().trim())) {
            throw new InvoiceManagementException(commonService.getMessage("forgotPwdMaxRetriesReached"));
        }

        OtpTransaction otpTransaction = new OtpTransaction();
        String id = CommonUtil.getUniqueToken();

        //checked if randomly generated transaction id is available then create new unique id
        while (otpTransactionRepository.findOneByTransactionId(id) != null) { // gives unique id
            id = getUniqueToken();
        }

        otpTransaction.setTransactionId(id);
        //otpTransaction.setEmail(requestOtpRequestWrapper.getEmail());
        otpTransaction.setVerified(false);
        otpTransaction.setUserIdentity(userIdentity);
        otpTransaction = otpTransactionRepository.saveAndFlush(otpTransaction);
        Otp otp = new Otp();
        otp.setExpireOn(CommonUtil.getTimeAfterMinutes(AppConfigConstants.EXPIRE_TIME));
        otp.setOtpTransaction(otpTransaction);
        otp.setOtpValue(CommonUtil.getUniqueOtp());
        otpRepository.saveAndFlush(otp);
        OtpRequestDTO otpRequestDTO = new OtpRequestDTO();
        otpRequestDTO.setTransactionId(id);

        sendMail(otp.getOtpValue(), userIdentity.getEmail(), acceptLanguage);

        return otpRequestDTO;

    }

    @Override
    @Transactional
    public OtpRequestDTO resendOtp(RequestOtpRequestWrapper requestWrapper, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException, MessagingException {
        UserIdentity user = userIdentityRepository.findTop1ByEmailIgnoreCase(requestWrapper.getEmail());
        if (user == null) {
            throw new InvoiceManagementException(commonService.getMessageFromDatabase("emailNotRegisterd"), 500);
        } else {
            Long count = otpTransactionRepository.countAllByUserIdentityAndCreatedAtAfter(user, DateUtils.addHours(new Date(), -24));
            AppConfig appConfig = appConfigRepository.findOneByKey(AppConfigConstants.FORGOT_PWD_MAX_RETRIES);
            if (null != count && appConfig != null && count >= Long.parseLong(appConfig.getValue().trim())) {
                throw new InvoiceManagementException(commonService.getMessage("forgotPwdMaxRetriesReached"));
            }
        }

        OtpTransaction otpTransaction = otpTransactionRepository.findOneByTransactionId(requestWrapper.getTransactionId());
        if (otpTransaction != null) {
            List<Otp> otps = otpRepository.findAllByOtpTransactionId(UUID.fromString(String.valueOf(otpTransaction.getId())));
            if (otps.size() >= 3) {
                throw new InvoiceManagementException(commonService.getMessage("maxOtpRequest"), 500);
            } else {
                for (Otp otp : otps) {
                    otp.setIsActive(false);
                    otpRepository.save(otp);
                }

                Otp otp = new Otp();
                otp.setExpireOn(new Date(new Date().getTime() + AppConfigConstants.EXPIRE_TIME * 1000 * 60 * 60));
                otp.setOtpTransaction(otpTransaction);
                otp.setOtpValue(CommonUtil.getUniqueOtp());
                otpRepository.save(otp);
                OtpRequestDTO otpRequestDTO = new OtpRequestDTO();
                otpRequestDTO.setTransactionId(otpTransaction.getTransactionId());
                UserIdentity userIdentity = userIdentityRepository.findTop1ByEmailIgnoreCase(requestWrapper.getEmail());
                Boolean isWeb = Boolean.valueOf(commonService.getIsWeb());

                sendMail(otp.getOtpValue(), userIdentity.getEmail(), acceptLanguage);

                return otpRequestDTO;
            }

        } else {
            throw new InvoiceManagementException(commonService.getMessage("invalidRequest"), 500);
        }
    }

    @Override
    @Transactional
    public UserDTO otpVerify(OtpVerifyRequesWrapper otpVerifyRequesWrapper, String acceptLanguage, CommonRequestHeaders commonRequestHeaders) throws InvoiceManagementException {
        //Boolean isValid = false;
        UserDTO loginDTO = new UserDTO();
        OtpTransaction otpTransaction = otpTransactionRepository.findOneByTransactionId(otpVerifyRequesWrapper.getTransactionId());
        if (otpTransaction != null) {
            List<Otp> otps = otpTransaction.getOtps();
            for (Otp otp : otps) {
                if (otpVerifyRequesWrapper.getOtp().equals(otp.getOtpValue()) && otp.getExpireOn().getTime() > new Date().getTime() && otp.getIsActive()) {
                    otpTransaction.setVerified(true);
                    otpTransactionRepository.save(otpTransaction);
                    //isValid = true;
                    loginDTO.setValid(true);
                }
            }
            if (loginDTO.isValid()) {
                UserProfile userProfile = userProfileRepository.findOneByUserIdentityAndIsActive(otpTransaction.getUserIdentity(), Boolean.TRUE);
                if (userProfile == null) {
                    userProfile = new UserProfile();
                    userProfile.setUserIdentity(otpTransaction.getUserIdentity());
//                    userProfile.setCreditAmount(0.0);
//                    userProfile = userProfileRepository.saveAndFlush(userProfile);
                }

                LoginRequestWrapper loginRequestWrapper = new LoginRequestWrapper();
                loginRequestWrapper.setEmail(otpTransaction.getUserIdentity().getEmail());
//                loginRequestWrapper.setRole(RoleType.USER);
//                loginDTO.setValid(true);
//                loginDTO = appUserService.loginEmail(loginRequestWrapper, commonRequestHeaders);
            }
        }
        return loginDTO;
    }

    public String getUniqueToken() {
        String token = "";
        do {
            token = CommonUtil.getUniqueToken();
            UserIdentity userIdentity = userIdentityRepository.findOneByUniqueId(token);
            if (userIdentity == null) {
                break;
            }
        } while (true);
        return token;
    }

    public void sendMail(String code, String email, String acceptLanguage) throws MessagingException {
        MailDTO mail = new MailDTO();
        String[] toEmail = new String[1];
        toEmail[0] = email;
        mail.setMailTo(toEmail);

        String FORGOT_PASSWORD_SUBJECT = "Verification code for login is.";//commonService.getMessageFromDatabase("forgotOtpSubject");
        mail.setMailSubject(FORGOT_PASSWORD_SUBJECT + " " + code);
        Map<String, Object> model = new HashMap<>();
        model.put("code", code);
        mail.setModel(model);

        mailClient.prepareAndSend(mail, MailType.EMAIL_OTP);
    }
}

