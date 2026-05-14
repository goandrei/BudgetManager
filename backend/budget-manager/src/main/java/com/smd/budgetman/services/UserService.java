package com.smd.budgetman.services;

import com.smd.budgetman.vo.UserAuthorizeRequestVo;
import com.smd.budgetman.vo.UserAuthorizeResponseVo;
import com.smd.budgetman.vo.UserLoginRequestVo;
import com.smd.budgetman.vo.UserRequestVo;
import com.smd.budgetman.vo.UserTokenResponseVo;
import com.smd.budgetman.vo.UserUpdateVo;
import java.text.ParseException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserRequestVo findByUserId(Long userId) {
        return null;
    }

    public UserTokenResponseVo registerNewUser(UserRequestVo userRequestVo) {
        return null;
    }

    public UserTokenResponseVo validateUserCredentialsAndGenerateToken(UserLoginRequestVo userRequestVo) {
        return null;
    }

    public UserAuthorizeResponseVo authorizeV2(UserAuthorizeRequestVo userRequestVo) throws ParseException {
        return null;
    }

    public void updateUser(String token, UserUpdateVo vo) {}

    public void deleteUser(Long userId) {}
}
