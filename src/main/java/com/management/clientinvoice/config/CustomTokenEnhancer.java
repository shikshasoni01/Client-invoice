package com.management.clientinvoice.config;


import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.dto.UserDTO;
import com.management.clientinvoice.repository.UserIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;

@Transactional
public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();
        UserDTO loginUserDTO = null;

        UserIdentity userIdentity = userIdentityRepository.findOneByEmailIgnoreCase(user.getUsername());
        loginUserDTO = new UserDTO(userIdentity);
        additionalInfo.put("data", loginUserDTO);
        additionalInfo.put("status", "success");
        additionalInfo.put("code", 200);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
