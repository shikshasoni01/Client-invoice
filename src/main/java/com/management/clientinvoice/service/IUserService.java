package com.management.clientinvoice.service;

import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.dto.UserDTO;
import com.management.clientinvoice.dto.UserDetailsDTO;
import com.management.clientinvoice.dto.UserProfileImagesDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.requestWrapper.AddUserRequestWrapper;
import com.management.clientinvoice.requestWrapper.UserPictureRequestWrapper;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.List;


@Service
public interface IUserService {

    UserDetailsDTO updateUserProfile(AddUserRequestWrapper updateUserProfileRequestWrapper, String acceptLanguage) throws InvoiceManagementException, ParseException, Exception;

    String createUser(AddUserRequestWrapper request, String acceptLanguage, boolean isFromImport) throws Exception;

    UserDTO getUserDetailsDTO(UserIdentity userIdentity, UserIdentity loginUser) throws InvoiceManagementException;

    void logout(String acceptLanguage) throws InvoiceManagementException;

    void activateUser(String userId, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException;

    UserDTO getUserProfile(Long userId, String acceptLanguage) throws InvoiceManagementException;

    UserDetailsDTO getMyUserProfile(String acceptLanguage) throws InvoiceManagementException;

    List<UserDTO> getUserListInfo(List<String> userIdList, String acceptLanguage);
    
    UserProfileImagesDTO getUserImages(String acceptLanguage) throws InvoiceManagementException;

    String addUserImage(UserPictureRequestWrapper request, String acceptLanguage) throws InvoiceManagementException;

    String deleteUserImage(UserPictureRequestWrapper request, String acceptLanguage) throws InvoiceManagementException;

    String setDefaultImage(UserPictureRequestWrapper request, String acceptLanguage) throws InvoiceManagementException;

    List<UserDetailsDTO> getAllUser() throws InvoiceManagementException;


}
