package com.management.clientinvoice.service;

import java.util.List;
import com.management.clientinvoice.domain.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserInfoService {

	List<UserInfo> getAllActiveUserInfo();

	UserInfo addUser(UserInfo userRecord);

	UserInfo updateUser(Integer id, UserInfo userRecord);

	UserInfo updatePassword(Integer id, UserInfo userRecord);

	UserInfo updateRole(Integer id, UserInfo userRecord);

	void deleteUser(Integer id);

	UserInfo getUserInfoById(Integer id);

	UserInfo getUserInfoByUserName(String userName);

}
