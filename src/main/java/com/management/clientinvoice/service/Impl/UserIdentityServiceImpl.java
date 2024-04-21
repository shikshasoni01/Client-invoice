package com.management.clientinvoice.service.Impl;


import com.management.clientinvoice.domain.Role;
import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.domain.UserProfile;
import com.management.clientinvoice.dto.UserDTO;
import com.management.clientinvoice.enumerator.RoleType;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.LoginHistoryRepository;
import com.management.clientinvoice.repository.RoleRepository;
import com.management.clientinvoice.repository.UserIdentityRepository;
import com.management.clientinvoice.repository.UserProfileRepository;
import com.management.clientinvoice.requestWrapper.SignupBaseRequestWrapper;
import com.management.clientinvoice.service.IUserIdentityService;
import com.management.clientinvoice.util.ValidationUtil;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@Transactional
public class UserIdentityServiceImpl implements IUserIdentityService {


    @Value("${server.url}")
    private String serverURL;

    @Autowired
    private  MailClient mailClient;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserIdentityValidator userIdentityValidator;

    @Autowired
    private ValidationUtil validationUtil;

    @Autowired
    private CommonServiceImpl commonService;

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public UserDTO signup(@Valid SignupBaseRequestWrapper request, String acceptLanguage) throws InvoiceManagementException, MessagingException, javax.mail.MessagingException, UnsupportedEncodingException {

        UserIdentity user = userIdentityRepository.findTop1ByEmailIgnoreCase(request.getEmail());
        UserProfile userProfile = null;

        userIdentityValidator.checkForUniqueEmail(request.getEmail());

        if(user == null) {
            user = new UserIdentity();
        }

        BeanUtils.copyProperties(request, user);

        validationUtil.validatePassword(request.getAuthPassword(), "en");
        userIdentityValidator.checkForUniqueContactNo(request.getContactNo());
        user.setCountryCode(request.getCountryCode());

        Role role = roleRepository.findOneByRoleType(RoleType.getEnum(request.getRoleType()));
        user.setRole(role);

        String randomUUID = UUID.randomUUID().toString();
        while (null != userIdentityRepository.findOneByUniqueId(randomUUID)) {
            randomUUID = UUID.randomUUID().toString();
        }

        user.setUniqueId(randomUUID);

        user = userIdentityRepository.saveAndFlush(user);

        userProfile = new UserProfile();
        userProfile.setUserIdentity(user);
        userProfile = userProfileRepository.saveAndFlush(userProfile);
        UserDTO userDTO = new UserDTO(userProfile.getUserIdentity());

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setIsActive(true);
        user.setIsEmailVerified(false);
        user = userIdentityRepository.saveAndFlush(user);
//        sendVerificationEmail(user,serverURL);

        return userDTO;
    }


    /**
     * Insert Template data for signup verification link
     */
    private void sendVerificationEmail(UserIdentity user, String siteURL)
            throws MessagingException, UnsupportedEncodingException, javax.mail.MessagingException {

        if(user == null){
            return;
        }
        String toAddress = user.getEmail();

        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Client Invoice Team.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);


        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        String verifyURL = serverURL + "/api/v1/users/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public boolean verify(String verificationCode) {
        UserIdentity user = userIdentityRepository.findByVerificationCode(verificationCode);

        if (user == null || user.getIsActive() && user.getIsEmailVerified()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setIsEmailVerified(true);
            user.setIsActive(true);
            userIdentityRepository.save(user);
            return true;
        }

    }


}
