package com.management.clientinvoice.config;



import com.management.clientinvoice.domain.Role;
import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.dto.UserDTO;
import com.management.clientinvoice.enumerator.RoleType;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.RoleRepository;
import com.management.clientinvoice.repository.UserIdentityRepository;
import com.management.clientinvoice.repository.UserProfileRepository;
import com.management.clientinvoice.requestWrapper.SignupBaseRequestWrapper;
import com.management.clientinvoice.service.IUserIdentityService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = Logger.getLogger(InitialDataLoader.class);

    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Environment env;

    @Autowired
    private IUserIdentityService userIdentityService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        LOGGER.info("Initial setup started");

        createRoleIfNotFound(RoleType.ADMIN);
        createRoleIfNotFound(RoleType.MANAGER);
        createRoleIfNotFound(RoleType.ACCOUNT);
        createRoleIfNotFound(RoleType.USER);

        LOGGER.info("Initial setup completed");
    }

    private void createAdminUserIfNotExists() throws InvoiceManagementException, MessagingException, UnsupportedEncodingException, com.sun.xml.messaging.saaj.packaging.mime.MessagingException {
        UserIdentity userIdentity = userIdentityRepository.findOneByEmailIgnoreCase(env.getProperty("admin_email"));

        if (null == userIdentity) {

            SignupBaseRequestWrapper request = new SignupBaseRequestWrapper();
            request.setRoleType(RoleType.MANAGER.getRoleType());
            request.setFirstName(env.getProperty("admin_FirstName"));
            request.setLastName(env.getProperty("admin_LastName"));
            request.setEmail(env.getProperty("admin_email"));
            request.setAuthPassword(env.getProperty("admin_password"));


            UserDTO userDTO= userIdentityService.signup(request, "en_US");
        }
    }

    @Transactional
    private Role createRoleIfNotFound(RoleType roleType) {

        Role role = roleRepository.findOneByRoleType(roleType);
        if (role == null) {
            role = new Role(roleType.getRoleType());
            roleRepository.save(role);
        }
        return role;
    }
}

