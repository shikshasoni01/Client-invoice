package com.biz4solutions.clientinvoice.service.Impl;

import com.biz4solutions.clientinvoice.constant.WebConstants;
import com.biz4solutions.clientinvoice.domain.Role;
import com.biz4solutions.clientinvoice.domain.UserIdentity;
import com.biz4solutions.clientinvoice.domain.UserPicture;
import com.biz4solutions.clientinvoice.domain.UserProfile;
import com.biz4solutions.clientinvoice.dto.*;
import com.biz4solutions.clientinvoice.enumerator.MailType;
import com.biz4solutions.clientinvoice.enumerator.RoleType;
import com.biz4solutions.clientinvoice.enumerator.SignupType;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.repository.RoleRepository;
import com.biz4solutions.clientinvoice.repository.UserIdentityRepository;
import com.biz4solutions.clientinvoice.repository.UserPictureRepository;
import com.biz4solutions.clientinvoice.repository.UserProfileRepository;
import com.biz4solutions.clientinvoice.requestWrapper.AddUserRequestWrapper;
import com.biz4solutions.clientinvoice.requestWrapper.UpdateUserProfileRequestWrapper;
import com.biz4solutions.clientinvoice.requestWrapper.UserPictureRequestWrapper;
import com.biz4solutions.clientinvoice.service.ICommonService;
import com.biz4solutions.clientinvoice.service.IUserService;
import com.biz4solutions.clientinvoice.util.CommonUtil;
import org.apache.http.util.TextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements IUserService, UserDetailsService {


    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private UserPictureRepository userPictureRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        if (request.getHeader(WebConstants.HEADER_KEY_DEVICE_TYPE) == null) {
            throw new OAuth2Exception("Invalid request");
        }

        String deviceType = request.getHeader(WebConstants.HEADER_KEY_DEVICE_TYPE);
        UserIdentity user = userIdentityRepository.findOneByEmailIgnoreCase(username);


        if (user == null) {
            try {
                throw new OAuth2Exception(
                        messageSource.getMessage("enterEmail", null,
                                new Locale(httpServletRequest.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE)))
                );
            } catch (NoSuchMessageException e) {
                e.printStackTrace();
            }
        }
        if ( (user != null && user.getAuthPassword() == null)) {
            try {
                throw new OAuth2Exception(
                        messageSource.getMessage("enterPassword", null,
                                new Locale(httpServletRequest.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE)))
                );
            } catch (NoSuchMessageException e) {
                e.printStackTrace();
            }
        }

        if(user!=null && user.getIsEmailVerified()== false) {
            try{
                throw new OAuth2Exception(
                        messageSource.getMessage("emailIsNotVerified",null,
                                new Locale(httpServletRequest.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE))));
            }catch (NoSuchMessageException e){
                e.printStackTrace();
            }
        }

        if (null != user && user.getDelete() != null && user.getDelete()) {
            throw new OAuth2Exception(messageSource.getMessage("deactivatedAccount", null,
                    new Locale(httpServletRequest.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE))));
        }

        if (null != user && user.getIsBlocked() != null && user.getIsBlocked()) {
            throw new OAuth2Exception(messageSource.getMessage("blockedAccount", null,
                    new Locale(httpServletRequest.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE))));
        }

        Boolean isWeb = Boolean.valueOf(commonService.getIsWeb());

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleType()));

        String password = user.getAuthPassword();
        String signupType =  request.getHeader("socialSignup");

        return new org.springframework.security.core.userdetails.User(user.getEmail(), password,
                simpleGrantedAuthorities);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String createUser(AddUserRequestWrapper request, String acceptLanguage, boolean isFromImport
    ) throws Exception {

        UserIdentity loggedInUser = null;

        if (!isFromImport) {
            loggedInUser = commonService.getLoggedInUserIdentity(acceptLanguage);
            checkEmailAndPhoneNumberValidations(request, acceptLanguage);
        }

        UserIdentity userIdentity = new UserIdentity();
        userIdentity.setEmail(request.getEmail());
        List<RoleType> roleTypes = new ArrayList<>();
        for (String roleType : request.getRoleType()) {
            roleTypes.add(RoleType.valueOf(roleType.replaceAll(" ", "_")));
        }

        Role role = roleRepository.findOneByRoleType(roleTypes.get(0));
        if (role != null) {
            userIdentity.setRole(role);
        } else {
            throw new InvoiceManagementException(messageSource.getMessage("roleNotFound", null, new Locale(acceptLanguage)),
                    500);
        }

        // Set user profile data and address
        UserProfile userProfile = new UserProfile();

        // Create address object
        userProfile.setUserIdentity(userIdentity);

        setUserProfile(request, userProfile);
        userIdentity.setUserProfile(userProfile);

        userIdentity = userIdentityRepository.saveAndFlush(userIdentity);

        userIdentity.setIsEmailVerified(true);
        userIdentity.setSignupType(SignupType.APP);

        if(RoleType.MANAGER.getRoleType().equalsIgnoreCase(role.getRoleType().getRoleType()))
            userIdentity.setSignupType(SignupType.WEB);

        String password = CommonUtil.generateRandomPassword(10);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userIdentity.setAuthPassword(passwordEncoder.encode(password));

        userIdentity = userIdentityRepository.save(userIdentity);
        userProfile = userIdentity.getUserProfile();
        userProfile.setSetPasswordToken("");

        userProfileRepository.saveAndFlush(userProfile);

        return userIdentity.getId().toString(); // to call upload profile image api

    }

    private void checkEmailAndPhoneNumberValidations(AddUserRequestWrapper request, String acceptLanguage
    ) throws InvoiceManagementException {

        UserIdentity userWithRequestedEmail = userIdentityRepository.findOneByEmailIgnoreCase(request.getEmail());

        if (userWithRequestedEmail != null && !userWithRequestedEmail.getDelete())
            throw new InvoiceManagementException(messageSource.getMessage("emailAlreadyExist", null, new Locale(acceptLanguage)),
                    500);
        else if (userWithRequestedEmail != null && userWithRequestedEmail.getDelete())
            throw new InvoiceManagementException(
                    messageSource.getMessage("emailAlreadyExitsActivateDeletedUser", null, new Locale(acceptLanguage)),
                    500);

        UserIdentity userWithRequestedPhoneNumber = userIdentityRepository.findOneByContactNo(request.getPhoneNumber());
        if (userWithRequestedPhoneNumber != null && !userWithRequestedPhoneNumber.getDelete())
            throw new InvoiceManagementException(
                    messageSource.getMessage("phoneNumberAlreadyExists", null, new Locale(acceptLanguage)), 500);
        else if (userWithRequestedEmail != null && userWithRequestedEmail.getDelete())
            throw new InvoiceManagementException(messageSource.getMessage("phoneNumberAlreadyExistsActivateDeletedUser", null,
                    new Locale(acceptLanguage)), 500);

    }

    private void rolePermissionValidations(AddUserRequestWrapper request, String acceptLanguage,
                                           UserIdentity loggedInUser) throws InvoiceManagementException {

        // If logged in user is admin he can create all users
        Boolean isAdmin = false;

        for (String roleType : request.getRoleType()) {
            if (roleType.equals(RoleType.MANAGER.getRoleType()))
                isAdmin = true;
        }
    }

    /**
     * Set user profile from request object
     */
    private void setUserProfile(AddUserRequestWrapper request, UserProfile userProfile) {
        BeanUtils.copyProperties(request, userProfile);
    }



    @Override
    public UserDetailsDTO updateUserProfile(AddUserRequestWrapper request, String acceptLanguage) throws Exception {
        UserIdentity loggedInUser = commonService.getLoggedInUserIdentity(acceptLanguage);

        UserIdentity userIdentity = userIdentityRepository.findOneById(Long.valueOf(String.valueOf(Long.valueOf(request.getUserId()))));

        if (userIdentity != null) {
            checkUpdateEmailAndPhoneValidation(request, acceptLanguage);

            String oldEmail = userIdentity.getEmail();
            userIdentity.setEmail(request.getEmail());
            userIdentity.setEmployeeId ( request.getEmployeeId () );
            userIdentity.setDepartment ( request.getDepartment () );
            userIdentityRepository.save(userIdentity);
            if (!oldEmail.equalsIgnoreCase(request.getEmail()) && !userIdentity.getIsEmailVerified()) {
                String emailChangMsg = "Your email id has been changed.";
                sendEmailVerificationLink(loggedInUser, userIdentity.getUserProfile(), request.getEmail(),
                        emailChangMsg, acceptLanguage);
            }

            List<RoleType> roleTypes = new ArrayList<>();
            for (String roleType : request.getRoleType()) {
                roleTypes.add(RoleType.valueOf(roleType.replaceAll(" ", "_")));
            }

            List<String> userRoleTypes = new ArrayList<>();
            userRoleTypes.add(userIdentity.getRole().getRoleType().getRoleType());

            if (!userRoleTypes.get(0).equalsIgnoreCase(request.getRoleType().get(0))) {
                Role role = roleRepository.findOneByRoleType(roleTypes.get(0));
                if (role != null) {
                    userIdentity.setRole(role);
                } else {
                    throw new InvoiceManagementException(commonService.getMessage("roleNotFound"),
                            500);
                }
            }

            return null;
        } else {
            throw new InvoiceManagementException(commonService.getMessage("userNotFound"),
                    500);
        }
    }


    @Override
    public UserDTO getUserProfile(Long userId, String acceptLanguage) throws InvoiceManagementException {
        UserIdentity userIdentity = userIdentityRepository.findOneById(Long.valueOf((userId)));
        if (userIdentity == null) {
         throw new InvoiceManagementException(messageSource.getMessage("userNotFound", null, new Locale(acceptLanguage)),
                    404);
        }
        return getUserDetails(userIdentity, acceptLanguage);
    }

    @Override
    public UserDetailsDTO getMyUserProfile(String acceptLanguage) throws InvoiceManagementException {
        return null;
    }


    @Override
    public List<UserDTO> getUserListInfo(List<String> userIdList, String acceptLanguage) {
        List<UserDTO> userDTOList =  new ArrayList<>();

        if (!CollectionUtils.isEmpty(userIdList)) {
            List<UUID> uuidList = CommonUtil.getUUIDListFromStringList(userIdList);
            List<UserIdentity> userIdentityList = userIdentityRepository.findAllByIdIn(uuidList);
            for (UserIdentity userIdentity : userIdentityList) {
                UserDTO userDTO = new UserDTO(userIdentity);
                userDTOList.add(userDTO);
            }
        }
        return userDTOList;
    }

    /**
     * Get user details object of passed user(client or sponsor)
     *
     * @param userIdentity
     * @param acceptLanguage
     * @return
     */
    private UserDTO getUserDetails(UserIdentity userIdentity, String acceptLanguage) throws InvoiceManagementException {

        List<String> roleTypes;

        UserIdentity loginUser = commonService.getLoggedInUserIdentity(acceptLanguage);
        if (loginUser == null) {
            throw new InvoiceManagementException(messageSource.getMessage("userNotFound", null, new Locale(acceptLanguage)),
                    404);
        }

        return getUserDetailsDTO(userIdentity, loginUser);
    }

    @Override
    public UserDTO getUserDetailsDTO(UserIdentity userIdentity, UserIdentity loginUser) throws InvoiceManagementException {
        UserDTO userDetailsDTO = new UserDTO();

        // set the profile image data
        UserProfile userProfile = userIdentity.getUserProfile();

        if (userProfile != null) {
            BeanUtils.copyProperties(userProfile, userDetailsDTO);
        }

        BeanUtils.copyProperties(userIdentity, userDetailsDTO);

        return userDetailsDTO;
    }


    @Override
    public void logout(String acceptLanguage) throws InvoiceManagementException {
        UserIdentity userIdentity = commonService.getLoggedInUserIdentity(acceptLanguage);
        if (userIdentity != null) {
            userIdentity.setLastLoginDeviceToken("");
            userIdentity.setLastLoginDeviceId(null);
            userIdentityRepository.save(userIdentity);
        }
    }

    @Override
    public void activateUser(String userId, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException {

    }


    public void sendEmailVerificationLink(UserIdentity loggedInUser, UserProfile userProfile, String email,
                                          String emailChangeMsg, String acceptLanguage) throws Exception {
        MailDTO mail = new MailDTO();
        String[] toEmail = new String[1];
        toEmail[0] = email;
        mail.setMailTo(toEmail);

        //String verificationLink;
        //String token;
        ModelMap map = new ModelMap();
        map.put("email", email);
        map.put("random", CommonUtil.getUniqueToken());


        String USER_SIGNUP_SUBJECT = commonService.getMessage("userSignUpSubject");
        mail.setMailSubject(USER_SIGNUP_SUBJECT);

        Map<String, Object> model = new HashMap<>();
        model.put("firstName", userProfile.getUserIdentity().getFirstName());
        model.put("adminName", loggedInUser.getFirstName());
        model.put("emailChangeMsg", emailChangeMsg);
        mail.setModel(model);

        mailClient.prepareAndSend(mail, MailType.SIGNUP);
    }


    public void checkUpdateEmailAndPhoneValidation(UpdateUserProfileRequestWrapper request, String acceptLanguage)
            throws NoSuchMessageException, InvoiceManagementException {

        UserIdentity userWithRequestedEmail = userIdentityRepository.findTop1ByEmailIgnoreCaseAndIdNot(
                request.getEmail(), CommonUtil.getUUIDFromStringIfValidElseNull(request.getUserId()));

        if (userWithRequestedEmail != null && !userWithRequestedEmail.getDelete())
            throw new InvoiceManagementException(commonService.getMessage("emailAlreadyExist"),
                    500);

        if (userWithRequestedEmail != null && userWithRequestedEmail.getDelete())
            throw new InvoiceManagementException(
                    commonService.getMessage("emailAlreadyExistsActivateDeletedUser"),
                    500);

        UserIdentity userWithRequestedPhoneNumber = userIdentityRepository.findTop1ByContactNoAndIdNot(
                request.getPhoneNumber(), CommonUtil.getUUIDFromStringIfValidElseNull(request.getUserId()));

        if (userWithRequestedPhoneNumber != null && !userWithRequestedPhoneNumber.getDelete())
            throw new InvoiceManagementException(
                    commonService.getMessage("phoneNumberAlreadyExists"), 500);

        if (userWithRequestedPhoneNumber != null && userWithRequestedPhoneNumber.getDelete())
            throw new InvoiceManagementException(commonService.getMessage("phoneNumberAlreadyExistsActivateDeletedUser"), 500);
    }


    @Override
    public UserProfileImagesDTO getUserImages(String acceptLanguage) throws InvoiceManagementException {
        UserProfileImagesDTO profileImagesDTO = new UserProfileImagesDTO();
        UserIdentity userIdentity = commonService.getLoggedInUserIdentity(acceptLanguage);

        if (userIdentity == null) {
            throw new InvoiceManagementException(commonService.getMessage("userNotFound"),
                    500);
        } else {
            List<PictureDTO> pictureDTOList = getUserPicturesDTOList(userIdentity);
            profileImagesDTO.setUserPictures(pictureDTOList);
        }

        return profileImagesDTO;
    }

    private List<PictureDTO> getUserPicturesDTOList(UserIdentity userIdentity) {

        List<UserPicture> userPictures = userPictureRepository.findByUserIdentityAndIsActive(userIdentity, true);
        List<PictureDTO> pictureDTOList = new ArrayList<PictureDTO>();

        boolean isLoggedInUser = commonService.isLoggedInUser(userIdentity);

        if (!CollectionUtils.isEmpty(userPictures)) {
            for (UserPicture userPicture : userPictures) {
                PictureDTO pictureDTO = new PictureDTO();

                if (!TextUtils.isEmpty(userPicture.getBucketName()) && !TextUtils.isEmpty(userPicture.getKeyString())) {
                    pictureDTO.setPicURL(userPicture.getPicURL());
                } else {
                    pictureDTO.setPicURL("");
                }

                pictureDTO.setId(userPicture.getId().toString());
                pictureDTO.setUserId(userPicture.getUserIdentity().getId().toString());
                pictureDTO.setIsDefaultImage(userPicture.getIsDefaultImage());
                pictureDTO.setPicURL(userPicture.getPicURL());
                pictureDTOList.add(pictureDTO);

            }
        }

        return pictureDTOList;
    }

    @Override
    public String addUserImage(UserPictureRequestWrapper request, String acceptLanguage) throws
            InvoiceManagementException {

        UserIdentity userIdentity = commonService.getLoggedInUserIdentity(acceptLanguage);

        if (userIdentity == null) {
            throw new InvoiceManagementException(commonService.getMessage("userNotFound"),
                    500);
        } else {
            List<UserPicture> userPicture = userPictureRepository.findByUserIdentityAndPicURLAndIsActive(userIdentity, request.getPicURL(), true);
            if (!CollectionUtils.isEmpty(userPicture)) {
                throw new InvoiceManagementException(commonService.getMessage("pictureAlreadyExistsForUser"),
                        500);
            } else {
                int totalPics = userPictureRepository.findPicturesCountByUserIdentityAndIsActive(userIdentity.getId(), true);
                if (totalPics >= WebConstants.MAX_PICS_LIMIT_PER_USER) {
                    throw new InvoiceManagementException(commonService.getMessage("maxPicsLimitRichedForUser"),
                            500);
                } else {
                    UserPicture picture = new UserPicture();
                    picture.setUserIdentity(userIdentity);
                    picture.setPicURL(request.getPicURL());
                    picture.setBucketName(request.getBucketName());
                    picture.setKeyString(request.getKeyString());
                    picture = userPictureRepository.saveAndFlush(picture);
                    if (totalPics == 0) {
                        UserProfile userProfile = userIdentity.getUserProfile();
                        if (userProfile == null) {
                            userProfile = userProfileRepository.findOneByUserIdentityAndIsActive(userIdentity, Boolean.TRUE);
                        }

                        userProfile.setDefaultPic(picture);
                        userProfile = userProfileRepository.saveAndFlush(userProfile);

                        picture.setIsDefaultImage(true);
                    } else {
                        picture.setIsDefaultImage(false);
                    }
                    picture = userPictureRepository.saveAndFlush(picture);
                }

            }
        }

        return userIdentity.getId().toString();
    }

    @Override
    public String setDefaultImage(UserPictureRequestWrapper request, String acceptLanguage) throws
            InvoiceManagementException {

        UserIdentity userIdentity = commonService.getLoggedInUserIdentity(acceptLanguage);

        if (userIdentity == null) {
            throw new InvoiceManagementException(commonService.getMessage("userNotFound"),
                    500);
        } else {
            UserPicture userPicture = userPictureRepository.findOneByUserIdentityAndIdAndIsActive(userIdentity, CommonUtil.getUUIDFromStringIfValidElseNull(request.getId()), true);
            if (userPicture == null) {
                throw new InvoiceManagementException(commonService.getMessage("picNotFoundwithThisUser"),
                        500);
            } else {
                unsetPreviousDefaultImage(userIdentity);
                userPicture.setIsDefaultImage(true);
                userPictureRepository.saveAndFlush(userPicture);

                UserProfile userProfile = userIdentity.getUserProfile();
                if (userProfile == null) {
                    userProfile = userProfileRepository.findOneByUserIdentityAndIsActive(userIdentity, Boolean.TRUE);
                }

                userProfile.setDefaultPic(userPicture);
                userProfile = userProfileRepository.saveAndFlush(userProfile);
            }
        }
        return userIdentity.getId().toString();
    }

    private void unsetPreviousDefaultImage(UserIdentity userIdentity) {
        UserPicture userPicture = userPictureRepository.findOneByUserIdentityAndIsDefaultImageAndIsActive(userIdentity, true, true);
        if (userPicture != null) {
            userPicture.setIsDefaultImage(false);
            userPictureRepository.saveAndFlush(userPicture);
        }
    }


    @Override
    public String deleteUserImage(UserPictureRequestWrapper request, String acceptLanguage) throws
           InvoiceManagementException {

        UserIdentity userIdentity = commonService.getLoggedInUserIdentity(acceptLanguage);

        if (userIdentity == null) {
            throw new InvoiceManagementException(commonService.getMessageFromDatabase("userNotFound"),
                    500);
        } else {
            UserPicture userPicture = userPictureRepository.findOneByUserIdentityAndIdAndIsActive(userIdentity, CommonUtil.getUUIDFromStringIfValidElseNull(request.getId()), true);
            if (userPicture == null) {
                throw new InvoiceManagementException(commonService.getMessageFromDatabase("picNotFoundwithThisUser"),
                        500);
            } else {
                if (userPicture.getIsDefaultImage()) {
                    throw new InvoiceManagementException(commonService.getMessageFromDatabase("cantDeleteDefaultImage"),
                            500);
                } else {
                    userPictureRepository.delete(userPicture);
                }
            }
        }

        return userIdentity.getId().toString();
    }


    @Override
    public List<UserDetailsDTO> getAllUser() throws InvoiceManagementException{

        List<UserIdentity> userIdentities=userIdentityRepository.findAll ();
        List<UserDetailsDTO> userDetailsDTOS=new ArrayList<> ();

        for (UserIdentity userIdentity:userIdentities){
            UserDetailsDTO userDetailsDTO=new UserDetailsDTO ();
            userDetailsDTO.setUserId (userIdentity.getId ());
            userDetailsDTO.setEmail ( userIdentity.getEmail () );
            userDetailsDTO.setFirstName ( userIdentity.getFirstName () );
            userDetailsDTO.setLastName ( userIdentity.getLastName () );
            userDetailsDTO.setContactNo ( userIdentity.getContactNo ());
            userDetailsDTO.setCountryCode ( userIdentity.getCountryCode ());
            userDetailsDTO.setRoleType ( userIdentity.getRole ( ).getRoleType ( ) );
            userDetailsDTOS.add(userDetailsDTO);

        }

        return userDetailsDTOS;

    }

}


