package com.biz4solutions.clientinvoice.service.Impl;

import com.biz4solutions.clientinvoice.constant.AppConfigConstants;
import com.biz4solutions.clientinvoice.domain.AppConfig;
import com.biz4solutions.clientinvoice.domain.Otp;
import com.biz4solutions.clientinvoice.domain.OtpTransaction;
import com.biz4solutions.clientinvoice.domain.UserIdentity;
import com.biz4solutions.clientinvoice.dto.MailDTO;
import com.biz4solutions.clientinvoice.dto.OtpRequestDTO;
import com.biz4solutions.clientinvoice.dto.UserDTO;
import com.biz4solutions.clientinvoice.enumerator.MailType;
import com.biz4solutions.clientinvoice.enumerator.RoleType;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.repository.*;
import com.biz4solutions.clientinvoice.requestWrapper.*;
import com.biz4solutions.clientinvoice.service.ICommonService;
import com.biz4solutions.clientinvoice.service.IPasswordManagerService;
import com.biz4solutions.clientinvoice.util.CommonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class PasswordManagerServiceImpl implements IPasswordManagerService {
    private final static Logger logger = Logger.getLogger(PasswordManagerServiceImpl.class);
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private UserIdentityRepository userIdentityRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private OtpTransactionRepository otpTransactionRepository;
//    @Autowired
//    private JwtUtil jwtUtil;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private AppConfigRepository appConfigRepository;
    @Autowired
    private ICommonService commonService;

    @Override
    public void changePassword(ChangePasswordRequestWrapper changePasswordRequestWrapper, String lang) throws InvoiceManagementException {
        UserIdentity adminAccount = commonService.getLoggedInAdmin(lang);
        if (adminAccount == null) {
            throw new InvoiceManagementException(commonService.getMessageFromDatabase("adminNotFound"), 500);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(changePasswordRequestWrapper.getOldPassword(), adminAccount.getAuthPassword())) {
            adminAccount.setAuthPassword(passwordEncoder.encode(changePasswordRequestWrapper.getNewPassword()));
            userIdentityRepository.save(adminAccount);
        } else {
            throw new InvoiceManagementException(commonService.getMessage("oldPasswordDoesNotMatch"), 500);
        }
    }


    @Override
    public boolean forgotPassword(ForgotPasswordRequestWrapper forgotPasswordRequest, String lang) throws InvoiceManagementException {

        UserIdentity userIdentity = userIdentityRepository.findOneByEmailIgnoreCaseAndRole_RoleType(forgotPasswordRequest.getEmail(), RoleType.MANAGER);
        if (userIdentity != null) {
            OtpTransaction otpTransaction = otpTransactionRepository.findOneByTransactionId(forgotPasswordRequest.getTransactionId());
            if (otpTransaction != null) {
                if (otpTransaction.isVerified()) {
//                    UserIdentity userIdentity = null;
                 //    userIdentity = userIdentityRepository.findTop1ByEmailIgnoreCase(forgotPasswordRequest.getEmail());

                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    userIdentity.setAuthPassword(passwordEncoder.encode(forgotPasswordRequest.getPassword()));
                    userIdentityRepository.save(userIdentity);
                    return true;
                } else {
                    throw new InvoiceManagementException(commonService.getMessage("invalidRequest"), 500);
                }
            } else {
                throw new InvoiceManagementException(commonService.getMessage("invalidRequest"), 500);
            }
        } else {
            //throw new InvoiceManagementException(messageSource.getMessage("emailNotRegisterd", null,new Locale(lang), 500));
            throw new InvoiceManagementException(commonService.getMessage("emailNotRegisterd"));


        }
    }


    @Override
    public void resetPassword(ResetPasswordRequestWrapper resetPassword, String acceptLanguage) throws Exception {
      /*  ModelMap map = jwtUtil.getDataFromEmailToken(resetPassword.getToken());
        UserIdentity userIdentity;
        boolean validToken=false;
        if(map != null){
            userIdentity = userIdentityRepository.findTop1ByEmailIgnoreCase((String) map.get("email"));
			if (userIdentity != null) {
				String forgotPasswordRequest = userIdentity.getUserProfile().getSetPasswordToken();
				if (forgotPasswordRequest.equals(resetPassword.getToken()))
					validToken = true;
			} else {
				throw new InvoiceManagementException(commonService.getMessageFromDatabase("userNotFound", null, new Locale(acceptLanguage)), 500);
			}
        }else
            userIdentity = null;
        if(validToken){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            userIdentity.setAuthPassword(passwordEncoder.encode(resetPassword.getNewPassword()));

            userIdentity.setIsEmailVerified(true);
            userIdentityRepository.save(userIdentity);
            UserProfile userProfile= userIdentity.getUserProfile();
            userProfile.setSetPasswordToken("");
            userProfileRepository.saveAndFlush(userProfile);
        }else{
            throw new InvoiceManagementException(commonService.getMessageFromDatabase("resetPasswordLinkExpired"), 500);
        }*/

    }


    public OtpRequestDTO requestOtp(RequestOtpRequestWrapper requestOtpRequestWrapper, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException, MessagingException {
        UserIdentity userIdentity = userIdentityRepository.findOneByEmailIgnoreCaseAndRole_RoleType(requestOtpRequestWrapper.getEmail(), RoleType.MANAGER);

        if (userIdentity != null) {

            Long count = otpTransactionRepository.countAllByUserIdentityAndCreatedAtAfter(userIdentity, DateUtils.addHours(new Date(), -24));
            AppConfig appConfig = appConfigRepository.findOneByKey(AppConfigConstants.FORGOT_PWD_MAX_RETRIES);
            if (null != count && appConfig != null && count >= Long.parseLong(appConfig.getValue().trim())) {
                throw new InvoiceManagementException(commonService.getMessage("forgotPwdMaxRetriesReached"));
            }

            OtpTransaction otpTransaction = new OtpTransaction();
            String id = CommonUtil.getUniqueToken();
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
            otp = otpRepository.saveAndFlush(otp);
            OtpRequestDTO otpRequestDTO = new OtpRequestDTO();
            otpRequestDTO.setTransactionId(id);

            sendMail(otp.getOtpValue(), userIdentity.getEmail(), acceptLanguage);

            return otpRequestDTO;
        } else {
            throw new InvoiceManagementException(commonService.getMessage("emailNotRegisterd"), 500);
        }

    }


    public OtpRequestDTO resendOtp(RequestOtpRequestWrapper requestOtpRequestWrapper, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException, MessagingException {
        OtpTransaction otpTransaction = otpTransactionRepository.findOneByTransactionId(requestOtpRequestWrapper.getTransactionId());
        if (otpTransaction != null) {
            List<Otp> otps = otpTransaction.getOtps();
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
                UserIdentity userIdentity = userIdentityRepository.findTop1ByEmailIgnoreCase(requestOtpRequestWrapper.getEmail());
                Boolean isWeb = Boolean.valueOf(commonService.getIsWeb());

                sendMail(otp.getOtpValue(), userIdentity.getEmail(), acceptLanguage);


                return otpRequestDTO;
            }

        } else {
            throw new InvoiceManagementException(commonService.getMessage("invalidRequest"), 500);
        }
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

    @Override
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
        }
        return loginDTO;
    }


    public void sendMail(String code, String email, String acceptLanguage) throws MessagingException {
        MailDTO mail = new MailDTO();
        String[] toEmail = new String[1];
        toEmail[0] = email;
        mail.setMailTo(toEmail);

        String FORGOT_PASSWORD_SUBJECT = "Verification code for password reset is";//commonService.getMessageFromDatabase("forgotPasswordSubject");
        mail.setMailSubject(FORGOT_PASSWORD_SUBJECT + " " + code);
        Map<String, Object> model = new HashMap<>();
        //model.put("firstName", userIdentity.getFirstName());
        model.put("code", code);
        mail.setModel(model);

        mailClient.prepareAndSend(mail, MailType.EMAIL_PASSWORD);

    }


    @Override
    public void deleteOtpTransactionsAndOtp(UserIdentity userIdentity) throws InvoiceManagementException {
        if (userIdentity != null) {
            List<UUID> ids = otpTransactionRepository.findAllIdsByUserIdentity(userIdentity);
            if (!CollectionUtils.isEmpty(ids)) {
                otpRepository.deleteAllUsersWithIds(ids);
                otpTransactionRepository.deleteAllUsersWithIds(ids);
            }
        }
    }

}

