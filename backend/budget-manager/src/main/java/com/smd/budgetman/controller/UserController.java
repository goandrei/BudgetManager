package com.smd.budgetman.controller;

import com.smd.budgetman.services.UserService;
import com.smd.budgetman.vo.UserAuthorizeRequestVo;
import com.smd.budgetman.vo.UserAuthorizeResponseVo;
import com.smd.budgetman.vo.UserLoginRequestVo;
import com.smd.budgetman.vo.UserRequestVo;
import com.smd.budgetman.vo.UserTokenResponseVo;
import com.smd.budgetman.vo.UserUpdateVo;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    @CrossOrigin
    public UserRequestVo getAnswersByQuestionId(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }

    @PostMapping("/user/register")
    @CrossOrigin
    public UserTokenResponseVo registerNewUser(@RequestBody UserRequestVo userRequestVo) {
        return userService.registerNewUser(userRequestVo);
    }

    @PostMapping("/user/authenticate")
    @CrossOrigin
    public UserTokenResponseVo login(@RequestBody UserLoginRequestVo userRequestVo) {
        return userService.validateUserCredentialsAndGenerateToken(userRequestVo);
    }

    @PostMapping("/user/authorize")
    @CrossOrigin
    public UserAuthorizeResponseVo authorize(@RequestBody UserAuthorizeRequestVo userRequestVo) throws ParseException {
        return userService.authorizeV2(userRequestVo);
    }

    @PostMapping("/user/update")
    @CrossOrigin
    public void updateUser(@RequestHeader("Authorization") String authorization, @RequestBody UserUpdateVo vo) {

        String token = authorization.replace("Bearer ", "");
        userService.updateUser(token, vo);
    }

    @DeleteMapping("/user/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
